package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.close.SignOffResponse;
import com.tgcs.tgcp.bridge.checoperations.model.close.SignOffResult;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.print.PrinterHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignOffHandler implements IHandler {
    private static Logger logger = LoggerFactory.getLogger(InitializationHandler.class);
    private BridgeSession bridgeSession = new BridgeSession();

    @Autowired
    private IWebService webService;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Autowired(required = false)
    PrinterHandler printerHandler;

    @Autowired
    ConfProperties properties;

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getSilentNodeTextContent(ChecMessagePath.SIGN_OFF_REQUEST_ID);

        try {
            webService.signOff();
        } catch (Exception e) {
            // FIXME Handle Exception - Unauthorized or User has no roles, send failure to chec client.sendResponseToChecApp(xmlResponse);
            logger.error("Failed to initialize web service", e);
            ChecOperationException ex = exceptionHandler.retrieveWebServiceError(e);
            client.sendResponseToChecApp(createExceptionSignOffResponse(ex, requestId));
            return;
        }

        client.sendResponseToChecApp(new SignOffResponse(requestId));
        closeDevices();
        webService.releaseResources();
    }

    protected SignOffResponse createExceptionSignOffResponse(ChecOperationException ex, String requestId) {

        return new SignOffResponse(requestId)
                .setSignOffResult(new SignOffResult(requestId)
                        .setExceptionResult(new ExceptionResult()
                        .setErrorCode(ex.getErrorCode())
                        .setMessage(ex.getErrorMessage())));
    }

    protected void closeDevices(){
        closePrinter();
    }

    protected void closePrinter(){
        if (properties.isPosHandlePrinter()) {
            printerHandler.close();
        }
    }

}
