package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.math.BigDecimal;

public class PaymentEntryData {

    /**
     * (optional)
     * The account number associated with this payment;
     * this could be a tokenized or encrypted value depending on the destination host processor's requirements
     */
    String accountNumber;

    /**
     * (optional)
     * The displayed portion of the payment's account number
     */
    String displayAccountNumber;

    /**
     * (optional)
     * Check number (check payments)
     */
    String checkNumber;

    /**
     * (optional)
     * The routing number (check payments)
     */
    String routingNumber;

    /**
     * (optional)
     * For &quot;card not present&quot; payments, the expiration date for the card
     */
    String expirationDate;

    /**
     * (optional)
     * For &quot;card not present&quot; payments, the security code from the card; this should be encrypted
     */
    String securityCode;

    /**
     * (optional)
     */
    //PaymentEntryData_trackData trackData

    /**
     * (optional)
     * For payments requiring PIN, the PIN data. This could contain the encrypted block from the PIN that the customer entered.
     */
    String pinData;

    /**
     * (optional)
     * The cash back amount for the customer from this payment
     */
    BigDecimal cashBackAmount;

    /**
     * (optional)
     */
    private CustomerName customerName;

    /**
     *  (optional)
     */
    //Address billingAddress;

    /**
     * (optional)
     * The ZIP or other postal code required to authorize this payment
     */
    String postalCode;

    /**
     * (optional)
     * A secondary ID required to complete the payment authorization
     */
    String secondaryId;

    /**
     * (optional)
     * The Payment type, for example CASH, AMEX, DEBIT
     */
    String type;

    /**
     * (optional)
     * PaymentTypeGroup object ???
     */
    String group;

    /**
     * (optional)
     */
    private String entryMethod;


    public String getAccountNumber() {
        return accountNumber;
    }

    public PaymentEntryData setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getDisplayAccountNumber() {
        return displayAccountNumber;
    }

    public PaymentEntryData setDisplayAccountNumber(String displayAccountNumber) {
        this.displayAccountNumber = displayAccountNumber;
        return this;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public PaymentEntryData setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
        return this;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public PaymentEntryData setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
        return this;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public PaymentEntryData setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public PaymentEntryData setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
        return this;
    }

    public String getPinData() {
        return pinData;
    }

    public PaymentEntryData setPinData(String pinData) {
        this.pinData = pinData;
        return this;
    }

    public BigDecimal getCashBackAmount() {
        return cashBackAmount;
    }

    public PaymentEntryData setCashBackAmount(BigDecimal cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public PaymentEntryData setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getSecondaryId() {
        return secondaryId;
    }

    public PaymentEntryData setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
        return this;
    }

    public String getType() {
        return type;
    }

    public PaymentEntryData setType(String type) {
        this.type = type;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public PaymentEntryData setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public PaymentEntryData setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
        return this;
    }

    public CustomerName getCustomerName() {
        return customerName;
    }

    public PaymentEntryData setCustomerName(CustomerName customerName) {
        this.customerName = customerName;
        return this;
    }
}
