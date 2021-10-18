package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.math.BigDecimal;

public class CurrencyValue {

    /**
     * The 'value' rounded based on the currency rules for ISO currency code from the 'currencyCode' field.
     */
    BigDecimal value;

    /**
     * (optional)
     * The original un-rounded 'value'.
     */
    BigDecimal originalValue;

    /**
     * CurrencyCode
     */
    String currencyCode;

    public BigDecimal getValue() {
        return value;
    }

    public CurrencyValue setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public CurrencyValue setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
}
