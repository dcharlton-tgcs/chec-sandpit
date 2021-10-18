package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

public class SubmitOrderRequest extends WebRequestBody {

    private Context context;

    public Context getContext() {
        return context;
    }

    public SubmitOrderRequest setContext(Context context) {
        this.context = context;
        return this;
    }
}
