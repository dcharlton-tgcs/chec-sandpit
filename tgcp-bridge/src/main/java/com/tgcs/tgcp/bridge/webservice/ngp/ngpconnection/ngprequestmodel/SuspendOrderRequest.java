package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import org.springframework.stereotype.Component;

@Component
public class SuspendOrderRequest extends WebRequestBody {

    private Context context;

    public Context getContext() {
        return context;
    }

    public SuspendOrderRequest setContext(Context context) {
        this.context = context;
        return this;
    }
}
