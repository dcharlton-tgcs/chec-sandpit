package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PrintCurrentReceiptsResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrintCurrentReceiptsResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "ExceptionResult")
    private ExceptionResult exceptionResult;

    public PrintCurrentReceiptsResult() {
    }

    public String getRequestId() {
        return requestId;
    }

    public PrintCurrentReceiptsResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

    public PrintCurrentReceiptsResult setExceptionResult(ExceptionResult exceptionResult) {
        this.exceptionResult = exceptionResult;
        return this;
    }
}
