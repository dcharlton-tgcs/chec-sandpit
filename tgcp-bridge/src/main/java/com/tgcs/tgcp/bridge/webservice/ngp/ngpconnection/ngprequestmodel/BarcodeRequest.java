package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.math.BigDecimal;

public class BarcodeRequest extends WebRequestBody {

    private BigDecimal quantity;

    private Context context;

    private String barcodeData;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BarcodeRequest setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public BarcodeRequest setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getBarcodeData() {
        return barcodeData;
    }

    public BarcodeRequest setBarcodeData(String barcodeData) {
        this.barcodeData = barcodeData;
        return this;
    }
}
