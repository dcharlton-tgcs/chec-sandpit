package com.tgcs.posbc.bridge.eft.wrapper.client.dto;

import com.tgcs.posbc.bridge.eft.wrapper.model.EftContext;

public class DebitRequestBody extends RequestBody {

    private PaymentTransaction paymentTransaction;

    public DebitRequestBody(EftContext context) {
        super(context);
    }

    public DebitRequestBody setPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
        return this;
    }

    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }
}
