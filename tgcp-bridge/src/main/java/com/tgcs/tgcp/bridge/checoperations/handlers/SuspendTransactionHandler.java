package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend.SuspendResponse;
import com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend.SuspendTransactionResult;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SuspendTransactionHandler implements IHandler, ChecMessagePath {
    private static Logger logger = LoggerFactory.getLogger(SuspendTransactionHandler.class);

    private BridgeSession bridgeSession;

    @Autowired
    private ApplicationContext context;

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getSilentNodeTextContent(SUSPEND_TRANSACTION_REQUEST_ID);

        if(!bridgeSession.inTransaction()){
            client.sendResponseToChecApp(createExceptionSuspendTransactionResponse(new ChecOperationException(SuspendResponse.SuspendErrorCode.APPLICATION_NOT_IN_PROPER_STATE.getValue(), "Not in a transaction", null), requestId));
            return;
        }

        try {
            suspendTransaction();
        } catch (ChecOperationException e) {
            client.sendResponseToChecApp(createExceptionSuspendTransactionResponse(e, requestId));
            return;
        }

        // Send Suspend Receipt Event
        client.sendResponseToChecApp(Ngp2ChecMapper.createSuspendPOSReceiptEvent(requestId));

        // Send transaction suspend status event
        client.sendResponseToChecApp(Ngp2ChecMapper.createTransactionStatusEvent(bridgeSession.getOrderAdapter(), requestId, TransactionStatusType.TRANSACTION_SUSPENDED.name()));

        // Send suspend transaction response
        client.sendResponseToChecApp(new SuspendResponse(requestId));

        bridgeSession.clearOrder();
    }

    private SuspendResponse createExceptionSuspendTransactionResponse(ChecOperationException e, String requestId) {
        return new SuspendResponse()
                .setSuspendTransactionResult(new SuspendTransactionResult().setRequestId(requestId)
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }


    private void suspendTransaction() throws ChecOperationException {
        IWebService webService = context.getBean(IWebService.class);
        try {
            webService.suspendTransaction();
        } catch (Exception e) {
            logger.error("Failed to suspend transaction");
            //TODO handle suspend transaction exception
            String errorCode = "";
            String errorMessage = e.getMessage();
            throw new ChecOperationException(errorCode, errorMessage, e);
        }
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }
}
