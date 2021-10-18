package com.tgcs.tgcp.bridge.checoperations.model.addtender;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:AddTenderResponse")
public class AddTenderResponse implements ResponseType {
    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "AddTenderResult")
    private AddTenderResult AddTenderResult;

    public AddTenderResponse() {  }


    public AddTenderResponse(AddTenderResult addTenderResult) {
        AddTenderResult = addTenderResult;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }

    public com.tgcs.tgcp.bridge.checoperations.model.addtender.AddTenderResult getAddTenderResult() {
        return AddTenderResult;
    }

    public AddTenderResponse setAddTenderResult(com.tgcs.tgcp.bridge.checoperations.model.addtender.AddTenderResult addTenderResult) {
        AddTenderResult = addTenderResult;
        return this;
    }
}
