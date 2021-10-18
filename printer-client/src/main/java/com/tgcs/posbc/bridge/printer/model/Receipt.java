package com.tgcs.posbc.bridge.printer.model;

public interface Receipt {

    ReceiptContent getContent();

    Receipt setContent(ReceiptContent content);

    BarcodeData getBarcodeData();

    Receipt setBarcodeData(BarcodeData barcodeData);
}
