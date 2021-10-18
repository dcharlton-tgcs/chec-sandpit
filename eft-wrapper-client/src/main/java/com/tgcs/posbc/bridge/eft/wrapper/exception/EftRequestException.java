package com.tgcs.posbc.bridge.eft.wrapper.exception;

public class EftRequestException extends EftException {

    public static final String MESSAGE = "EFT Wrapper Service call fail : %s";

    public EftRequestException(Throwable throwable) {
        super(BRIDGE_ERROR, String.format(MESSAGE, throwable.getMessage()));
    }

    public EftRequestException(String message) {
        super(BRIDGE_ERROR, String.format(MESSAGE, message));
    }
}
