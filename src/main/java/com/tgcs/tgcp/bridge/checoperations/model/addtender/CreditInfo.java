package com.tgcs.tgcp.bridge.checoperations.model.addtender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CreditInfo")
public class CreditInfo {

    @XmlElement(name = "Description")
    private String description = "CREDIT";

    @XmlElement(name = "ReturnFlag")
    private boolean returnFlag;

    @XmlElement(name = "DepositFlag")
    private boolean depositFlag;

    @XmlElement(name = "VoidFlag")
    private boolean voidFlag;

    @XmlElement(name = "Amount")
    private String amount;

    @XmlElement(name = "LineItemType")
    private String lineItemType = "EPSEFT";

    @XmlElement(name = "BalanceDueSatisfied")
    private boolean balanceDueSatisfied = true;

    @XmlElement(name = "LegalText")
    private ArrayList<String> LegalText = new ArrayList<>();

    @XmlElement(name = "ReferenceNumber")
    private String referenceNumber;

    @XmlElement(name = "MaskedAccountNumber")
    private String maskedAccountNumber;

    @XmlElement(name = "ResponseCode")
    private int ResponseCode;

    @XmlElement(name = "ApprovalCode")
    private String approvalCode;

    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;

    @XmlElement(name = "CashBackAmount")
    private String cashBackAmount;

    @XmlElement(name = "IsDeclined")
    private boolean isDeclined;

    @XmlElement(name = "ResponseCodeDescriptor")
    private String responseCodeDescriptor;

    public String getDescription() {
        return description;
    }

    public CreditInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public CreditInfo setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
        return this;
    }

    public boolean isDepositFlag() {
        return depositFlag;
    }

    public CreditInfo setDepositFlag(boolean depositFlag) {
        this.depositFlag = depositFlag;
        return this;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public CreditInfo setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public CreditInfo setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getLineItemType() {
        return lineItemType;
    }

    public CreditInfo setLineItemType(String lineItemType) {
        this.lineItemType = lineItemType;
        return this;
    }

    public boolean isBalanceDueSatisfied() {
        return balanceDueSatisfied;
    }

    public CreditInfo setBalanceDueSatisfied(boolean balanceDueSatisfied) {
        this.balanceDueSatisfied = balanceDueSatisfied;
        return this;
    }

    public ArrayList<String> getLegalText() {
        return LegalText;
    }

    public CreditInfo setLegalText(ArrayList<String> legalText) {
        LegalText = legalText;
        return this;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public CreditInfo setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    public CreditInfo setMaskedAccountNumber(String maskedAccountNumber) {
        this.maskedAccountNumber = maskedAccountNumber;
        return this;
    }

    public int getResponseCode() {
        return ResponseCode;
    }

    public CreditInfo setResponseCode(int responseCode) {
        ResponseCode = responseCode;
        return this;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public CreditInfo setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
        return this;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public CreditInfo setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public String getCashBackAmount() {
        return cashBackAmount;
    }

    public CreditInfo setCashBackAmount(String cashBackAmount) {
        this.cashBackAmount = cashBackAmount;
        return this;
    }

    public boolean isDeclined() {
        return isDeclined;
    }

    public CreditInfo setDeclined(boolean declined) {
        isDeclined = declined;
        return this;
    }

    public String getResponseCodeDescriptor() {
        return responseCodeDescriptor;
    }

    public CreditInfo setResponseCodeDescriptor(String responseCodeDescriptor) {
        this.responseCodeDescriptor = responseCodeDescriptor;
        return this;
    }
}
