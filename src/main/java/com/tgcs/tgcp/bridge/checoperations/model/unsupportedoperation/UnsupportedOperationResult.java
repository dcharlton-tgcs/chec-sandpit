package com.tgcs.tgcp.bridge.checoperations.model.unsupportedoperation;

import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.NodeType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UnsupportedOperationResult implements NodeType {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "ExceptionResult")
    private ExceptionResult exceptionResult;

    public String getRequestId() {
        return requestId;
    }

    public UnsupportedOperationResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

    public UnsupportedOperationResult setExceptionResult(ExceptionResult exceptionResult) {
        this.exceptionResult = exceptionResult;
        return this;
    }

    @Override
    public String getNodeType() {
        return NodeTypeValues.RESULT.getValue();
    }
}
