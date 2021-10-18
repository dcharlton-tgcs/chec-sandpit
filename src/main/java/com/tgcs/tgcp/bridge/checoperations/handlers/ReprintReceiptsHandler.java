package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.ReprintReceiptsResponse;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.ReprintReceiptsResult;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReprintReceiptsHandler implements IHandler, ChecMessagePath {

    private static Logger logger = LoggerFactory.getLogger(ReprintReceiptsHandler.class);
    private BridgeSession bridgeSession;

    @Autowired
    private IWebService webService;

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getOrDefaultNodeTextContent(REPRINT_RECEIPTS_REQUEST_ID, "");

        try {
            printReceipt();
        } catch (ChecOperationException exception) {
            logger.error(exception.getErrorMessage());
            client.sendResponseToChecApp(createExceptionReprintReceiptsResponse(exception, requestId));
            return;
        }
        // Send reprint receipts  response
        client.sendResponseToChecApp(createReprintReceiptsResponse(requestId));
    }

    void printReceipt() throws ChecOperationException {
        try {
            webService.printReceipt();
        } catch (Exception e) {
            throw new ChecOperationException(ErrorCode.TRANSACTION_RECEIPTS_NOT_AVAILABLE.name(), e.getMessage(), null);
        }
    }

    ReprintReceiptsResponse createReprintReceiptsResponse(String requestId) {
        return new ReprintReceiptsResponse(new ReprintReceiptsResult()
                .setRequestId(requestId));
    }

    ReprintReceiptsResponse createExceptionReprintReceiptsResponse(ChecOperationException exception, String requestId) {
        return new ReprintReceiptsResponse(new ReprintReceiptsResult()
                .setRequestId(requestId)
                .setExceptionResult(new ExceptionResult()
                        .setErrorCode(exception.getErrorCode())
                        .setMessage(exception.getErrorMessage())));
    }
}