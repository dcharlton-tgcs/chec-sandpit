package com.tgcs.tgcp.bridge.checoperations.model.totals;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:GetTotalsResponse")
public class GetTotalsResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "GetTotalsResult")
    GetTotalsResult getTotalsResult;

    public GetTotalsResponse() {
    }

    public GetTotalsResult getGetTotalsResult() {
        return getTotalsResult;
    }

    public GetTotalsResponse setGetTotalsResult(GetTotalsResult getTotalsResult) {
        this.getTotalsResult = getTotalsResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseType.ResponseTypeValues.RESPONSE.getValue();
    }
}
