package com.tgcs.posbc.bridge.eft.wrapper.exception;

public class EftException extends RuntimeException {

    public static final int BRIDGE_ERROR = -100;

    private final int code;

    public EftException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() { return this.code; }
}
