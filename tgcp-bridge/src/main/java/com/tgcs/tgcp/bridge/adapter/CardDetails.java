package com.tgcs.tgcp.bridge.adapter;

public class CardDetails {

    private String accountNumber = "MjIyMjIyMDAwMDAwMDAwMA==";
    private String pinData = "0F374D32DE0E699AFFFF3A000104E6210B14";
    private String displayAccountNumber = "0000";
    private String expirationDate = "0923";
    private String entryMethod = "KEYED";
    private String firstName = "";
    private String lastName = "";

    public String getAccountNumber() {
        return accountNumber;
    }

    public CardDetails setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getPinData() {
        return pinData;
    }

    public CardDetails setPinData(String pinData) {
        this.pinData = pinData;
        return this;
    }

    public String getDisplayAccountNumber() {
        return displayAccountNumber;
    }

    public CardDetails setDisplayAccountNumber(String displayAccountNumber) {
        this.displayAccountNumber = displayAccountNumber;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public CardDetails setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public CardDetails setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public CardDetails setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public CardDetails setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getMaskedAccountNumber() {
        return "************" + displayAccountNumber;
    }
}
