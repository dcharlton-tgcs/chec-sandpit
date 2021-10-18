package com.tgcs.posbc.bridge.eft.wrapper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EftResponse {

    Integer resultCode;
    Integer amountPaid;
    String dspText;
    String cusTicket;
    String signTicket;

    public EftResponse() { }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getDspText() {
        return dspText;
    }

    public void setDspText(String dspText) {
        this.dspText = dspText;
    }

    public String getCusTicket() {
        return cusTicket;
    }

    public void setCusTicket(String cusTicket) {
        this.cusTicket = cusTicket;
    }

    public String getSignTicket() {
        return signTicket;
    }

    public void setSignTicket(String signTicket) {
        this.signTicket = signTicket;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }
}
