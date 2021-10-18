package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderResponse {

    /**
     * validations (optional)
     */
    private List validation = new ArrayList();

    /**
     * reasonCodes (optional)
     */
    private List reasonCode = new ArrayList();

    /**
     * The timestamp of the creation (optional)
     */
    private String createTimestamp;

    /**
     * The timestamp of the last modify (optional)
     */
    private String lastModifiedTimestamp;

    /**
     * The database version used for optimistic locking. format: int64 (optional)
     */
    private Long version;

    /**
     * The last user to modify the record (optional)
     */
    private String lastModifiedUserId;

    /**
     * (optional)
     */
    private String id;

    /**
     * The last request ID to modify the order.
     * This field is used for concurrent modification and retry scenarios to know if the colliding request is duplicate
     * of the attempting modification.
     * (optional)
     */
    private String lastModifiedRequestId;

    /**
     * Unique order identifier (optional)
     */
    private String orderNumber;

    /**
     * This optional field is used during the data synchronization between enterprise and in-store servers.
     * In certain cases, it is possible that order numbers will collide, and we use this field to preserve the original order
     * (optional)
     */
    private String originalOrderNumber;

    /**
     * Used to track concurrent modifications to the Order once submitted so that conflicts can be merged. format: int32
     * (optional)
     */
    private Integer syncCount;

    /**
     * Tracks the current points being awarded on the order format: int32 (optional).
     */
    private Integer totalPointsAwarded;

    /**
     * Tracks the points being used on this order format: int32 (optional).
     */
    private Integer pointsUsed;

    /**
     * Orders lifecycle history of unique identifiers.
     * Used to understand parent hierarchy when merging orders that have had concurrent modifications.
     * (optional)
     */
    private String[] syncHistory;


    /**
     * Action
     * (optional)
     */
//   private Action action

    /**
     *  OrderStatus (optional)
     */
//   private OrderStatus status;

    /**
     *  CurrencyCode (optional)
     */
//   private CurrencyCode currencyCode

    /**
     * The schema version of this order. This may be used by backend processes to alter processing workflows as needed.
     * (optional)
     */
    private String schema;

    /**
     * (optional)
     */
    private Map<String, Object> attributes;


    /**
     * OrderCustomer (optional)
     */
//   private OrderCustomer returnCustomer;

    /**
     * (optional)
     */
    private String currentAssociateId;

    /**
     * array[OrderAssociate] (optional)
     */
    private List associates = new ArrayList();

    /**
     * array[OrderFulfillmentGroup] (optional)
     */
    private List fulfillmentGroups = new ArrayList();

    /**
     * array[OrderItem] The model of an item in the Order.
     * By default OrderItems are collapsed together for matching sku.
     * However, items for matching skuId will be split when serial, weight, childItems, associates, price required,
     * or other fields requiring association to a specific item instance are set.
     * (optional)
     */
    private List<OrderItem> items = new ArrayList<>();

    /**
     * array[Payment] (optional)
     */
    private List<Payment> payments = new ArrayList<>();

    /**
     * array[OrderValidationApproval] (optional)
     */
    private List validationApprovals = new ArrayList();

    /**
     * array[Tax]
     * List of all aggregate taxes applied to this order usually aggregated by tax type either by the external tax provider or tax service.
     * (optional)
     */
    private List taxes = new ArrayList();

    /**
     * array[TaxDetail]
     * List of all individual taxes with details applied to this order usually obtained from the external tax provider calculations.
     * The tax detail on the Order are the actual taxes applied to the order and the details on OrderItem are for informational.
     * (optional)
     */
    private List taxDetails = new ArrayList();

    /**
     * The flag indicates if the buyer/customer on the order is exempted from paying taxes on the order
     * (optional)
     */
    private Boolean taxExemptFlag;

    /**
     * TaxExemptDetail (optional)
     */
//    TaxExemptDetail taxExemptDetail;

    /**
     * Total item count in the Order (optional)
     */
    private BigDecimal totalItemCount;

    /**
     * This is the grand total for all item quantities price modifications after taxes (optional)
     */
    private BigDecimal total;

    /**
     * This is the grand total of the charges/shipping/etc (no taxes).(optional)
     */
    private BigDecimal totalPriceModifications;

    /**
     * This is the grand total of the item prices. (optional)
     */
    private BigDecimal totalItems;

    /**
     * This is the grand total of the order payments. (optional)
     */
    private BigDecimal totalPayments;

    /**
     * This is the grand total of the order taxes. (optional)
     */
    private BigDecimal totalTaxes;

    /**
     * The total of this order including all modifications
     * (with or without tax depending on wether the itemprices are tax included or not - EU/US).
     * It is up to the tax service to do the right calculation.
     * (optional)
     */
    private BigDecimal subTotal;

    /**
     * Indicates whether or not the subTotal fields already have tax included
     * (Typically for US=false, EU=true)
     * (optional)
     */
    private Boolean subTotalIncludesTax;

    /**
     * Total of the item level amounts used for the tax calculation (items and discounts with tax exemption are not included in this amount).
     * This is calculated by the totals processor before invoking the tax service.
     * (optional)
     */
    private BigDecimal totalTaxableAmount;

    /**
     * The total of the order including all quantities, modifications, prices but before taxes applied. (optional)
     */
    private BigDecimal totalBeforeTaxes;

    /**
     * Keeps track of payment shortage on the order.
     * Positive value is a shortage (need more payment to complete order),
     * Negative value means change is due.
     * (optional)
     */
    private BigDecimal paymentShortage;

    /**
     * List of the orderIds that can be used as source Orders when returning items within this order.(optional)
     */
    private String[] returnOrderIds;

    /**
     * array[EligibleRefundPayment]
     * List of eligible refund payments and their amounts
     */
    private List refundPaymentInfo;

    /**
     * Are we in training mode?  (optional)
     */
    private Boolean trainingMode;


    public List getValidation() {
        return validation;
    }

    public OrderResponse setValidation(List validation) {
        this.validation = validation;
        return this;
    }

    public List getReasonCode() {
        return reasonCode;
    }

    public OrderResponse setReasonCode(List reasonCode) {
        this.reasonCode = reasonCode;
        return this;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public OrderResponse setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public OrderResponse setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public OrderResponse setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public OrderResponse setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
        return this;
    }

    public String getId() {
        return id;
    }

    public OrderResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getLastModifiedRequestId() {
        return lastModifiedRequestId;
    }

    public OrderResponse setLastModifiedRequestId(String lastModifiedRequestId) {
        this.lastModifiedRequestId = lastModifiedRequestId;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OrderResponse setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public String getOriginalOrderNumber() {
        return originalOrderNumber;
    }

    public OrderResponse setOriginalOrderNumber(String originalOrderNumber) {
        this.originalOrderNumber = originalOrderNumber;
        return this;
    }

    public Integer getSyncCount() {
        return syncCount;
    }

    public OrderResponse setSyncCount(Integer syncCount) {
        this.syncCount = syncCount;
        return this;
    }

    public Integer getTotalPointsAwarded() {
        return totalPointsAwarded;
    }

    public OrderResponse setTotalPointsAwarded(Integer totalPointsAwarded) {
        this.totalPointsAwarded = totalPointsAwarded;
        return this;
    }

    public Integer getPointsUsed() {
        return pointsUsed;
    }

    public OrderResponse setPointsUsed(Integer pointsUsed) {
        this.pointsUsed = pointsUsed;
        return this;
    }

    public String[] getSyncHistory() {
        return syncHistory;
    }

    public OrderResponse setSyncHistory(String[] syncHistory) {
        this.syncHistory = syncHistory;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public OrderResponse setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public OrderResponse setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public String getCurrentAssociateId() {
        return currentAssociateId;
    }

    public OrderResponse setCurrentAssociateId(String currentAssociateId) {
        this.currentAssociateId = currentAssociateId;
        return this;
    }

    public List getAssociates() {
        return associates;
    }

    public OrderResponse setAssociates(List associates) {
        this.associates = associates;
        return this;
    }

    public List getFulfillmentGroups() {
        return fulfillmentGroups;
    }

    public OrderResponse setFulfillmentGroups(List fulfillmentGroups) {
        this.fulfillmentGroups = fulfillmentGroups;
        return this;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderResponse setItems(List<OrderItem> items) {
        this.items = items;
        return this;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public OrderResponse setPayments(List<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public List getValidationApprovals() {
        return validationApprovals;
    }

    public OrderResponse setValidationApprovals(List validationApprovals) {
        this.validationApprovals = validationApprovals;
        return this;
    }

    public List getTaxes() {
        return taxes;
    }

    public OrderResponse setTaxes(List taxes) {
        this.taxes = taxes;
        return this;
    }

    public List getTaxDetails() {
        return taxDetails;
    }

    public OrderResponse setTaxDetails(List taxDetails) {
        this.taxDetails = taxDetails;
        return this;
    }

    public Boolean getTaxExemptFlag() {
        return taxExemptFlag;
    }

    public OrderResponse setTaxExemptFlag(Boolean taxExemptFlag) {
        this.taxExemptFlag = taxExemptFlag;
        return this;
    }

    public BigDecimal getTotalItemCount() {
        return totalItemCount;
    }

    public OrderResponse setTotalItemCount(BigDecimal totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderResponse setTotal(BigDecimal total) {
        this.total = total;
        return this;
    }

    public BigDecimal getTotalPriceModifications() {
        return totalPriceModifications;
    }

    public OrderResponse setTotalPriceModifications(BigDecimal totalPriceModifications) {
        this.totalPriceModifications = totalPriceModifications;
        return this;
    }

    public BigDecimal getTotalItems() {
        return totalItems;
    }

    public OrderResponse setTotalItems(BigDecimal totalItems) {
        this.totalItems = totalItems;
        return this;
    }

    public BigDecimal getTotalPayments() {
        return totalPayments;
    }

    public OrderResponse setTotalPayments(BigDecimal totalPayments) {
        this.totalPayments = totalPayments;
        return this;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public OrderResponse setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
        return this;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public OrderResponse setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public Boolean getSubTotalIncludesTax() {
        return subTotalIncludesTax;
    }

    public OrderResponse setSubTotalIncludesTax(Boolean subTotalIncludesTax) {
        this.subTotalIncludesTax = subTotalIncludesTax;
        return this;
    }

    public BigDecimal getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public OrderResponse setTotalTaxableAmount(BigDecimal totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
        return this;
    }

    public BigDecimal getTotalBeforeTaxes() {
        return totalBeforeTaxes;
    }

    public OrderResponse setTotalBeforeTaxes(BigDecimal totalBeforeTaxes) {
        this.totalBeforeTaxes = totalBeforeTaxes;
        return this;
    }

    public BigDecimal getPaymentShortage() {
        return paymentShortage;
    }

    public OrderResponse setPaymentShortage(BigDecimal paymentShortage) {
        this.paymentShortage = paymentShortage;
        return this;
    }

    public String[] getReturnOrderIds() {
        return returnOrderIds;
    }

    public OrderResponse setReturnOrderIds(String[] returnOrderIds) {
        this.returnOrderIds = returnOrderIds;
        return this;
    }

    public List getRefundPaymentInfo() {
        return refundPaymentInfo;
    }

    public OrderResponse setRefundPaymentInfo(List refundPaymentInfo) {
        this.refundPaymentInfo = refundPaymentInfo;
        return this;
    }

    public Boolean getTrainingMode() {
        return trainingMode;
    }

    public OrderResponse setTrainingMode(Boolean trainingMode) {
        this.trainingMode = trainingMode;
        return this;
    }

}
