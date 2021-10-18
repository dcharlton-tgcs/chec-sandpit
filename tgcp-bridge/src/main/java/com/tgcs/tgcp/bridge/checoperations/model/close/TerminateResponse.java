package com.tgcs.tgcp.bridge.checoperations.model.close;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "schema:TerminateResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminateResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "TerminateResult")
    private TerminateResult terminateResult;

    public TerminateResponse() {
    }

    public TerminateResponse(TerminateResult terminateResult) {
        this.terminateResult = terminateResult;
    }

    public TerminateResponse(String requestId) {
        this.terminateResult = new TerminateResult(requestId);
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}
