package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.util.Map;

public class ReceiptMessage {

    /**
     * message (optional)
     */
    Map<String, String> message;

    /**
     * barcodeData (optional)
     */
    String barcodeData;

    /**
     * symbology (optional)
     */
    String symbology;

    public Map<String, String> getMessage() {
        return message;
    }

    public ReceiptMessage setMessage(Map<String, String> message) {
        this.message = message;
        return this;
    }

    public String getBarcodeData() {
        return barcodeData;
    }

    public ReceiptMessage setBarcodeData(String barcodeData) {
        this.barcodeData = barcodeData;
        return this;
    }

    public String getSymbology() {
        return symbology;
    }

    public ReceiptMessage setSymbology(String symbology) {
        this.symbology = symbology;
        return this;
    }
}
