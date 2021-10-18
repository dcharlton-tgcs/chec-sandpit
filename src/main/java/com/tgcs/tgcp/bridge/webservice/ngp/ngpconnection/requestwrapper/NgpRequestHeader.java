package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestwrapper;

public enum NgpRequestHeader {

    AUTHORIZATION("Authorization");

    private String value;

    NgpRequestHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
