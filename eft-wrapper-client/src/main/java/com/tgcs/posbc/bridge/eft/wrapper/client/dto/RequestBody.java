package com.tgcs.posbc.bridge.eft.wrapper.client.dto;

import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;

public class RequestBody {

    private final EftContext context;

    public RequestBody(EftContext context) {
        this.context = context;
    }

    public EftContext getContext() {
        return context;
    }
}
