package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.math.BigDecimal;

public class ConvertedCurrencyValue {

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
     * "CurrencyCode" object, what is it?
     */
    String currencyCode;

    /**
     *
     */
    CurrencyValue currencyValue;

    /**
     * (optional)
     * The conversion rate at the time of conversion.
     * This field is normally only set on cash tender values and should not be modified once set.
     */
    BigDecimal conversionRate;

    public BigDecimal getValue() {
        return value;
    }

    public ConvertedCurrencyValue setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public ConvertedCurrencyValue setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public CurrencyValue getCurrencyValue() {
        return currencyValue;
    }

    public ConvertedCurrencyValue setCurrencyValue(CurrencyValue currencyValue) {
        this.currencyValue = currencyValue;
        return this;
    }
}
