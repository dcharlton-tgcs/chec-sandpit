package com.tgcs.tgcp.bridge.checoperations.model.close;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:SignOffResponse")
public class SignOffResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "SignOffResult")
    private SignOffResult signOffResult;

    public SignOffResponse() {
        signOffResult = new SignOffResult();
    }

    public SignOffResponse(String requestId) {
        this.signOffResult = new SignOffResult(requestId);
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }

    public SignOffResult getSignOffResult() {
        return signOffResult;
    }

    public SignOffResponse setSignOffResult(SignOffResult signOffResult) {
        this.signOffResult = signOffResult;
        return this;
    }
}


