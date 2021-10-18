package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddReceiptLinesRequest")
public class AddReceiptLinesRequest {

    @XmlElement(name = "FormattedReceiptLine")
    private FormattedReceiptLine formattedReceiptLine;

    public FormattedReceiptLine getFormattedReceiptLine() {
        return formattedReceiptLine;
    }

    public AddReceiptLinesRequest setFormattedReceiptLine(FormattedReceiptLine formattedReceiptLine) {
        this.formattedReceiptLine = formattedReceiptLine;
        return this;
    }
}