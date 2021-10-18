package com.tgcs.posbc.bridge.printer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReceiptContent {

    @XmlElement(name = "line")
    private List<String> receiptLines = new ArrayList<>();

    public List<String> getReceiptLines() {
        return receiptLines;
    }

    public ReceiptContent setReceiptLines(List<String> receiptLines) {
        this.receiptLines = receiptLines;
        return this;
    }
}
