package com.tgcs.posbc.bridge.eft.wrapper.client.dto;

import java.util.HashMap;
import java.util.Map;

public class PaymentTransaction {

    public static final String VALUE = "value";

    private final Map<String, Object> requestedAmount = new HashMap<>();

    public PaymentTransaction value(int value) {
        requestedAmount.put(VALUE, value);
        return this;
    }

    public Map<String, Object> getRequestedAmount() {
        return requestedAmount;
    }
}
