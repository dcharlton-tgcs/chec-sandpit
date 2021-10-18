package com.tgcs.posbc.bridge.eft.wrapper.exception;

public class ReprintNeededException extends EftException {

    public static final String MESSAGE = "Need to reprint last transaction";

    public ReprintNeededException() {
        super(BRIDGE_ERROR, MESSAGE);
    }
}
