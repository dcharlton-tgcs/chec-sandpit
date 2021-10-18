package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddReceiptLines", namespace = "http://bc.si.retail.ibm.com/POSBCSchema")
public class AddReceiptLines {

    @XmlElement(name = "AddReceiptLinesRequest")
    private AddReceiptLinesRequest addReceiptLinesRequest;

    public AddReceiptLinesRequest getAddReceiptLinesRequest() {
        return addReceiptLinesRequest;
    }

    public AddReceiptLines setAddReceiptLinesRequest(AddReceiptLinesRequest addReceiptLinesRequest) {
        this.addReceiptLinesRequest = addReceiptLinesRequest;
        return this;
    }
}