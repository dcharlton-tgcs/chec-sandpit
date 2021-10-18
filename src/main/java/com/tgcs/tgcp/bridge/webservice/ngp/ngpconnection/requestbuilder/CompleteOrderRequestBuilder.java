package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.CompleteOrderRequest;

public class CompleteOrderRequestBuilder {

    public static CompleteOrderRequest build(ConfProperties properties)
    {
        return new CompleteOrderRequest().context(ContextBuilder.build(properties));
    }
}
