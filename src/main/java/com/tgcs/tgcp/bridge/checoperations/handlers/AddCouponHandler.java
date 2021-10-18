package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderCoupon;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.addcoupon.AddCouponResponse;
import com.tgcs.tgcp.bridge.checoperations.model.addcoupon.AddCouponResult;
import com.tgcs.tgcp.bridge.checoperations.model.addcoupon.CouponItem;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.EReceiptLine;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.exception.WebServiceException;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AddCouponHandler implements IHandler, ChecMessagePath {
    private static Logger logger = LoggerFactory.getLogger(AddTenderHandler.class);
    private BridgeSession bridgeSession;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        //TODO ADD_COUPON_CODE must be replaced by ADD_COUPON_BARCODE_SCAN_DATA_LABEL
        OrderCoupon orderCoupon = (OrderCoupon) new OrderCoupon()
                .setAmount(message.checkPathExists(ADD_COUPON_AMOUNT) ?
                        new BigDecimal(message.getOrDefaultNodeTextContent(ADD_COUPON_AMOUNT, null)).movePointLeft(2) : null)
                .setItemIdentifier(message.getOrDefaultNodeTextContent(ADD_COUPON_CODE, ""));

        String requestId = message.getOrDefaultNodeTextContent(ADD_COUPON_REQUEST_ID, null);
        try {
            bridgeSession.setOrderAdapter(sendAddCoupon(orderCoupon));
        } catch (ChecOperationException e) {
            // if we lost connection to the POS, we will send a status event to CHEC
            if (WebServiceException.TIMEOUT.equalsIgnoreCase(e.getErrorCode()))
                client.sendResponseToChecApp(createPOSBCStatusEvent(POSBCStatusType.SEVERITY_ERROR, POSBCStatusType.POS_CONNECTION_LOST));

            client.sendResponseToChecApp(createExceptionAddCouponResponse(e, requestId));
            return;
        }

        orderCoupon = bridgeSession.getAddedCoupon(orderCoupon);
        bridgeSession.getReceiptItemsCache().putIfAbsent(new EReceiptLine(orderCoupon.getId(), requestId, orderCoupon));

        // If first coupon, Send POS Receipt Header Event and Transaction start
        if (!bridgeSession.getReceiptItemsCache().isHeaderPrinted()) {
            client.sendResponseToChecApp(Ngp2ChecMapper.createTransactionStatusEvent(bridgeSession.getOrderAdapter(), requestId, TransactionStatusType.TRANSACTION_START.getValue()));
            client.sendResponseToChecApp(Ngp2ChecMapper.createPOSReceiptEventHeader(requestId));
            bridgeSession.getReceiptItemsCache().setHeaderPrinted(true);
        }

        // Send POS Receipt Coupon Event
        client.sendResponseToChecApp(Ngp2ChecMapper.createPOSReceiptEventCoupon(requestId, orderCoupon));

        // Send total event
        client.sendResponseToChecApp(Ngp2ChecMapper.createTotalEventFromOrder(bridgeSession.getOrderAdapter(), requestId));

        // Send add coupon response
        client.sendResponseToChecApp(createAddCouponResponse(orderCoupon, requestId));

    }

    private AddCouponResponse createAddCouponResponse(OrderCoupon orderCoupon, String requestId) {
        return new AddCouponResponse()
                .setAddCouponResult(new AddCouponResult()
                        .setRequestId(requestId)
                        .setCouponItem(new CouponItem()
                                .setDescription(orderCoupon.getDescription())
                                .setItemIdentifier(orderCoupon.getItemIdentifier())
                                .setRegularUnitPrice(orderCoupon.getValue().negate().toString())
                                .setQuantity(orderCoupon.getQuantity().toString())
                                .setValue(orderCoupon.getValue().toString())));
    }


    private AddCouponResponse createExceptionAddCouponResponse(ChecOperationException e, String requestId) {
        return new AddCouponResponse()
                .setAddCouponResult(new AddCouponResult().setRequestId(requestId)
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }

    private OrderAdapter sendAddCoupon(OrderCoupon orderCoupon) throws ChecOperationException {
        IWebService webService = context.getBean(IWebService.class);
        try {
            return webService.addCoupon(orderCoupon);
        } catch (Exception e) {
            logger.error("Failed to deal with AddCoupon");
            throw createChecOperationException(e);
        }
    }

    /**
     * This method will be called when we receive an exception from NGP.
     * It will parse the error and set the ErrorCode and ErrorMessage fields needed to provide CHEC a proper response.
     *
     * @param e The exception received from NGP
     * @return ChecOperationException
     */
    private ChecOperationException createChecOperationException(Exception e) {

        return exceptionHandler.retrieveWebServiceError(e);
    }

    /**
     * @param severity
     * @param status
     * @return
     */
    private POSBCStatusEvent createPOSBCStatusEvent(POSBCStatusType severity, POSBCStatusType status) {
        return POSBCStatusEvent.createPOSBCStatus(severity.getValue(), status.toString(), status.getValue());
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }
}
