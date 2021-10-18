package com.tgcs.tgcp.bridge.checoperations.model;

public interface NodeType {

    enum NodeTypeValues {
        RESPONSE("Response"),
        RESULT("Result");

        NodeTypeValues(String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }
    }

    String getNodeType();
}
