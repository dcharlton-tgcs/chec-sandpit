package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.PaymentInfo;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.addtender.*;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.tgcs.tgcp.bridge.adapter.PaymentInfo.PaymentTypeGroup.*;

@Component
public class AddTenderHandler implements com.tgcs.tgcp.bridge.checoperations.handlers.IHandler, ChecMessagePath {
    private static Logger logger = LoggerFactory.getLogger(AddTenderHandler.class);
    private BridgeSession bridgeSession;

    @Autowired
    private IWebService webService;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public void processChecMessage(TcpClient connectedClient, XmlDocument message) {
        PaymentInfo paymentInfo = createPaymentInfo(message);

        try {
            bridgeSession.setOrderAdapter(sendAddPayment(paymentInfo));

            if(paymentInfo.getPaymentTypeGroup() == OTHER) { // payments from OTHER group might have a different amount applied on the order than the requested amount
                updatePaymentInfoAmount(paymentInfo);
            }
        } catch (ChecOperationException e) {
            connectedClient.sendResponseToChecApp(createExceptionAddTenderResponse(e, paymentInfo.getRequestId()));
            return;
        }

        // Send pos receipt Event
        sendReceiptEvents(connectedClient, paymentInfo);

        // Send total event
        connectedClient.sendResponseToChecApp(Ngp2ChecMapper.createTotalEventFromOrder(bridgeSession.getOrderAdapter(), paymentInfo.getRequestId()));

        boolean transactionFinished = false;
        // If order fully paid, Finish transaction at webservice
        if (bridgeSession.getOrderAdapter().isOrderFullyPaid() && (transactionFinished = webService.finishTransaction())) {
            // Send transaction status end if transaction ended successfully on the service side
            connectedClient.sendResponseToChecApp(Ngp2ChecMapper.createTransactionStatusEvent(bridgeSession.getOrderAdapter(), paymentInfo.getRequestId(),
                    TransactionStatusType.TRANSACTION_END.getValue()));
        }

        // Send add tender response
        connectedClient.sendResponseToChecApp(createAddTenderResponse(paymentInfo));

        if (transactionFinished) bridgeSession.clearOrder();
    }

    private void updatePaymentInfoAmount(PaymentInfo paymentInfo) {
        Optional<PaymentInfo> addedPaymentById = bridgeSession.getAddedPaymentById(paymentInfo.getPaymentId());
        String amount = paymentInfo.getAmount();
        if(addedPaymentById.isPresent()) {
            amount = addedPaymentById.get().getAmount();
        }

        paymentInfo.setAmount(amount);
    }

    private void sendReceiptEvents(TcpClient connectedClient, PaymentInfo paymentInfo) {
        switch (paymentInfo.getPaymentTypeGroup()) {
            case DEBIT:
                connectedClient.sendResponseToChecApp(Ngp2ChecMapper.createDebitTenderPOSReceiptEvent(paymentInfo));
                break;
            case CREDIT:
                break;
            case OTHER:
                connectedClient.sendResponseToChecApp(Ngp2ChecMapper.createMiscTenderPOSReceiptEvent(paymentInfo));
                break;
            case CASH:
            default:
                connectedClient.sendResponseToChecApp(Ngp2ChecMapper.createAddTenderPOSReceiptEvent(bridgeSession, paymentInfo));
                break;
        }
    }

    private OrderAdapter sendAddPayment(PaymentInfo paymentDetails) throws ChecOperationException {
        try {
            return webService.addPayment(paymentDetails);
        } catch (Exception e) {
            logger.error("Failed to Add Tender");
            throw exceptionHandler.retrieveWebServiceError(e);
        }
    }

    private PaymentInfo createPaymentInfo(XmlDocument message) {
        String tenderIdentifier = getPaymentIdentifier(message);
        String tenderIsVoid = message.getOrDefaultNodeTextContent(tenderIdentifier + IS_VOID, "false");

        return new PaymentInfo()
                .setRequestId(message.getOrDefaultNodeTextContent(ADD_TENDER_REQUEST_ID, null))
                .setPaymentId(getPaymentId(message))
                .setAmount(message.getSilentNodeTextContent(tenderIdentifier + AMOUNT))
                .setVoid(Boolean.parseBoolean(tenderIsVoid))
                .setAdditional(message.getKeyValuePairMap(ADD_TENDER_REQUEST_PARAMETER_EXTENSION, KEY_NAME, VALUE_NAME))
                .setPaymentType(getPaymentType(message, tenderIdentifier))
                .setPaymentTypeGroup(getPaymentTypeGroup(tenderIdentifier));
    }

