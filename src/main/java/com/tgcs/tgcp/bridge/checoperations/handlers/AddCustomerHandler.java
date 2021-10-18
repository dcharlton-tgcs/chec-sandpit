package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderCustomer;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.addcustomer.AddCustomerResponse;
import com.tgcs.tgcp.bridge.checoperations.model.addcustomer.AddCustomerResult;
import com.tgcs.tgcp.bridge.checoperations.model.addcustomer.CustomerInfo;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.exception.WebServiceException;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class AddCustomerHandler implements IHandler, ChecMessagePath {
    private static Logger logger = LoggerFactory.getLogger(AddTenderHandler.class);
    private BridgeSession bridgeSession;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Autowired
    private ConfProperties confProperties;

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {

        // NoroE: changes related to different endpoint (barcode/add instead of customer/add)
        OrderCustomer orderCustomer = new OrderCustomer()
                .setCustomerId(message.getOrDefaultNodeTextContent(ADD_CUSTOMER_CUSTOMER_ID, ""));
        String requestId = message.getOrDefaultNodeTextContent(ADD_CUSTOMER_REQUEST_ID, "");

        try {
            checkForRestrictions();
            bridgeSession.setOrderAdapter(sendAddCustomerRequest(orderCustomer));
        } catch (ChecOperationException e) {
            // if we lost connection to the POS, we will send a status event to CHEC
            if (WebServiceException.TIMEOUT.equalsIgnoreCase(e.getErrorCode()))
                client.sendResponseToChecApp(createPOSBCStatusEvent(POSBCStatusType.SEVERITY_ERROR, POSBCStatusType.POS_CONNECTION_LOST));

            // NoroE todo: ok, we won't hang, but need to check better the exception handling
            client.sendResponseToChecApp(createExceptionAddCustomerResponse(e, requestId));
            return;
        }

        orderCustomer = bridgeSession.getOrderAdapter().getOrderCustomer();

        // If first customer, Send POS Receipt Header Event && Transaction start event
        if (!bridgeSession.getReceiptItemsCache().isHeaderPrinted()) {
            client.sendResponseToChecApp(Ngp2ChecMapper.createTransactionStatusEvent(bridgeSession.getOrderAdapter(), requestId, TransactionStatusType.TRANSACTION_START.getValue()));
            client.sendResponseToChecApp(Ngp2ChecMapper.createPOSReceiptEventHeader(requestId));
            bridgeSession.getReceiptItemsCache().setHeaderPrinted(true);
        }

        // Send POS Receipt Customer Event
        client.sendResponseToChecApp(Ngp2ChecMapper.createPOSReceiptEventCustomer(requestId, orderCustomer));

        // Send AddCustomer Response
        client.sendResponseToChecApp(createAddCustomerResponse(orderCustomer, requestId));

    }

    private AddCustomerResponse createExceptionAddCustomerResponse(ChecOperationException e, String requestId) {
        return new AddCustomerResponse()
                .setAddCustomerResult(new AddCustomerResult().setRequestId(requestId)
                        // NoroE do I need an empty CustomerInfo node?
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }

    /**
     * @param severity
     * @param status
     * @return
     */
    private POSBCStatusEvent createPOSBCStatusEvent(POSBCStatusType severity, POSBCStatusType status) {
        return POSBCStatusEvent.createPOSBCStatus(severity.getValue(), status.toString(), status.getValue());
    }

    private AddCustomerResponse createAddCustomerResponse(OrderCustomer orderCustomer, String requestId) {
        return new AddCustomerResponse()
                .setAddCustomerResult(new AddCustomerResult()
                        .setRequestId(requestId)
                        .setCustomerInfo(new CustomerInfo()
                                // Standard CHEC usually logs the loyalty card here
                                .setCustomerId(orderCustomer.getLoyaltyCard())
                                .setName(orderCustomer.getCustomerName().getFirstName()
                                        + " " + orderCustomer.getCustomerName().getLastName())
                        ));
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }


    private OrderAdapter sendAddCustomerRequest(OrderCustomer orderCustomer) throws ChecOperationException {
        IWebService webService = context.getBean(IWebService.class);
        try {
            return webService.addCustomer(orderCustomer);
        } catch (Exception e) {
            logger.error("Failed to deal with AddCustomer");
            throw createChecOperationException(e);
        }
    }

    private ChecOperationException createChecOperationException(Exception e) {
        return exceptionHandler.retrieveWebServiceError(e);
    }

    private boolean isItemInTransaction() {
        return bridgeSession.getOrderAdapter().getItemList().size() +
                bridgeSession.getOrderAdapter().getCouponList().size() > 0;

    }

    private void checkForRestrictions() throws ChecOperationException {
        if (confProperties.isLoyaltyCardIgnoreAdditionalScans() && bridgeSession.getOrderAdapter() != null &&
                bridgeSession.getOrderAdapter().getOrderCustomer() != null) {
            throw new ChecOperationException(ErrorCode.LOYALTY_ALREADY_SCANNED.name(),
                    ErrorCode.LOYALTY_ALREADY_SCANNED.getValue(), null);
        }
        if (confProperties.isLoyaltyCardFirstScanOnly() && bridgeSession.getOrderAdapter() != null && isItemInTransaction()) {
            throw new ChecOperationException(ErrorCode.LOYALTY_AFTER_ITEM.name(),
                    ErrorCode.LOYALTY_AFTER_ITEM.getValue(), null);
        }
    }
}