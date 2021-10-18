package com.tgcs.tgcp.bridge.checoperations.model.status;

public enum TransactionStatusType {

    NOT_IN_TRANSACTION("NOT_IN_TRANSACTION"),
    TRANSACTION_START("TRANSACTION_START"),
    TRANSACTION_VOID ("TRANSACTION_VOID"),
    TRANSACTION_SUSPENDED ("TRANSACTION_SUSPENDED"),
    TRANSACTION_END ("TRANSACTION_END");

    private String value;

    private TransactionStatusType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
