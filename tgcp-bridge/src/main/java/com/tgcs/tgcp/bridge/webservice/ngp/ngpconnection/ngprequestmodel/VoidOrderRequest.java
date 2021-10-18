package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import org.springframework.stereotype.Component;

@Component
public class VoidOrderRequest extends WebRequestBody {

    private Context context;

    public Context getContext() {
        return context;
    }

    public VoidOrderRequest setContext(Context context) {
        this.context = context;
        return this;
    }
}
