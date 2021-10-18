package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.addcustomer.AddCustomerBirthdateResponse;
import com.tgcs.tgcp.bridge.checoperations.model.addcustomer.AddCustomerBirthdateResult;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.springframework.stereotype.Component;

@Component
public class AddCustomerBirthdateHandler implements IHandler, ChecMessagePath {

    private BridgeSession bridgeSession;

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getOrDefaultNodeTextContent(ADD_CUSTOMER_BIRTHDATE_REQUEST_ID, "");
        client.sendResponseToChecApp(new AddCustomerBirthdateResponse(new AddCustomerBirthdateResult(requestId)));
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }
}
