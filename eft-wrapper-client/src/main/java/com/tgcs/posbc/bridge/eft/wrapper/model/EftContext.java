package com.tgcs.posbc.bridge.eft.wrapper.model;

public class EftContext implements IEftContext {

    private String endpointId;
    private String requestId;
    private String casherId;

    public String getEndpointId() {
        return endpointId;
    }

    public EftContext setEndpointId(String endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public EftContext setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getCasherId() {
        return casherId;
    }

    public EftContext setCasherId(String casherId) {
        this.casherId = casherId;
        return this;
    }
}
