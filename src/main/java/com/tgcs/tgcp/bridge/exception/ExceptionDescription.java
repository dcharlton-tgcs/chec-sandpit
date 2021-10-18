package com.tgcs.tgcp.bridge.exception;

public enum ExceptionDescription {

    NGP_QUANTITY_NEEDED_UOM_NOT_EACH("QUANTITY_NEEDED_UOM_NOT_EACH"),
    COUPON_NOT_FOUND("COUPON_NOT_FOUND"),
    BARCODE_NOT_FOUND("BARCODE_NOT_FOUND"),
    CUSTOMER_ALREADY_EXISTS("CUSTOMER_ALREADY_EXISTS"),
    NOT_FOR_SALE("NOT_FOR_SALE"),
    QUANTITY_REQUIRED("QUANTITY_REQUIRED"),
    LOYALTY_CARD_NOT_ALLOWED("TPNET_LOYALTY_CARD_NOT_ALLOWED"),
    TENDER_NOT_SUPPORTED("TPNET_TENDER_NOT_SUPPORTED"),
    ADD_PAYMENT_FAILED("TPNET_ADD_PAYMENT_FAILED");

    ExceptionDescription(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}