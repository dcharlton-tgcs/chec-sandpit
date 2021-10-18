package com.tgcs.tgcp.bridge.checoperations.model.additem;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:AddItemResponse")
public class AddItemResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "AddItemResult")
    private AddItemResult addItemResult;

    public AddItemResponse() {    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }

    public AddItemResult getAddItemResult() {
        return addItemResult;
    }

    public AddItemResponse setAddItemResult(AddItemResult addItemResult) {
        this.addItemResult = addItemResult;
        return this;
    }
}
