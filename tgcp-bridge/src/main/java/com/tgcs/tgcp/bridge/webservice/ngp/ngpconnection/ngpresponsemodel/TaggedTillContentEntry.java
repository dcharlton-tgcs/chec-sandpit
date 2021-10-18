package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngpresponsemodel;

public class TaggedTillContentEntry {

    TillContentEntry entry;
    String entryTag;

    public TillContentEntry getEntry() {
        return entry;
    }

    public TaggedTillContentEntry setEntry(TillContentEntry entry) {
        this.entry = entry;
        return this;
    }

    public String getEntryTag() {
        return entryTag;
    }

    public TaggedTillContentEntry setEntryTag(String entryTag) {
        this.entryTag = entryTag;
        return this;
    }
}
