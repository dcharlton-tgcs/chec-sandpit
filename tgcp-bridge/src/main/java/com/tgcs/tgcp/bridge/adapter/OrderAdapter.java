package com.tgcs.tgcp.bridge.adapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderAdapter implements ChecAdapter {

    private String orderId;

    private String orderNumber;

    private String requestId;

    private BigDecimal total;

    /**
     * should be same as Total if there is no Tax
     */
    private BigDecimal subtotal;

    private BigDecimal totalPayments;

    private BigDecimal totalTax;

    private BigDecimal totalItemCount;

    /**
     * Keeps track of payment shortage on the order.
     * Positive value is a shortage (need more payment to complete order),
     * negative value means change is due.
     */
    private BigDecimal paymentShortage;

    private List<PaymentInfo> paymentInfoList = new ArrayList<>();

    private List<OrderItem> itemList = new ArrayList<>();

    private List<OrderCoupon> couponList = new ArrayList<>();

    private boolean isVoid;

    /**
     * The timestamp of the creation
     */
    private String createTimestamp;

    /**
     * The timestamp of the last modify
     */
    private String lastModifiedTimestamp;

    private BigDecimal couponTotal;

    private OrderCustomer customer;

    private BigDecimal totalPriceModifications;

    public String getOrderId() {
        return orderId;
    }

    public OrderAdapter setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public OrderAdapter setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public OrderAdapter setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderAdapter setTotal(BigDecimal total) {
        this.total = total;
        return this;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public OrderAdapter setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public BigDecimal getTotalPayments() {
        return totalPayments;
    }

    public OrderAdapter setTotalPayments(BigDecimal totalPayments) {
        this.totalPayments = totalPayments;
        return this;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public OrderAdapter setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
        return this;
    }

    public BigDecimal getTotalItemCount() {
        return totalItemCount;
    }

    public OrderAdapter setTotalItemCount(BigDecimal totalItemCount) {
        this.totalItemCount = totalItemCount;
        return this;
    }

    public BigDecimal getPaymentShortage() {
        return paymentShortage;
    }

    public OrderAdapter setPaymentShortage(BigDecimal paymentShortage) {
        this.paymentShortage = paymentShortage;
        return this;
    }

    public List<PaymentInfo> getPaymentInfoList() {
        return paymentInfoList;
    }

    public OrderAdapter setPaymentInfoList(List<PaymentInfo> paymentInfoList) {
        this.paymentInfoList = paymentInfoList;
        return this;
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public OrderAdapter setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
        return this;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public OrderAdapter setVoid(boolean aVoid) {
        isVoid = aVoid;
        return this;
    }

    public String getBalanceDue() {
        return paymentShortage.doubleValue() > 0.0 ? paymentShortage.toString() : "0.00";
    }

    public String getChangeDue() {
        return paymentShortage.doubleValue() < 0.0 ? paymentShortage.abs().toString() : "0.00";
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public OrderAdapter setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public OrderAdapter setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        return this;
    }

    public String getLastModifiedDate() {
        return OffsetDateTime.parse(lastModifiedTimestamp).toLocalDate().toString();
    }

    public String getLastModifiedTime() {
        // Pattern: 2020-02-10T07:35:04.579+0000
        return OffsetDateTime.parse(lastModifiedTimestamp).toLocalTime().toString();
    }

    public boolean isOrderFullyPaid() {
        return total.compareTo(totalPayments) < 1;
    }

    public List<OrderCoupon> getCouponList() {
        return couponList;
    }

    public OrderAdapter setCouponList(List<OrderCoupon> couponList) {
        this.couponList = couponList;
        return this;
    }

    public BigDecimal getCouponTotal() {
        return couponTotal;
    }

    public OrderAdapter setCouponTotal(BigDecimal couponTotal) {
        this.couponTotal = couponTotal;
        return this;
    }

    public OrderCustomer getOrderCustomer() {
        return customer;
    }

    public OrderAdapter setOrderCustomer(OrderCustomer customer) {
        this.customer = customer;
        return this;
    }

    private int getTotalCustomerCount() {
        return (customer == null) ? 0 : 1;
    }

    public BigDecimal getTotalPriceModifications() {
        return totalPriceModifications.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public OrderAdapter setTotalPriceModifications(BigDecimal totalPriceModifications) {
        this.totalPriceModifications = totalPriceModifications;
        return this;
    }
}
