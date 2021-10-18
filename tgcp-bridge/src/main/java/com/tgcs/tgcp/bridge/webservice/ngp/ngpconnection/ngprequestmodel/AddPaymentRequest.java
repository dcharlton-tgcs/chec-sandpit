package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.util.Map;

public class AddPaymentRequest extends WebRequestBody {

    /**
     * (optional)
     */
    String id;

    /**
     * (optional)
     */
    //ConvertedCurrencyValue totalAuthorized;

    /**
     * (optional)
     */
    //ConvertedCurrencyValue totalCharged;

    /**
     * (optional)
     */
    //ConvertedCurrencyValue totalRefunded;

    /**
     * (optional)
     * The last request ID to modify the entity.
     * This field is used for concurrent modification and retry scenarios
     * to know if the colliding request is a duplicate of the attempting modification.
     */
    String  requestId;

    /**
     * (optional)
     * The unique identifier of the configuration entity (store, call center, ...)
     */
    String nodeId;

    /**
     * (optional)
     * The terminal / device of this payment
     */
    String endpointId;


    /**
     * (optional)
     * Signature is required for this payment
     */
    Boolean signatureRequired;

    /**
     * (optional)
     */
    Boolean voided;


    /**
     * (optional)
     */
    //ArrayList<PaymentTransaction>paymentTransactions;


    /**
     * (optional)
     * If true, indicates that this payment will be processed externally;
     * if false, the payment will only be recorded with no processing
     */
    Boolean paymentHostUsed;

    /**
     * (optional)
     * Indicates whether or not this payment's information should be securely stored for future use
     */
    Boolean storePaymentInfo;


    /**
     * (optional)
     * Indicates whether or not to use stored payment information for this transaction (ex. digital wallet)
     */
    Boolean useStoredPaymentInfo;

    /**
     * (optional)
     * The display name for this payment type that will used on the UI and receipt as display name
     */
    Map<String, String> displayName;

    /**
     * (optional)
     * Indicator of whether this is the current payment being processed on the Order. (Not Persisted)
     */
    Boolean currentPayment;


    /**
     * (optional)
     *
     */
    PaymentEntryData paymentEntryData;

    /**
     * (optional)
     */
    ConvertedCurrencyValue amountToRefund;

    /**
     * (required)
     */
    Context context;

    /**
     * (required)
     */
    PaymentTransactionRequestData paymentTransaction;

    //additionalProperties (optional)


    public String getId() {
        return id;
    }

    public AddPaymentRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public AddPaymentRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public AddPaymentRequest setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public AddPaymentRequest setEndpointId(String endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    public Boolean getSignatureRequired() {
        return signatureRequired;
    }

    public AddPaymentRequest setSignatureRequired(Boolean signatureRequired) {
        this.signatureRequired = signatureRequired;
        return this;
    }

    public Boolean getVoided() {
        return voided;
    }

    public AddPaymentRequest setVoided(Boolean voided) {
        this.voided = voided;
        return this;
    }

    public Boolean getPaymentHostUsed() {
        return paymentHostUsed;
    }

    public AddPaymentRequest setPaymentHostUsed(Boolean paymentHostUsed) {
        this.paymentHostUsed = paymentHostUsed;
        return this;
    }

    public Boolean getStorePaymentInfo() {
        return storePaymentInfo;
    }

    public AddPaymentRequest setStorePaymentInfo(Boolean storePaymentInfo) {
        this.storePaymentInfo = storePaymentInfo;
        return this;
    }

    public Boolean getUseStoredPaymentInfo() {
        return useStoredPaymentInfo;
    }

    public AddPaymentRequest setUseStoredPaymentInfo(Boolean useStoredPaymentInfo) {
        this.useStoredPaymentInfo = useStoredPaymentInfo;
        return this;
    }

    public Map<String, String> getDisplayName() {
        return displayName;
    }

    public AddPaymentRequest setDisplayName(Map<String, String> displayName) {
        this.displayName = displayName;
        return this;
    }

    public Boolean getCurrentPayment() {
        return currentPayment;
    }

    public AddPaymentRequest setCurrentPayment(Boolean currentPayment) {
        this.currentPayment = currentPayment;
        return this;
    }

    public PaymentEntryData getPaymentEntryData() {
        return paymentEntryData;
    }

    public AddPaymentRequest setPaymentEntryData(PaymentEntryData paymentEntryData) {
        this.paymentEntryData = paymentEntryData;
        return this;
    }

    public ConvertedCurrencyValue getAmountToRefund() {
        return amountToRefund;
    }

    public AddPaymentRequest setAmountToRefund(ConvertedCurrencyValue amountToRefund) {
        this.amountToRefund = amountToRefund;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public AddPaymentRequest setContext(Context context) {
        this.context = context;
        return this;
    }

    public PaymentTransactionRequestData getPaymentTransaction() {
        return paymentTransaction;
    }

    public AddPaymentRequest setPaymentTransaction(PaymentTransactionRequestData paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
        return this;
    }
}
