package com.tgcs.tgcp.bridge.exception;

public enum ExceptionType {

    NGP_REQUIRED_ATTRIBUTE("REQ_ATTRIBUTE"),
    NGP_VALIDATION_ERROR("VALIDATION_ERROR"),
    GENERAL("GENERAL");

    ExceptionType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
