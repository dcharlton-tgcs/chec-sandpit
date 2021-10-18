package com.tgcs.tgcp.bridge.checoperations.model.init;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:InitializeResponse")
public class InitializeResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "InitializeResult")
    private InitializeResult initializeResult;

    public InitializeResponse() {    }

    public InitializeResponse(String requestId) {
        initializeResult = new InitializeResult(requestId);
    }

    public InitializeResult getInitializeResult() {
        return initializeResult;
    }

    public InitializeResponse setInitializeResult(InitializeResult initializeResult) {
        this.initializeResult = initializeResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}
