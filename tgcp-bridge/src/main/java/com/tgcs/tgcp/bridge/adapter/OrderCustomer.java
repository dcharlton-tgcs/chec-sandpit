package com.tgcs.tgcp.bridge.adapter;

public class OrderCustomer implements ChecAdapter {

    private String customerId;
    private CustomerName customerName;
    private String loyaltyCard;

    public String getCustomerId() {
        return customerId;
    }

    public OrderCustomer setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CustomerName getCustomerName() {
        return customerName;
    }

    public OrderCustomer setCustomerName(CustomerName customerName) {
        this.customerName = customerName;
        return this;
    }

    public String getLoyaltyCard() {
        return loyaltyCard;
    }

    public OrderCustomer setLoyaltyCard(String loyaltyCard) {
        this.loyaltyCard = loyaltyCard;
        return this;
    }
}
