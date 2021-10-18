package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.Context;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class ContextBuilder {

    private Context createContext(ConfProperties properties) {
        return new Context()
                .requestId(UUID.randomUUID().toString())
                .nodeId(properties.getNodeId())
                .endpointId(properties.getEndpointId());
    }

    public static Context build(ConfProperties properties) {
        return new ContextBuilder().createContext(properties);
    }

}
