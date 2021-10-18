package com.tgcs.tgcp.bridge.checoperations.model.close;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TerminateResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class TerminateResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    public TerminateResult() { }

    public TerminateResult(String requestId) {
        this.requestId = requestId;
    }

}
