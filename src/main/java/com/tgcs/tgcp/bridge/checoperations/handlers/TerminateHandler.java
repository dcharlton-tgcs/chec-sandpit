package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.close.TerminateResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TerminateHandler implements IHandler {
    private static Logger logger = LoggerFactory.getLogger(TerminateHandler.class);
    private BridgeSession bridgeSession = new BridgeSession();

    @Autowired
    private IWebService webService;

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getSilentNodeTextContent(ChecMessagePath.TERMINATE_REQUEST_ID);
        voidCurrentTransactionIfAsked(message);
        client.sendResponseToChecApp(new TerminateResponse(requestId));
        webService.releaseResources();
    }

    public void voidCurrentTransactionIfAsked(XmlDocument message) {
        if (!bridgeSession.inTransaction()) return;

        String voidTransaction = message.getOrDefaultNodeTextContent(ChecMessagePath.TERMINATE_VOID, "");
        if (Boolean.parseBoolean(voidTransaction)) {
            voidTransaction();
        }
    }

    private void voidTransaction() {
        try {
            logger.info("Transaction pending during terminate, voiding...");
            webService.voidTransaction();
            logger.info("Transaction voided");
        } catch (Exception e) {
            logger.error("Failed to void transaction during terminate");
        }
    }

}
