package com.tgcs.tgcp.bridge.checoperations.model.addcustomer;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:AddCustomerResponse")
public class AddCustomerResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "AddCustomerResult")
    private AddCustomerResult addCustomerResult;

    public AddCustomerResult getAddCustomerResult() {
        return addCustomerResult;
    }

    public AddCustomerResponse setAddCustomerResult(AddCustomerResult addCustomerResult) {
        this.addCustomerResult = addCustomerResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}
