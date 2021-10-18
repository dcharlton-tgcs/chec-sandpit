package com.tgcs.tgcp.bridge.checoperations.model.status;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "POSBCVersion")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrinterStatus {


    @XmlElement(name = "Severity")
    private String severity;

    @XmlElement(name = "Status")
    private String status;

    @XmlElement(name = "StatusMessage")
    private String statusMessage;

    public String getSeverity() {
        return severity;
    }

    public PrinterStatus() {
    }

    public PrinterStatus(String severity, String status, String statusMessage) {
        this.severity = severity;
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

}
