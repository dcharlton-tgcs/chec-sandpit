package com.tgcs.tgcp.bridge.checoperations.model.addcustomer;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:AddCustomerBirthdateResponse")
public class AddCustomerBirthdateResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "AddCustomerBirthdateResult")
    private AddCustomerBirthdateResult addCustomerBirthdateResult;

    public AddCustomerBirthdateResponse() {
    }

    public AddCustomerBirthdateResponse(AddCustomerBirthdateResult addCustomerBirthdateResult) {
        this.addCustomerBirthdateResult = addCustomerBirthdateResult;
    }

    public AddCustomerBirthdateResult getAddCustomerBirthdateResult() {
        return addCustomerBirthdateResult;
    }

    public AddCustomerBirthdateResponse setAddCustomerBirthdateResult(AddCustomerBirthdateResult addCustomerBirthdateResult) {
        this.addCustomerBirthdateResult = addCustomerBirthdateResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}
