package com.tgcs.tgcp.bridge.checoperations.model.status;

public enum POSBCStatusType {

    RELEASING_SESSION_RESOURCES("Releasing Session Resources"),
    CONNECTING_TO_POS("Connecting to POS system"),
    CONNECTED_TO_POS("Connected to POS system"),
    POS_RESOURCES_INITIALIZED("POS Resources Initialized"),
    POS_CONNECTION_LOST("POS_CONNECTION_LOST"),

    ONLINE("Online"),

    INITIALIZED("Printer Initialized"),

    SEVERITY_INFO("INFO"),
    SEVERITY_ERROR("ERROR");

    private String value;

    private POSBCStatusType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
