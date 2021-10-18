package com.tgcs.tgcp.bridge.exception;

import com.tgcs.tgcp.bridge.webservice.ngp.WsErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoidItemException extends Exception {
    private static final Logger logger = LoggerFactory.getLogger(WsErrorResponse.class);

    String errorCode;
    String errorMessage;


    public VoidItemException(Throwable t) {
        super(t);
    }

    public VoidItemException(String errorCode, String errorMessage, Throwable t) {
        super(t);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public VoidItemException(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
