package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import java.math.BigDecimal;

public class ChildOrderItemRef {

    /**
     * (optional)
     */
    private String id;

    /**
     * (optional)
     */
    private BigDecimal quantity;

    public String getId() {
        return id;
    }

    public ChildOrderItemRef setId(String id) {
        this.id = id;
        return this;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public ChildOrderItemRef setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }
}
