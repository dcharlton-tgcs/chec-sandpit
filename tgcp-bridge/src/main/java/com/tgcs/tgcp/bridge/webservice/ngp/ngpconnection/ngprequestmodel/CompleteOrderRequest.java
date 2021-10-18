package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

public class CompleteOrderRequest extends WebRequestBody {

    private Context context;

    public Context getContext() {
        return context;
    }

    public CompleteOrderRequest setContext(Context context) {
        this.context = context;
        return this;
    }
}
