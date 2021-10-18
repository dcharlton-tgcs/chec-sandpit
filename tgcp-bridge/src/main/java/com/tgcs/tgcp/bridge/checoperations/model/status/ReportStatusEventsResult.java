package com.tgcs.tgcp.bridge.checoperations.model.status;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ReportStatusEventsResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportStatusEventsResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    public ReportStatusEventsResult() {
    }

    public ReportStatusEventsResult(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
