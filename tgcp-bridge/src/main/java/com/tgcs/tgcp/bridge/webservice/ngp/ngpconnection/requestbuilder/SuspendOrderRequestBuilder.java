package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.SuspendOrderRequest;

public class SuspendOrderRequestBuilder {

    public static SuspendOrderRequest build(ConfProperties properties) {
        return new SuspendOrderRequest().context(ContextBuilder.build(properties));
    }
}
