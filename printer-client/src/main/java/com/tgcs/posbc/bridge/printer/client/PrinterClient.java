package com.tgcs.posbc.bridge.printer.client;

import com.tgcs.posbc.bridge.printer.config.PrinterConfiguration;
import com.tgcs.posbc.bridge.printer.model.Receipt;

public interface PrinterClient {

    void startPrinterClient(PrinterConfiguration printerConfig);

    void addEventListener(PrinterEventListener listener);

    void removeEventListener(PrinterEventListener listener);

    boolean printReceipt(Receipt receiptData);

    void closeConnectionIfOpen();

}
