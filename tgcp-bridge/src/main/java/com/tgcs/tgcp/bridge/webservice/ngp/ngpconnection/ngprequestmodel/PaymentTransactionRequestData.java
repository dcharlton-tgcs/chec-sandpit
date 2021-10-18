package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.util.Map;

public class PaymentTransactionRequestData {

    /**
     * (optional)
     */
    PaymentTransactionAction type;

    /**
     * (optional)
     */
    ConvertedCurrencyValue requestedAmount;

    /**
     * (optional)
     * An authorization code to be specified for voice authorization (ex. call for authorization)
     */
    String voiceAuthorizationCode;

    /**
     * (optional)
     * ID for the host processor of this payment transaction
     */
    String paymentProcessor;

    /**
     * (optional)
     * The reference ID from the payment processor to be used for future payment transactions against this one (ex. refunds)
     */
    String paymentProcessorReferenceId;

    /**
     * (optional)
     */
    String referencePaymentTransactionId;

    /**
     * (optional)
     * EMV tags required for payment authorization in the request
     */
    String emvRequestTags;

    /**
     * (optional)
     * EMV confirmation tags
     */
    String emvConfirmationTags;

    /**
     * (optional)
     * Tags for EMV fallback processing in the request
     */
    String  emvFallbackTags;

    /**
     * (optional)
     */
    Map<String, Object> attributes;

    public ConvertedCurrencyValue getRequestedAmount() {
        return requestedAmount;
    }

    public PaymentTransactionRequestData setRequestedAmount(ConvertedCurrencyValue requestedAmount) {
        this.requestedAmount = requestedAmount;
        return this;
    }
}
