package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCVersion;
import com.tgcs.tgcp.bridge.checoperations.model.status.QueryStatusResponse;
import com.tgcs.tgcp.bridge.checoperations.model.status.QueryStatusResult;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class QueryStatusHandler implements IHandler, ChecMessagePath {
    private BridgeSession bridgeSession;

    @Autowired
    QueryStatusResponse queryStatusResponse;

    @Autowired
    ApplicationContext context;

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }


    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        String requestId = message.getOrDefaultNodeTextContent(QUERY_STATUS_REQUEST_ID, "");

        //Send status response
        client.sendResponseToChecApp(createStatusResponse(bridgeSession, requestId));
    }

    private QueryStatusResponse createStatusResponse(BridgeSession bridgeSession, String requestId) {
        QueryStatusResult status = new QueryStatusResult();

        if (requestId.length() > 0) {
            status.setRequestId(requestId);
            status.setLastSuccessfulRequestID(requestId);
        }

        status.setPosbcStatus(this.bridgeSession.getNGPStatus())
                .setCurrentDateAndTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy hh:mm:ss")))
                .setInTransaction(bridgeSession.getOrderAdapter() != null)
                .setPosbcVersion(new POSBCVersion()  // TODO grab the actual values from bridge itself or the POM
                        .setVersion("0")
                        .setRelease("1")
                        .setMaintenanceLevel("0")
                        .setDescription("EDC Posbc Bridge"));
        QueryStatusResponse queryStatusResponse = context.getBean(QueryStatusResponse.class);
        return queryStatusResponse.setQueryStatusResult(status);
    }


}
