package com.tgcs.tgcp.bridge.checoperations.model.voidorsuspend;

import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VoidTransactionResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoidTransactionResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "ExceptionResult")
    private ExceptionResult exceptionResult;

    public VoidTransactionResult() {
    }

    public VoidTransactionResult(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public VoidTransactionResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

    public VoidTransactionResult setExceptionResult(ExceptionResult exceptionResult) {
        this.exceptionResult = exceptionResult;
        return this;
    }
}
