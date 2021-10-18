package com.tgcs.posbc.bridge.printer.client;

import com.tgcs.posbc.bridge.printer.model.PrinterEvent;

public interface PrinterEventListener {
    void onEvent(PrinterEvent p);
}
