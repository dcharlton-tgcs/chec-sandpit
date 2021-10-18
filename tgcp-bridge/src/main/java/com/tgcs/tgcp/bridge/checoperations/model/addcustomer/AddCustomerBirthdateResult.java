package com.tgcs.tgcp.bridge.checoperations.model.addcustomer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddCustomerBirthdateResult")
public class AddCustomerBirthdateResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    public AddCustomerBirthdateResult() {
    }

    public AddCustomerBirthdateResult(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public AddCustomerBirthdateResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
