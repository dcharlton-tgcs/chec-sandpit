package com.tgcs.tgcp.bridge.checoperations;

import com.tgcs.tgcp.bridge.checoperations.handlers.IHandler;
import com.tgcs.tgcp.bridge.checoperations.handlers.InitializationHandler;
import com.tgcs.tgcp.bridge.checoperations.handlers.NgpHandlerFactory;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class OperationsController {
    private static final Logger logger = LoggerFactory.getLogger(OperationsController.class);

    private BridgeSession bridgeSession;

    @Autowired
    NgpHandlerFactory handlerFactory;

    @Autowired
    ApplicationContext context;

    public void handleMessage(TcpClient tcpClient, XmlDocument message) {
        IHandler operationHandler = handlerFactory.getHandler(message);

        if (operationHandler instanceof InitializationHandler) {
            bridgeSession = context.getBean(BridgeSession.class);
        }

        operationHandler.setBridgeSession(bridgeSession);

        try {
            operationHandler.processChecMessage(tcpClient, message);
        } catch (Exception e) {
            logger.error("Exception raised during processing operation ", e);
            //FIXME send response to chec for every request type to avoid hanging!!!!!!!
            //FIXME to be handled inside the handler -- a new method needs to be added into the interface
            // to force the implementation!!!!!!!
        }
    }


}
