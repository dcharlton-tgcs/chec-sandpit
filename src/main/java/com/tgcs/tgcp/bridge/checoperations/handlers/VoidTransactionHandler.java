package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend.VoidTransactionResponse;
import com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend.VoidTransactionResult;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class VoidTransactionHandler implements IHandler, ChecMessagePath {
    private static Logger logger = LoggerFactory.getLogger(VoidTransactionHandler.class);

    private BridgeSession bridgeSession = new BridgeSession();

    @Autowired
    private ApplicationContext context;

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getOrDefaultNodeTextContent(VOID_TRANSACTION_REQUEST_ID, "");

        try {
            sendVoidTransactionRequest();
        } catch (ChecOperationException e) {
            client.sendResponseToChecApp(createExceptionVoidTransactionResponse(e, requestId));
            return;
        }

        // Send transaction end status event
        client.sendResponseToChecApp(Ngp2ChecMapper.createTransactionStatusEvent(bridgeSession.getOrderAdapter(), requestId, TransactionStatusType.TRANSACTION_VOID.name()));

        // Send POS Receipt Item Event
        client.sendResponseToChecApp(Ngp2ChecMapper.createVoidPOSReceiptEvent(requestId));

        // Send void transaction response
        client.sendResponseToChecApp(new VoidTransactionResponse(requestId));

        bridgeSession.clearOrder();
    }

    private VoidTransactionResponse createExceptionVoidTransactionResponse(ChecOperationException e, String requestId) {
        return new VoidTransactionResponse()
                .setVoidTransactionResult(new VoidTransactionResult().setRequestId(requestId)
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }


    private void sendVoidTransactionRequest() throws ChecOperationException {
        IWebService webService = context.getBean(IWebService.class);
        try {
            webService.voidTransaction();
        } catch (Exception e) {
            logger.error("Failed to void transaction");
            //TODO handle void transaction exception
            String errorCode = "";
            String errorMessage = "";
            throw new ChecOperationException(errorCode, errorMessage, e);
        }
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }
}
