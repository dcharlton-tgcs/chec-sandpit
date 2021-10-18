package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.AddReceiptLines;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.AddReceiptLinesResponse;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.AddReceiptLinesResult;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.FormattedReceiptLine;
import com.tgcs.tgcp.bridge.common.ObjectToXmlConverter;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddReceiptLinesHandler implements IHandler, ChecMessagePath {

    private BridgeSession bridgeSession;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getOrDefaultNodeTextContent(ADD_RECEIPT_LINES_REQUEST_ID, "");

        try {
            FormattedReceiptLine formattedReceiptLine = ObjectToXmlConverter.jaxbXMLToObject(message, AddReceiptLines.class)
                    .getAddReceiptLinesRequest().getFormattedReceiptLine();
            client.sendResponseToChecApp(Ngp2ChecMapper.createPOSReceiptEventAddReceiptLines(requestId, formattedReceiptLine));
        } catch (Exception e) {
            ChecOperationException ce = new ChecOperationException(e.getLocalizedMessage(), e.getMessage(), e);
            client.sendResponseToChecApp(createExceptionAddReceiptLinesResponse(ce, requestId));
            return;
        }
        client.sendResponseToChecApp(new AddReceiptLinesResponse(new AddReceiptLinesResult(requestId)));
    }

    private AddReceiptLinesResponse createExceptionAddReceiptLinesResponse(ChecOperationException e, String requestId) {
        return new AddReceiptLinesResponse()
                .setAddReceiptLinesResult(new AddReceiptLinesResult().setRequestId(requestId)
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }

    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

}