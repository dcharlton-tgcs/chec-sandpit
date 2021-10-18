package com.tgcs.tgcp.bridge.checoperations.model.receipt;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TextReceiptLine")
public class TextReceiptLine {

    @XmlElement(name = "Bold")
    private Boolean bold;

    @XmlElement(name = "Text")
    private String text = "";

    public TextReceiptLine() {
    }

    public TextReceiptLine(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getBold() {
        return bold;
    }

    public TextReceiptLine setBold(Boolean bold) {
        this.bold = bold;
        return this;
    }
}
