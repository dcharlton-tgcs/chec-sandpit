package com.tgcs.tgcp.bridge.checoperations.model.status;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "POSBCStatus")
public class POSBCStatus {

    @XmlElement(name = "Severity")
    private String severity;

    @XmlElement(name = "Status")
    private String status;

    @XmlElement(name = "StatusMessage")
    private String statusMessage;

    public POSBCStatus() {
    }

    public POSBCStatus(String severity, String status, String statusMessage) {
        this.severity = severity;
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public String getSeverity() {
        return severity;
    }

    public POSBCStatus setSeverity(String severity) {
        this.severity = severity;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public POSBCStatus setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public POSBCStatus setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }
}
