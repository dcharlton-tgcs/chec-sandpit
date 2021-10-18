package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import java.math.BigDecimal;

public class TillContentEntry {

    String type;

    String currencyCode;

    BigDecimal amount;

    int quantity;

    BigDecimal loanedAmount;

    int loanedQuantity;

    public String getType() {
        return type;
    }

    public TillContentEntry setType(String type) {
        this.type = type;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public TillContentEntry setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TillContentEntry setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public TillContentEntry setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getLoanedAmount() {
        return loanedAmount;
    }

    public TillContentEntry setLoanedAmount(BigDecimal loanedAmount) {
        this.loanedAmount = loanedAmount;
        return this;
    }

    public int getLoanedQuantity() {
        return loanedQuantity;
    }

    public TillContentEntry setLoanedQuantity(int loanedQuantity) {
        this.loanedQuantity = loanedQuantity;
        return this;
    }
}
