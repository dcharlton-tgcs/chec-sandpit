package com.tgcs.tgcp.bridge.adapter;

import java.util.Map;

public class PaymentInfo implements ChecAdapter {
    /**
     * List of common values for the {@code paymentType} field of {@code PaymentInfo}.
     * {@code paymentType} is a {@code String} value that can contain other values, that are not found in this enumeration.
     */
    public enum PaymentType {
        CASH("CASH"),
        DEBIT("DEBIT"),
        CREDIT("CREDIT"),
        OTHER("OTHER");

        public String value;
        PaymentType(String value) {
            this.value = value;
        }
    }

    /**
     * List of possible values for the {@code paymentTypeGroup} field of {@code PaymentInfo}.
     */
    public enum PaymentTypeGroup {
        CASH("CASH"),
        DEBIT("DEBIT"),
        CREDIT("CREDIT"),
        OTHER("OTHER");

        public String value;
        PaymentTypeGroup(String value) {
            this.value = value;
        }
    }

    private String paymentId;
    private String requestId;
    private String paymentType;
    private PaymentTypeGroup paymentTypeGroup;
    private String amount;
    private String total;
    private String currency;
    private boolean signatureNeeded;
    private boolean signatureIndex;
    private boolean isVoid;
    private Map<String, String> additional;

    private CardDetails cardDetails = new CardDetails();

    public String getPaymentType() {
        return paymentType;
    }

    public PaymentInfo setPaymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public PaymentInfo setPaymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public PaymentInfo setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getTotal() {
        return total;
    }

    public PaymentInfo setTotal(String total) {
        this.total = total;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentInfo setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public PaymentInfo setVoid(boolean aVoid) {
        isVoid = aVoid;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public PaymentInfo setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public boolean isSignatureNeeded() {
        return signatureNeeded;
    }

    public PaymentInfo setSignatureNeeded(boolean signatureNeeded) {
        this.signatureNeeded = signatureNeeded;
        return this;
    }

    public boolean isSignatureIndex() {
        return signatureIndex;
    }

    public PaymentInfo setSignatureIndex(boolean signatureIndex) {
        this.signatureIndex = signatureIndex;
        return this;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public PaymentInfo setCardDetails(CardDetails cardDetails) {
        this.cardDetails = cardDetails;
        return this;
    }

    public Map<String, String> getAdditional() {
        return additional;
    }

    public PaymentInfo setAdditional(Map<String, String> additional) {
        this.additional = additional;
        return this;
    }

    public PaymentTypeGroup getPaymentTypeGroup() {
        return paymentTypeGroup;
    }

    public PaymentInfo setPaymentTypeGroup(PaymentTypeGroup paymentTypeGroup) {
        this.paymentTypeGroup = paymentTypeGroup;
        return this;
    }
}
