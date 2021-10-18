package com.tgcs.tgcp.bridge.webservice.ngp;

import com.tgcs.posbc.bridge.eft.wrapper.client.EftWrapperClient;
import com.tgcs.posbc.bridge.eft.wrapper.exception.ReprintNeededException;
import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;
import com.tgcs.spring.framework.Priority;
import com.tgcs.tgcp.bridge.adapter.*;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.VoidItemException;
import com.tgcs.tgcp.bridge.print.PrinterHandler;
import com.tgcs.tgcp.bridge.print.ReceiptBuilder;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder.*;
import com.tgcs.tgcp.cash.management.api.client.impl.CashManagementClientImpl;
import com.tgcs.tgcp.pos.api.service.PosService;
import com.tgcs.tgcp.pos.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.HttpClientErrorException;

import java.util.function.Consumer;

public class NgpServiceHandler implements IWebService {

    private static final Logger logger = LoggerFactory.getLogger(NgpServiceHandler.class);

    @Autowired
    ConfProperties properties;

    @Autowired
    @Qualifier(PosService.BEAN_NAME)
    @Priority(Priority.MAX) // highest non-cache priority
    protected PosService posService;

    @Autowired
    @Qualifier(CashManagementClientImpl.BEAN_NAME)
    private CashManagementClientImpl cashManagementService;

    @Autowired
    private NgpSessionManager ngpSessionManager;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EftWrapperClient eftWrapperClient;

    @Autowired(required = false)
    private PrinterHandler printerHandler;

    private NgpSession ngpSession;

    private static final String MSG_SESSION_NOT_INITIALIZED = "Session not initialized";


    private void openTill() throws Exception {
        // TODO We need to discuss how to handle tills:
        // Should we close and open every time?
        // Should we query status and do like NGP? Close if day has changed and reopen?

        try {
            ngpSessionManager.execute(() -> cashManagementService.openTill(
                    CashManagementClientImpl.openTillBuilder.create(
                            TillBuilder.buildOpenTillRequest(properties))));
            logger.info("Till successfully opened");
        } catch (WsErrorResponse e) {
            if (!e.getWsMessage().contains("A primary till is already assigned to the endpoint in context")) {
                throw e;
            }
            logger.warn("Till already open");
        }
        // TODO Currency code retrieve from till, if not present hardcoded to Euro.
        //  We will need to handle it after we understand the fiscal implications
        //          String currency = response.getBody().getContents().stream().reduce((first, second) -> second)
        //                 .orElse(new TillContentEntry().setCurrencyCode("EUR")).getCurrencyCode();
        //       ngpSession.setCurrencyCode(currency);


        ngpSession.setCurrencyCode("EUR");

    }

    @Override
    public void initializeWebService() throws Exception {
        logger.info("initialize Web Service");
        ngpSession = context.getBean(NgpSession.class);

        if (properties.getPosType().contains("tpnet")) {
            try {
                eftWrapperClient.open(null);
                posService.tpnetSignOn(PosService.tpnetSignOnBuilder
                        .create(LoginRequestBuilder.buildTPNET(properties)));
            } catch (Exception e) {
                logger.error("Error occurred during Sign On operation on TP.NET");
                throw e;
            }
        }

        if (properties.isCashManagementEnabled()) {
            // In order to be able to create orders on NGP, a till needs to be open
            openTill();
        }
    }

    @Override
    public OrderAdapter addItem(OrderItem orderItem) throws Exception {
        logger.info("add Item");
        if (ngpSession.getNgpOrderResponse() == null) {
            ngpSession.setNgpOrderResponse(ngpSessionManager.execute(() ->
                    posService.addBarcodeCreateOrder(PosService.addBarcodeCreateOrderBuilder
                            .create(BarcodeRequestBuilder.build(orderItem, properties)))));
        } else {
            ngpSession.setNgpOrderResponse(ngpSessionManager.execute(() ->
                    posService.addBarcodeToOrder(PosService.addBarcodeToOrderBuilder
                            .create(ngpSession.getNgpOrderResponse().getId(), ngpSession.getNgpOrderResponse().getVersion(),
                                    BarcodeRequestBuilder.build(orderItem, properties)))));
        }
        logger.debug("OrderResponse (addItem): " + ngpSession.getNgpOrderResponse());
        return NgpToChecConvertor.convertNgpOrderToChecOrder(ngpSession.getNgpOrderResponse());

    }

