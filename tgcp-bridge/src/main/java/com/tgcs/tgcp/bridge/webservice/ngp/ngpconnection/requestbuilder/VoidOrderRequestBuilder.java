package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.VoidOrderRequest;

public class VoidOrderRequestBuilder {

    public static VoidOrderRequest build(ConfProperties properties) {
        return new VoidOrderRequest().context(ContextBuilder.build(properties));
    }
}
