package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddReceiptLinesResult")
public class AddReceiptLinesResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "ExceptionResult")
    private ExceptionResult exceptionResult;


    public AddReceiptLinesResult() {
    }

    public AddReceiptLinesResult(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public AddReceiptLinesResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

    public AddReceiptLinesResult setExceptionResult(ExceptionResult exceptionResult) {
        this.exceptionResult = exceptionResult;
        return this;
    }
}