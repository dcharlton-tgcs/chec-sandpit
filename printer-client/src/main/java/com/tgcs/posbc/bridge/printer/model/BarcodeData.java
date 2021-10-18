package com.tgcs.posbc.bridge.printer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BarcodeData {

    @XmlElement(name = "barcode")
    private String barcode;

    @XmlElement(name = "symbology")
    private String symbology;

    @XmlElement(name = "width")
    private String width;

    @XmlElement(name = "height")
    private String height;

    public String getBarcode() {
        return barcode;
    }

    public BarcodeData setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getSymbology() {
        return symbology;
    }

    public BarcodeData setSymbology(String symbology) {
        this.symbology = symbology;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public BarcodeData setWidth(String width) {
        this.width = width;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public BarcodeData setHeight(String height) {
        this.height = height;
        return this;
    }
}
