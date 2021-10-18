package com.tgcs.tgcp.bridge.checoperations.model;

public enum ErrorCode {
    ITEM_NOT_FOUND("ITEM_NOT_FOUND"),
    ITEM_NOT_FOR_SALE("ITEM_NOT_FOR_SALE"),
    NO_ITEM_MATCH_FOR_COUPON("NO_ITEM_MATCH_FOR_COUPON"),
    ITEM_WEIGHT_REQUIRED("INVALID_ARGUMENT:ITEM_WEIGHT_REQUIRED"),
    ITEM_QUANTITY_REQUIRED("INVALID_ARGUMENT:ITEM_QUANTITY_REQUIRED"),
    ITEM_PRICE_REQUIRED("INVALID_ARGUMENT:ITEM_PRICE_REQUIRED"),
    LOYALTY_AFTER_ITEM("Loyalty card can not be scanned at this time"),
    LOYALTY_ALREADY_SCANNED("Loyalty card already scanned"),
    VOID_ITEM_NOT_FOUND("Item not in transaction"),
    VOID_ITEM_FAILURE("An error occurred while voiding the item"),
    TRANSACTION_RECEIPTS_NOT_AVAILABLE("No transaction receipts were available."),
    UNSUPPORTED_OPERATION("UNSUPPORTED_OPERATION:NOT_IMPLEMENTED");

    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}

