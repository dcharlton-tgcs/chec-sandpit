package com.tgcs.tgcp.bridge.checoperations.model.receipt;

public enum ReceiptSection {
    HEADER("Header"),
    ITEM("Body"),
    TRAILER("Trailer"),
    BODY("Body");

    ReceiptSection(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
