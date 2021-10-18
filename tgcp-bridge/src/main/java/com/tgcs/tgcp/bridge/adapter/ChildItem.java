package com.tgcs.tgcp.bridge.adapter;

import java.math.BigDecimal;

public class ChildItem implements ChecAdapter {

    private String id;

    private BigDecimal quantity;

    public String getId() {
        return id;
    }

    public ChildItem setId(String id) {
        this.id = id;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public ChildItem setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }
}
