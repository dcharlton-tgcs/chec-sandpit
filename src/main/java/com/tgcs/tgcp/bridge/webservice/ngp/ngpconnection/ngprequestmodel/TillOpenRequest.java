package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

public class TillOpenRequest extends WebRequestBody {

    private Context context;

    private String type;

    private Boolean primary;

    private String operatorId;

    public Context getContext() {
        return context;
    }

    public TillOpenRequest setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getType() {
        return type;
    }

    public TillOpenRequest setType(String type) {
        this.type = type;
        return this;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public TillOpenRequest setPrimary(Boolean primary) {
        this.primary = primary;
        return this;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public TillOpenRequest setOperatorId(String operatorId) {
        this.operatorId = operatorId;
        return this;
    }
}
