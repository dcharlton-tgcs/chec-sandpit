package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.cash.management.model.Context;
import com.tgcs.tgcp.cash.management.model.OpenTillRequest;
import com.tgcs.tgcp.cash.management.model.TillType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class TillBuilder {

    public static OpenTillRequest buildOpenTillRequest(ConfProperties properties) {
        return new OpenTillRequest()
                .context(CashManagementContextBuilder.build(properties))
                .type(TillType.SINGLE)
                .primary(true)
                .operatorId("1");//FIXME read from property
    }

}

class CashManagementContextBuilder {

    private Context createContext(ConfProperties properties) {
        return new Context()
                .requestId(UUID.randomUUID().toString())
                .nodeId(properties.getNodeId())
                .endpointId(properties.getEndpointId());
    }

    public static Context build(ConfProperties properties) {
        return new CashManagementContextBuilder().createContext(properties);
    }

}