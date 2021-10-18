package com.tgcs.tgcp.bridge.adapter;

import java.math.BigDecimal;

public class OrderCoupon extends Item {

    private BigDecimal value;

    private BigDecimal amount;


    public BigDecimal getValue() {
        return value;
    }

    public OrderCoupon setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderCoupon setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}
