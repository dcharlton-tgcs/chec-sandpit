package com.tgcs.tgcp.bridge.checoperations.model.receipt;

public enum LineCategory {
    HEADER("Header"),
    ITEM("LineItem"),
    TRAILER("Trailer");

    LineCategory(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}

