package com.tgcs.tgcp.bridge.checoperations;

public class ChecOperationException extends Exception {

    private String errorCode;
    private String errorMessage;

    public ChecOperationException( String errorCode, String errorMessage, Throwable t) {
        super(t);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ChecOperationException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ChecOperationException setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
