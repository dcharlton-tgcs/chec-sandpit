package com.tgcs.tgcp.bridge.checoperations.model;

public interface ResponseType {

    enum ResponseTypeValues {
        RESPONSE("RESP"),
        EVENT("EVENT"),
        REQUEST("REQ");

        ResponseTypeValues(String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }
    }

    String getMessageType();
}
