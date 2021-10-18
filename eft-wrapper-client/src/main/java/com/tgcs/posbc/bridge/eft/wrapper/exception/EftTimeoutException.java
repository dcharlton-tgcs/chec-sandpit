package com.tgcs.posbc.bridge.eft.wrapper.exception;

public class EftTimeoutException extends EftException {

    public static final String MESSAGE = "EFT Wrapper Service call is timeout";

    public EftTimeoutException() {
        super(BRIDGE_ERROR, MESSAGE);
    }
}
