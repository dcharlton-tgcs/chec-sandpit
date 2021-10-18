package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the information about the electronic receipt lines
 */
public class EReceiptLineTrack {

    private List<EReceiptLine> receiptLines = new ArrayList<>();
    private boolean headerPrinted = false;

    public EReceiptLine getLineInfoByLineContent(String lineContent) {
        return receiptLines.stream()
                .filter(e -> e.getLineContent().equalsIgnoreCase(lineContent))
                .findFirst()
                .orElse(null);
    }

    public EReceiptLine getLineInfoByLineId(String lineId) {
        return receiptLines.stream()
                .filter(e -> e.getLineId().equalsIgnoreCase(lineId))
                .findFirst()
                .orElse(null);
    }

    public void putIfAbsent(EReceiptLine receiptLine){
        if (!receiptLines.stream().anyMatch(e -> e.equals(receiptLine))) {
            receiptLines.add(receiptLine);
        }
    }

    public void put(EReceiptLine receiptLine){
            receiptLines.add(receiptLine);
    }

    public boolean containsContent(String lineContent){
        return getLineInfoByLineContent(lineContent) != null;
    }

    public void updateLineIdForContent(String lineContent, String newLineId){
        getLineInfoByLineContent(lineContent).setLineId(newLineId);
    }

    public void removeLineByContent(String lineContent){
        receiptLines.remove(getLineInfoByLineContent(lineContent));
    }

    public List<EReceiptLine> getReceiptLines() {
        return receiptLines;
    }

    public boolean isHeaderPrinted() {
        return headerPrinted;
    }

    public EReceiptLineTrack setHeaderPrinted(boolean headerPrinted) {
        this.headerPrinted = headerPrinted;
        return this;
    }
}
