package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.init.InitializeResponse;
import com.tgcs.tgcp.bridge.checoperations.model.init.InitializeResult;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.configuration.PrinterProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.print.PrinterHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitializationHandler implements IHandler {
    private static Logger logger = LoggerFactory.getLogger(InitializationHandler.class);

    private BridgeSession bridgeSession;

    @Autowired
    ConfProperties properties;

    @Autowired(required = false)
    PrinterHandler printerHandler;

    @Autowired
    private IWebService webService;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        initDevices(client);

        String requestId = message.getOrDefaultNodeTextContent(ChecMessagePath.INITIALIZE_REQUEST_ID, "");
        client.sendResponseToChecApp(createPOSBCStatusEvent(POSBCStatusType.SEVERITY_INFO, POSBCStatusType.RELEASING_SESSION_RESOURCES));
        client.sendResponseToChecApp(createPOSBCStatusEvent(POSBCStatusType.SEVERITY_INFO, POSBCStatusType.CONNECTING_TO_POS));

        try {
            webService.initializeWebService();
        } catch (Exception e) {
            // FIXME Handle Exception - Unauthorized or User has no roles, send failure to chec client.sendResponseToChecApp(xmlResponse);
            logger.error("Failed to initialize web service", e);
            ChecOperationException ex = exceptionHandler.retrieveWebServiceError(e);
            client.sendResponseToChecApp(createExceptionInitResponse(ex, requestId));
            return;
        }

        client.sendResponseToChecApp(createPOSBCStatusEvent(POSBCStatusType.SEVERITY_INFO, POSBCStatusType.CONNECTED_TO_POS));
        client.sendResponseToChecApp(createPOSBCStatusEvent(POSBCStatusType.SEVERITY_INFO, POSBCStatusType.POS_RESOURCES_INITIALIZED));

        // We have connected with ngp, till was open, send initialization result back to check
        client.sendResponseToChecApp(new InitializeResponse(requestId));

        bridgeSession.clearOrder();
    }

    private POSBCStatusEvent createPOSBCStatusEvent(POSBCStatusType severity, POSBCStatusType status) {
        return POSBCStatusEvent.createPOSBCStatus(severity.getValue(), status.toString(), status.getValue());
    }

    protected InitializeResponse createExceptionInitResponse(ChecOperationException e, String requestId) {
        return new InitializeResponse(requestId)
                .setInitializeResult(new InitializeResult(requestId)
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }

    protected void initDevices(TcpClient connectedSCOClient) {
        initPrinter(connectedSCOClient);

    }

    public void initPrinter(TcpClient connectedSCOClient) {
        if (properties.isPosHandlePrinter()) {
            printerHandler.startListenForPrinterEvents(connectedSCOClient);
        }
    }

}
