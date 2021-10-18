package com.tgcs.tgcp.bridge.checoperations.model.receipt;

public enum LineType {
    HEADER("StoreHeader"),
    ITEM("ItemSale"),
    COUPON("StoreCoupon"),
    SUSPEND_TRANSACTION("SuspendTransaction"),
    VOID_TRANSACTION("VoidTransaction"),
    TENDER("Tender"),
    CHANGE("Change"),
    WORKSTATION_INFO("WorkstationInfo"),
    LOYALTY_MESSAGES("LoyaltyMessages"),
    TENDER_FRANKING("TenderFranking)"),
    CUSTOMER_ID_ENTRY("CustomerIDEntry");

    LineType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }



}

