package com.tgcs.tgcp.bridge.checoperations.model.addtender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddTenderResult")
public class AddTenderResult {

    @XmlElement(name = "ExceptionResult")
    private com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult ExceptionResult;

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "CashInfo")
    private CashInfo cashInfo;

    @XmlElement(name = "CreditInfo")
    private CreditInfo creditInfo;

    @XmlElement(name = "DebitInfo")
    private DebitInfo debitInfo;

    @XmlElement(name = "MiscInfo")
    private MiscInfo miscInfo;

    @XmlElement(name = "SignatureNeeded")
    private boolean signatureNeeded = false;

    @XmlElement(name = "SignatureIndex")
    private boolean signatureIndex;

    public String getRequestId() {
        return requestId;
    }

    public AddTenderResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public CashInfo getCashInfo() {
        return cashInfo;
    }

    public AddTenderResult setCashInfo(CashInfo cashInfo) {
        this.cashInfo = cashInfo;
        return this;
    }

    public CreditInfo getCreditInfo() {
        return creditInfo;
    }

    public AddTenderResult setCreditInfo(CreditInfo creditInfo) {
        this.creditInfo = creditInfo;
        return this;
    }

    public DebitInfo getDebitInfo() {
        return debitInfo;
    }

    public AddTenderResult setDebitInfo(DebitInfo debitInfo) {
        this.debitInfo = debitInfo;
        return this;
    }

    public MiscInfo getMiscInfo() {
        return miscInfo;
    }

    public void setMiscInfo(MiscInfo miscInfo) {
        this.miscInfo = miscInfo;
    }

    public boolean isSignatureNeeded() {
        return signatureNeeded;
    }

    public AddTenderResult setSignatureNeeded(boolean signatureNeeded) {
        this.signatureNeeded = signatureNeeded;
        return this;
    }

    public boolean isSignatureIndex() {
        return signatureIndex;
    }

    public AddTenderResult setSignatureIndex(boolean signatureIndex) {
        this.signatureIndex = signatureIndex;
        return this;
    }

    public com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult getExceptionResult() {
        return ExceptionResult;
    }

    public AddTenderResult setExceptionResult(com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult exceptionResult) {
        ExceptionResult = exceptionResult;
        return this;
    }
}
