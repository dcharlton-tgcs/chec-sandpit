package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.ConvertedCurrencyValue;
import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.PaymentEntryData;

import java.util.Map;

public class Payment {

    /**
     * (optional)
     *  The timestamp of the creation
     */
    private String createTimestamp;

    /**
     * (optional)
     * The timestamp of the last modify
     */
    private String lastModifiedTimestamp;

    /**
     * (optional)
     * The database version used for optimistic locking. format: int64
     */
    private Long version ;

    /**
     * (optional)
     * The last user to modify the record
     */
    private String lastModifiedUserId;

    /**
     * (optional)
     *
     */
    private String id;

    /**
     * (optional)
     */
    private ConvertedCurrencyValue totalAuthorized;

    /**
     * (optional)
     */
    private  ConvertedCurrencyValue totalCharged;

    /**
     * (optional)
     */
    private ConvertedCurrencyValue totalRefunded;

    /**
     * (optional)
     *  The last request ID to modify the entity.
     *  This field is used for concurrent modification and retry scenarios to know if the colliding request is a duplicate of the attempting modification.
     */
    private String requestId;

    /**
     * (optional)
     *  The unique identifier of the configuration entity (store, call center, ...)
     */
    private String nodeId;

    /**
     * (optional)
     * The terminal / device of this payment
     */
    String endpointId;

    /**
     * (optional)
     *  Signature is required for this payment
     */
    private boolean signatureRequired;

    /**
     * (optional)
     */
    private boolean voided;

    /**
     *  (optional)
     */
 //   List<PaymentTransaction> paymentTransactions;

    /**
     * (optional)
     * If true, indicates that this payment will be processed externally;
     * if false, the payment will only be recorded with no processing
     */
    private boolean paymentHostUsed;

    /**
     * (optional)
     * Indicates whether or not this payment's information should be securely stored for future use
     */
    private boolean storePaymentInfo;

    /**
     * (optional)
     * Indicates whether or not to use stored payment information for this transaction (ex. digital wallet)
     */
    private boolean useStoredPaymentInfo;

    /**
     * (optional)
     *  The display name for this payment type that will used on the UI and receipt as display name
     */
    private Map<String, String> displayName;

    /**
     * (optional)
     * Indicator of whether this is the current payment being processed on the Order. (Not Persisted)
     */
    private boolean currentPayment;

    /**
     * (optional)
     */
    private PaymentEntryData paymentEntryData;

    /**
     * (optional)
     */
    private ConvertedCurrencyValue amountToRefund;

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public Payment setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public Payment setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Payment setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public Payment setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
        return this;
    }

    public String getId() {
        return id;
    }

    public Payment setId(String id) {
        this.id = id;
        return this;
    }

    public ConvertedCurrencyValue getTotalAuthorized() {
        return totalAuthorized;
    }

    public Payment setTotalAuthorized(ConvertedCurrencyValue totalAuthorized) {
        this.totalAuthorized = totalAuthorized;
        return this;
    }

    public ConvertedCurrencyValue getTotalCharged() {
        return totalCharged;
    }

    public Payment setTotalCharged(ConvertedCurrencyValue totalCharged) {
        this.totalCharged = totalCharged;
        return this;
    }

    public ConvertedCurrencyValue getTotalRefunded() {
        return totalRefunded;
    }

    public Payment setTotalRefunded(ConvertedCurrencyValue totalRefunded) {
        this.totalRefunded = totalRefunded;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public Payment setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public Payment setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public Payment setEndpointId(String endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    public boolean getSignatureRequired() {
        return signatureRequired;
    }

    public Payment setSignatureRequired(boolean signatureRequired) {
        this.signatureRequired = signatureRequired;
        return this;
    }

    public boolean getVoided() {
        return voided;
    }

    public Payment setVoided(boolean voided) {
        this.voided = voided;
        return this;
    }

    public boolean getPaymentHostUsed() {
        return paymentHostUsed;
    }

    public Payment setPaymentHostUsed(boolean paymentHostUsed) {
        this.paymentHostUsed = paymentHostUsed;
        return this;
    }

    public boolean getStorePaymentInfo() {
        return storePaymentInfo;
    }

    public Payment setStorePaymentInfo(boolean storePaymentInfo) {
        this.storePaymentInfo = storePaymentInfo;
        return this;
    }

    public boolean getUseStoredPaymentInfo() {
        return useStoredPaymentInfo;
    }

    public Payment setUseStoredPaymentInfo(boolean useStoredPaymentInfo) {
        this.useStoredPaymentInfo = useStoredPaymentInfo;
        return this;
    }

    public Map<String, String> getDisplayName() {
        return displayName;
    }

    public Payment setDisplayName(Map<String, String> displayName) {
        this.displayName = displayName;
        return this;
    }

    public boolean getCurrentPayment() {
        return currentPayment;
    }

    public Payment setCurrentPayment(boolean currentPayment) {
        this.currentPayment = currentPayment;
        return this;
    }

    public PaymentEntryData getPaymentEntryData() {
        return paymentEntryData;
    }

    public Payment setPaymentEntryData(PaymentEntryData paymentEntryData) {
        this.paymentEntryData = paymentEntryData;
        return this;
    }

    public ConvertedCurrencyValue getAmountToRefund() {
        return amountToRefund;
    }

    public Payment setAmountToRefund(ConvertedCurrencyValue amountToRefund) {
        this.amountToRefund = amountToRefund;
        return this;
    }
}
