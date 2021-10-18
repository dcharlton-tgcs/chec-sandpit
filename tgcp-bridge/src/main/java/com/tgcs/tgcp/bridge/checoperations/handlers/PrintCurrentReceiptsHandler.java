package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.PrintCurrentReceiptsResponse;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.PrintCurrentReceiptsResult;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrintCurrentReceiptsHandler implements IHandler, ChecMessagePath {
    private static Logger logger = LoggerFactory.getLogger(PrintCurrentReceiptsHandler.class);

    private BridgeSession bridgeSession;

    @Autowired
    private IWebService webService;

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getOrDefaultNodeTextContent(PRINT_CURRENT_RECEIPTS_REQUEST_ID, "");

        try {
            printCurrentReceipt();
        } catch (ChecOperationException exception) {
            logger.error(exception.getErrorMessage());
            client.sendResponseToChecApp(createExceptionPrintCurrentReceiptsResponse(exception, requestId));
            return;
        }
        client.sendResponseToChecApp(createPrintCurrentReceiptsResponse(requestId));
    }

    void printCurrentReceipt() throws ChecOperationException {
        try {
            webService.printReceipt();
        } catch (Exception e) {
            throw new ChecOperationException(ErrorCode.TRANSACTION_RECEIPTS_NOT_AVAILABLE.name(), e.getMessage(), null);
        }
    }

    PrintCurrentReceiptsResponse createPrintCurrentReceiptsResponse(String requestId) {
        return new PrintCurrentReceiptsResponse(new PrintCurrentReceiptsResult()
                .setRequestId(requestId));
    }

    PrintCurrentReceiptsResponse createExceptionPrintCurrentReceiptsResponse(ChecOperationException exception, String requestId) {
        return new PrintCurrentReceiptsResponse(new PrintCurrentReceiptsResult()
                .setRequestId(requestId)
                .setExceptionResult(new ExceptionResult()
                        .setErrorCode(exception.getErrorCode())
                        .setMessage(exception.getErrorMessage())));
    }

}
