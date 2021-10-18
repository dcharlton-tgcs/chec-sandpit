package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:AddReceiptLinesResponse")
public class AddReceiptLinesResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "AddReceiptLinesResult")
    private AddReceiptLinesResult addReceiptLinesResult;

    public AddReceiptLinesResponse() {
    }

    public AddReceiptLinesResponse(AddReceiptLinesResult addReceiptLinesResult) {
        this.addReceiptLinesResult = addReceiptLinesResult;
    }

    public AddReceiptLinesResult getAddReceiptLinesResult() {
        return addReceiptLinesResult;
    }

    public AddReceiptLinesResponse setAddReceiptLinesResult(AddReceiptLinesResult addReceiptLinesResult) {
        this.addReceiptLinesResult = addReceiptLinesResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}