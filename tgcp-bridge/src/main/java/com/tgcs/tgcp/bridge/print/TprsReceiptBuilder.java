package com.tgcs.tgcp.bridge.print;

import com.tgcs.posbc.bridge.printer.model.BarcodeData;
import com.tgcs.posbc.bridge.printer.model.Receipt;
import com.tgcs.posbc.bridge.printer.model.ReceiptData;
import com.tgcs.posbc.bridge.printer.model.ReceiptContent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TprsReceiptBuilder {
    private static int BARCODE_SYMBOLOGY_INDEX = 1;
    private static int BARCODE_HEIGHT_INDEX = 2;
    private static int BARCODE_WIDTH_INDEX = 3;

    public static Receipt buildReceipt(String fullReceiptMessage) {
        if (fullReceiptMessage == null || fullReceiptMessage.trim().length() == 0) return null;

        String[] lines = fullReceiptMessage.split("\r\n");
        List<String> allLines = Arrays.asList(lines);

        Optional<String> barcodeData = allLines.stream()
                .filter(e -> e.toLowerCase().contains("x{bar"))
                .findFirst();
        List<String> receiptLines = allLines.stream()
                .filter(e -> !e.toLowerCase().contains("x{bar"))
                .collect(Collectors.toList());

        return new ReceiptData()
                .setBarcodeData(createBarcodeData(barcodeData.orElse("")))
                .setContent(createReceiptContent(receiptLines));
    }

    static ReceiptContent createReceiptContent(List<String> receiptLines) {
        return new ReceiptContent().setReceiptLines(receiptLines);
    }

    static BarcodeData createBarcodeData(String barcodeData) {
        if(barcodeData == null || barcodeData.trim().length() == 0) return null;

        String barcodeDescription = barcodeData.substring(barcodeData.indexOf("{"), barcodeData.indexOf("}"));
        String[] barData = barcodeDescription.split(",");
        String barcode = barcodeData.substring(barcodeData.indexOf("}") + 1);

        return new BarcodeData().setBarcode(barcode)
                .setSymbology(barData[BARCODE_SYMBOLOGY_INDEX])
                .setWidth(barData[BARCODE_HEIGHT_INDEX])
                .setHeight(barData[BARCODE_WIDTH_INDEX]);
    }


}
