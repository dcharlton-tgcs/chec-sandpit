package com.tgcs.tgcp.bridge.checoperations.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ExceptionResult")
public class ExceptionResult {

    @XmlElement(name = "Message")
    private String message;

    @XmlElement(name = "ErrorCode")
    private String errorCode;

    @XmlElement(name = "OverridableConditions")
    private String overridableConditions;

    public String getMessage() {
        return message;
    }

    public ExceptionResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ExceptionResult setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getOverridableConditions() {
        return overridableConditions;
    }

    public ExceptionResult setOverridableConditions(String overridableConditions) {
        this.overridableConditions = overridableConditions;
        return this;
    }
}
