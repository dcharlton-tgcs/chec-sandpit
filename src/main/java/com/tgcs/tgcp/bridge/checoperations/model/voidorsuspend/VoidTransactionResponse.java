package com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:VoidTransactionResponse")
public class VoidTransactionResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "VoidTransactionResult")
    private VoidTransactionResult voidTransactionResult;

    public VoidTransactionResponse() {
    }

    public VoidTransactionResponse(String requestId) {
        this.voidTransactionResult = new VoidTransactionResult(requestId);
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }

    public VoidTransactionResult getVoidTransactionResult() {
        return voidTransactionResult;
    }

    public VoidTransactionResponse setVoidTransactionResult(VoidTransactionResult voidTransactionResult) {
        this.voidTransactionResult = voidTransactionResult;
        return this;
    }
}
