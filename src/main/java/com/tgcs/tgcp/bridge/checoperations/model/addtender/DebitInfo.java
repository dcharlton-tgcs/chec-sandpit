package com.tgcs.tgcp.bridge.checoperations.model.addtender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DebitInfo")
public class DebitInfo {

    @XmlElement(name = "ReturnFlag")
    private boolean returnFlag;

    @XmlElement(name = "DepositFlag")
    private boolean depositFlag;

    @XmlElement(name = "VoidFlag")
    private boolean voidFlag;

    @XmlElement(name = "Amount")
    private String amount;

    @XmlElement(name = "LineItemType")
    private String lineItemType = "DEBIT";

    @XmlElement(name = "BalanceDueSatisfied")
    private boolean balanceDueSatisfied = true;

    @XmlElement(name = "Change")
    private String Change;

    @XmlElement(name = "LegalText")
    private ArrayList<String> legalText = new ArrayList<>();

    @XmlElement(name = "MaskedAccountNumber")
    private String maskedAccountNumber;

    public boolean isReturnFlag() {
        return returnFlag;
    }

    public DebitInfo setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
        return this;
    }

    public boolean isDepositFlag() {
        return depositFlag;
    }

    public DebitInfo setDepositFlag(boolean depositFlag) {
        this.depositFlag = depositFlag;
        return this;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public DebitInfo setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public DebitInfo setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getLineItemType() {
        return lineItemType;
    }

    public DebitInfo setLineItemType(String lineItemType) {
        this.lineItemType = lineItemType;
        return this;
    }

    public boolean isBalanceDueSatisfied() {
        return balanceDueSatisfied;
    }

    public DebitInfo setBalanceDueSatisfied(boolean balanceDueSatisfied) {
        this.balanceDueSatisfied = balanceDueSatisfied;
        return this;
    }

    public String getChange() {
        return Change;
    }

    public DebitInfo setChange(String change) {
        Change = change;
        return this;
    }

    public ArrayList<String> getLegalText() {
        legalText.addAll(legalTextValues);
        return legalText;
    }

    public DebitInfo setLegalText(ArrayList<String> legalText) {
        this.legalText = legalText;
        return this;
    }

    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    public DebitInfo setMaskedAccountNumber(String maskedAccountNumber) {
        String maskAccount = "************";
        this.maskedAccountNumber = maskAccount + maskedAccountNumber;
        return this;
    }

    static List<String> legalTextValues = Arrays.asList("____________________________________",
            "I agree to pay the above total amount",
            "according to the card issuer agreement");


}
