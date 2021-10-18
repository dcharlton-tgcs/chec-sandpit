package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.SubmitOrderRequest;

public class SubmitOrderRequestBuilder {

    public static SubmitOrderRequest build(ConfProperties properties)
    {
        SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest();

        submitOrderRequest.setContext(ContextBuilder.build(properties));

        return submitOrderRequest;
    }
}