    /**
     * Supplies a {@code String} value to be used as paymentId.
     *
     * Needs to exist because there is no other way to identify BRVCoupon tenders in the response comming back from the {@code pos} application.
     * @param message XML
     * @return XML value from /AddTender/AddTenderRequest/ParameterExtension/KeyValuePair/Value or AddTender/AddTenderRequest/RequestID
     */
    private String getPaymentId(XmlDocument message) {
        if (message.checkPathExists(MISC_IDENTIFIER_TENDER)) {
            return message.getSilentNodeTextContent(ADD_TENDER_REQUEST_PARAMETER_EXTENSION + "/" + KEY_VALUE_PAIR_NAME + "/" + VALUE_NAME);
        }
        return message.getOrDefaultNodeTextContent(ADD_TENDER_REQUEST_ID, null);
    }

    private String getPaymentIdentifier(XmlDocument message) {
        if (message.checkPathExists(DEBIT_IDENTIFIER_TENDER)) return DEBIT_IDENTIFIER_TENDER;
        if (message.checkPathExists(CREDIT_IDENTIFIER_TENDER)) return CREDIT_IDENTIFIER_TENDER;
        if (message.checkPathExists(MISC_IDENTIFIER_TENDER)) return MISC_IDENTIFIER_TENDER;
        return CASH_IDENTIFIER_TENDER;
    }

    private String getPaymentType(XmlDocument message, String tenderIdentifier) {
        switch (tenderIdentifier) {
            case DEBIT_IDENTIFIER_TENDER:
                return PaymentInfo.PaymentType.DEBIT.value;
            case CREDIT_IDENTIFIER_TENDER:
                return PaymentInfo.PaymentType.CREDIT.value;
            case MISC_IDENTIFIER_TENDER:
                return message.getSilentNodeTextContent(tenderIdentifier + DESCRIPTION);
            default:
                return PaymentInfo.PaymentType.CASH.value;
        }
    }

    private PaymentInfo.PaymentTypeGroup getPaymentTypeGroup(String tenderIdentifier) {
        switch (tenderIdentifier) {
            case DEBIT_IDENTIFIER_TENDER:
                return DEBIT;
            case CREDIT_IDENTIFIER_TENDER:
                return CREDIT;
            case MISC_IDENTIFIER_TENDER:
                return OTHER;
            default:
                return CASH;
        }
    }

    private AddTenderResponse createAddTenderResponse(PaymentInfo paymentInfo) {
        AddTenderResult addTenderResult = new AddTenderResult()
                .setRequestId(paymentInfo.getRequestId())
                .setSignatureNeeded(paymentInfo.isSignatureNeeded())
                .setSignatureIndex(paymentInfo.isSignatureIndex());
        switch (paymentInfo.getPaymentTypeGroup()) {
            case CASH:
                addTenderResult.setCashInfo(createCashInfo(paymentInfo));
                break;
            case DEBIT:
                addTenderResult.setDebitInfo(createDebitInfo(paymentInfo));
                break;
            case CREDIT:
                addTenderResult.setCreditInfo(createCreditInfo(paymentInfo));
                break;
            case OTHER:
                addTenderResult.setMiscInfo(createMiscInfo(paymentInfo));
                break;
        }

        return new AddTenderResponse(addTenderResult);
    }

    private CashInfo createCashInfo(PaymentInfo paymentInfo) {
        return new CashInfo()
                .setAmount(paymentInfo.getAmount())
                .setBalanceDueSatisfied(bridgeSession.getOrderAdapter().isOrderFullyPaid())
                .setChange(bridgeSession.getOrderAdapter().getChangeDue())
                .setVoidFlag(paymentInfo.isVoid());
    }

    private CreditInfo createCreditInfo(PaymentInfo paymentInfo) {
        return new CreditInfo();
    }

    private DebitInfo createDebitInfo(PaymentInfo paymentInfo) {
        return new DebitInfo()
                .setAmount(paymentInfo.getAmount())
                .setBalanceDueSatisfied(bridgeSession.getOrderAdapter().isOrderFullyPaid())
                .setChange(bridgeSession.getOrderAdapter().getChangeDue())
                .setVoidFlag(paymentInfo.isVoid())
                .setMaskedAccountNumber(paymentInfo.getCardDetails().getDisplayAccountNumber());
    }

    private MiscInfo createMiscInfo(PaymentInfo paymentInfo) {
        return new MiscInfo()
                .setDescription(paymentInfo.getPaymentType())
                .setAmount(paymentInfo.getAmount())
                .setBalanceDueSatisfied(bridgeSession.getOrderAdapter().isOrderFullyPaid())
                .setVoidFlag(paymentInfo.isVoid());
    }

    private AddTenderResponse createExceptionAddTenderResponse(ChecOperationException e, String requestId) {
        return new AddTenderResponse()
                .setAddTenderResult(new AddTenderResult()
                        .setRequestId(requestId)
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }


}
