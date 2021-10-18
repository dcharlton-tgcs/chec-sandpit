package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.unsupportedoperation.UnsupportedOperationResponse;
import com.tgcs.tgcp.bridge.checoperations.model.unsupportedoperation.UnsupportedOperationResult;
import com.tgcs.tgcp.bridge.common.ObjectToXmlConverter;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.springframework.stereotype.Component;

@Component
public class UnsupportedOperationHandler implements IHandler {

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String rootNodeName = message.getRootNodeName();
        String requestId = message.getOrDefaultNodeTextContent(
                ChecMessagePath.UNKNOWN_REQUEST_ID.replaceAll("\\{.*?}", rootNodeName), "");

        UnsupportedOperationResponse unsupportedOperationResponse = createExceptionUnsupportedOperationResponse(requestId, rootNodeName);

        client.sendResponseToChecApp(ObjectToXmlConverter.createDynamicNodeName(rootNodeName, unsupportedOperationResponse));
    }

    public UnsupportedOperationResponse createExceptionUnsupportedOperationResponse(String requestId, String rootNodeName) {
        UnsupportedOperationResult unsupportedOperationResult = new UnsupportedOperationResult()
                .setRequestId(requestId)
                .setExceptionResult(new ExceptionResult()
                        .setMessage(rootNodeName + " not implemented")
                        .setErrorCode(ErrorCode.UNSUPPORTED_OPERATION.getValue()));

        return new UnsupportedOperationResponse().setUnsupportedOperationResult(
                ObjectToXmlConverter.createDynamicNodeName(rootNodeName, unsupportedOperationResult)
        );
    }
}
