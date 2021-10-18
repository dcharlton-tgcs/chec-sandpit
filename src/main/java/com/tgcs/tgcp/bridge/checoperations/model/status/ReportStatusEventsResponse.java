package com.tgcs.tgcp.bridge.checoperations.model.status;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "schema:ReportStatusEventsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportStatusEventsResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "ReportStatusEventsResult")
    ReportStatusEventsResult reportStatusEventsResult;

    public ReportStatusEventsResponse() {
    }

    public ReportStatusEventsResponse(ReportStatusEventsResult reportStatusEventsResult) {
        this.reportStatusEventsResult = reportStatusEventsResult;
    }

    public ReportStatusEventsResponse(String requestId) {
        this.reportStatusEventsResult = new ReportStatusEventsResult(requestId);
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}
