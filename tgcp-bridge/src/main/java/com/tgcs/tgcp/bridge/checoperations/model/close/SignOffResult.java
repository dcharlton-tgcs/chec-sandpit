package com.tgcs.tgcp.bridge.checoperations.model.close;

import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SignOffResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignOffResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "ExceptionResult")
    private ExceptionResult exceptionResult;


    public SignOffResult() {
    }

    public SignOffResult(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public SignOffResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

    public SignOffResult setExceptionResult(ExceptionResult exceptionResult) {
        this.exceptionResult = exceptionResult;
        return this;
    }
}
