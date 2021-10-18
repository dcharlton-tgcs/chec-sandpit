package com.tgcs.tgcp.bridge.checoperations.model.addtender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MiscInfo")
public class MiscInfo {
    @XmlElement(name = "Description")
    private String description;

    @XmlElement(name = "IsVoid")
    private boolean voidFlag;

    @XmlElement(name = "Amount")
    private String amount;

    @XmlElement(name = "BalanceDueSatisfied")
    private boolean balanceDueSatisfied = true;

    public String getDescription() {
        return description;
    }

    public MiscInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isVoidFlag() {
        return voidFlag;
    }

    public MiscInfo setVoidFlag(boolean voidFlag) {
        this.voidFlag = voidFlag;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public MiscInfo setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public boolean isBalanceDueSatisfied() {
        return balanceDueSatisfied;
    }

    public MiscInfo setBalanceDueSatisfied(boolean balanceDueSatisfied) {
        this.balanceDueSatisfied = balanceDueSatisfied;
        return this;
    }
}
