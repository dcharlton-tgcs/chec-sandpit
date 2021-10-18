package com.tgcs.posbc.bridge.printer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "receipt")
public class ReceiptData implements Receipt{

    @XmlElement(name = "content")
    private ReceiptContent content;

    @XmlElement(name = "barcodeData")
    private BarcodeData barcodeData;

    public ReceiptContent getContent() {
        return content;
    }

    public Receipt setContent(ReceiptContent content) {
        this.content = content;
        return this;
    }

    public BarcodeData getBarcodeData() {
        return barcodeData;
    }

    public Receipt setBarcodeData(BarcodeData barcodeData) {
        this.barcodeData = barcodeData;
        return this;
    }
}
