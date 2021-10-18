package com.tgcs.tgcp.bridge.checoperations.model.addtender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CashInfo")
public class CashInfo {

    @XmlElement(name = "ReturnFlag")
    private boolean returnFlag;

    @XmlElement(name = "DepositFlag")
    private boolean depositFlag = false;

    @XmlElement(name = "VoidFlag")
    private boolean voidFlag;

    @XmlElement(name = "Amount")
    private String amount;

    @XmlElement(name = "LineItemType")
    private String lineItemType = "Cash";

    @XmlElement(name = "BalanceDueSatisfied")
    private boolean balanceDueSatisfied;

    @XmlElement(name = "Change")
    private String Change;

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public CashInfo setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
        return this;
    }

    public boolean isDepositFlag() {
        return depositFlag;
    }

    public CashInfo setDepositFlag(boolean depositFlag) {
        this.depositFlag = depositFlag;
        return this;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public CashInfo setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public CashInfo setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getLineItemType() {
        return lineItemType;
    }

    public CashInfo setLineItemType(String lineItemType) {
        this.lineItemType = lineItemType;
        return this;
    }

    public boolean isBalanceDueSatisfied() {
        return balanceDueSatisfied;
    }

    public CashInfo setBalanceDueSatisfied(boolean balanceDueSatisfied) {
        this.balanceDueSatisfied = balanceDueSatisfied;
        return this;
    }

    public String getChange() {
        return Change;
    }

    public CashInfo setChange(String change) {
        Change = change;
        return this;
    }
}