    @Override
    public OrderAdapter addCoupon(OrderCoupon orderCoupon) throws Exception {

        if (ngpSession == null) throw new Exception(MSG_SESSION_NOT_INITIALIZED);
        try {
 //           addCouponRequestWrapper.buildRequest(ngpSession, orderCoupon);
  //          ResponseEntity<OrderResponse> orderResponse = addCouponRequestWrapper.sendRequest(OrderResponse.class);
   //         ngpSession.setOrderResponse(orderResponse.getBody());
            logger.info("Coupon added to order");
        } catch (HttpClientErrorException ee) {
            logger.error("Coupon could not be added to the order: " + ee.getResponseBodyAsString(), ee);
            throw ee;
        } catch (Exception e) {
            logger.error("Error sending item to Service", e);
            throw e;
        }
        return NgpToChecConvertor.convertNgpOrderToChecOrder(ngpSession.getNgpOrderResponse());
    }

    @Override
    public OrderAdapter addCustomer(OrderCustomer orderCustomer) throws Exception {
        logger.info("add Customer");
        if (ngpSession.getNgpOrderResponse() == null) {
            ngpSession.setNgpOrderResponse(ngpSessionManager.execute(() ->
                    posService.addCustomerCreateOrder(PosService.addCustomerCreateOrderBuilder
                            .create(AddCustomerRequestBuilder.build(orderCustomer, properties)))));
        } else {
            ngpSession.setNgpOrderResponse(ngpSessionManager.execute(() ->
                    posService.addCustomerToOrder(PosService.addCustomerToOrderBuilder
                            .create(ngpSession.getNgpOrderResponse().getId(), ngpSession.getNgpOrderResponse().getVersion(),
                                    AddCustomerRequestBuilder.build(orderCustomer, properties)))));
        }

        return NgpToChecConvertor.convertNgpOrderToChecOrder(ngpSession.getNgpOrderResponse());
    }

    @Override
    public OrderAdapter addPayment(PaymentInfo paymentDetails) throws Exception {
        if (ngpSession == null) throw new Exception(MSG_SESSION_NOT_INITIALIZED);

        if (paymentDetails.getPaymentTypeGroup() == PaymentInfo.PaymentTypeGroup.DEBIT) {
            processEftDebitPayment(paymentDetails);
        }

        ngpSession.setNgpOrderResponse(ngpSessionManager.execute(() ->
                posService.addPaymentToOrder(PosService.addPaymentToOrderBuilder
                        .create(ngpSession.getNgpOrderResponse().getId(), ngpSession.getNgpOrderResponse().getVersion(),
                                AddPaymentRequestBuilder.build(paymentDetails, properties, ngpSession.getCurrencyCode())
                        ))));

        logger.debug("OrderResponse (addPayment): " + ngpSession.getNgpOrderResponse());
        return NgpToChecConvertor.convertNgpOrderToChecOrder(ngpSession.getNgpOrderResponse());
    }

    private void processEftDebitPayment(PaymentInfo paymentDetails) {
        if (properties.getPosType().contains("tpnet")) {
            Consumer<EftContext> debitPaymentCall = ctx -> eftWrapperClient.debitPayment(ctx, paymentDetails.getAmount());

            EftContext ctx = buildEftContext();
            try {
                debitPaymentCall.accept(ctx);

            } catch (ReprintNeededException e) {
                eftWrapperClient.reprint(ctx);
                debitPaymentCall.accept(ctx);
            }
        }
    }

    @Override
    public boolean finishTransaction() {
        // EN: not sure about this return true here, but I'll leave it for the time being
        if (ngpSession == null || ngpSession.getNgpOrderResponse() == null) return false;

        try {
            ngpSession.setNgpOrderResponse(ngpSessionManager.execute(() ->
                    posService.completeOrder(PosService.completeOrderBuilder
                            .create(ngpSession.getNgpOrderResponse().getId(), ngpSession.getNgpOrderResponse().getVersion(),
                                    CompleteOrderRequestBuilder.build(properties)
                            ))));

            logger.info("Transaction completed: " + ngpSession.getNgpOrderResponse().getId());
            logger.debug("OrderResponse (finishTransaction): " + ngpSession.getNgpOrderResponse());

            ngpSession.setNgpOrderResponse(ngpSessionManager.execute(() ->
                    posService.submitOrder(PosService.submitOrderBuilder
                            .create(ngpSession.getNgpOrderResponse().getId(), ngpSession.getNgpOrderResponse().getVersion(),
                                    SubmitOrderRequestBuilder.build(properties)
                            ))));

            ngpSession.setReceipt(ReceiptBuilder.buildReceipt(ngpSession.getNgpOrderResponse()));

            logger.info("Transaction submitted: " + ngpSession.getNgpOrderResponse().getId());
            logger.debug("OrderResponse (finishTransaction): " + ngpSession.getNgpOrderResponse());

        } catch (Exception e) {
            logger.error("Error finishing the transaction: ", e);
            return false;
        }

        //EN: resetting the session object
        ngpSession.setOrderResponse(null);
        ngpSession.setNgpOrderResponse(null);

        return true;
    }

    @Override
    public void signOff() {
        if (properties.getPosType().contains("tpnet")) {
            try {
                posService.tpnetSignOff(PosService.tpnetSignOffBuilder
                        .create(LoginRequestBuilder.buildTPNET(properties)));

                eftWrapperClient.close(buildEftContext());

            } catch (Exception e) {
                logger.error("Error occurred during Sign Off operation on TP.NET");
                throw e;
            }
        }
    }

    @Override
    public void suspendTransaction() throws Exception {
        Order ngpOrderResponse = ngpSessionManager.execute(() ->
                posService.suspendOrder(PosService.suspendOrderBuilder
                        .create(ngpSession.getNgpOrderResponse().getId(), ngpSession.getNgpOrderResponse().getVersion(),
                                SuspendOrderRequestBuilder.build(properties))));
        logger.debug("OrderResponse (suspendTransaction): " + ngpOrderResponse);

        ngpSession.setReceipt(ReceiptBuilder.buildReceipt(ngpOrderResponse));

        ngpSession.setOrderResponse(null);
        ngpSession.setNgpOrderResponse(null);

        logger.debug("Transaction suspended");
    }

    @Override
    public void voidTransaction() throws Exception {
        Order ngpOrderResponse = ngpSessionManager.execute(() ->
                posService.voidOrder(PosService.voidOrderBuilder
                        .create(ngpSession.getNgpOrderResponse().getId(), ngpSession.getNgpOrderResponse().getVersion(),
                                VoidOrderRequestBuilder.build(properties))));
        logger.debug("OrderResponse (voidTransaction): " + ngpOrderResponse);

        ngpSession.setReceipt(ReceiptBuilder.buildReceipt(ngpOrderResponse));

        ngpSession.setOrderResponse(null);
        ngpSession.setNgpOrderResponse(null);
    }

    @Override
    public void releaseResources() {
        ngpSession = null;
    }


    private EftContext buildEftContext() {
        EftContext eftContext = new EftContext();
        // EN: we return empty context, this is for open and close scenarios.
        // It seems that Marcel's wrapper works even without requests for these scenarios,
        // but we only tested with the simulator endpoint, so I cannot be sure.
        if (ngpSession == null || ngpSession.getNgpOrderResponse() == null) return eftContext;

        eftContext.setCasherId(properties.getUsername());
        // EN: EndpointId (till number in the wrapper) must be an integer... We will pass the username again.
        // eftContext.setEndpointId(properties.getEndpointId());
        eftContext.setEndpointId(properties.getUsername());
        // EN: RequestId for the Wrapper is the txn number, that is stored in the OrderNumber field
        eftContext.setRequestId(ngpSession.getNgpOrderResponse().getOrderNumber());
        return eftContext;
    }

    @Override
    public OrderAdapter voidItem(OrderItem orderItem) throws Exception {
        com.tgcs.tgcp.pos.model.OrderItem itemToVoid = ngpSession.getNgpOrderResponse().getItems().stream()
                .filter(e -> e.getItemLabelData().equalsIgnoreCase(orderItem.getItemIdentifier()))
                .findFirst()
                .orElseThrow(() -> new VoidItemException(ErrorCode.VOID_ITEM_NOT_FOUND.name(), ErrorCode.VOID_ITEM_NOT_FOUND.getValue()));

        Order order = ngpSessionManager.execute(() -> posService.voidItemFromOrder(
                VoidItemRequestBuilder.createBuilder(ngpSession.getNgpOrderResponse(), itemToVoid, properties)));

        ngpSession.setNgpOrderResponse(order);

        logger.debug("OrderResponse (void item): " + ngpSession.getNgpOrderResponse());
        return NgpToChecConvertor.convertNgpOrderToChecOrder(ngpSession.getNgpOrderResponse());
    }

    public void printReceipt() throws Exception {
        if (ngpSession.getReceipt() == null || !properties.isPosHandlePrinter()) {
            throw new Exception("No transaction receipts were available.");
        }
        printerHandler.printReceipt(ngpSession.getReceipt());
    }
}
