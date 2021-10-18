package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Context {

    @Value("${context.deviceId}")
    private String deviceId;

    @Value("${context.endpointId}")
    private String endpointId;

    @Value("${context.nodeId}")
    private String nodeId;

    private String requestId;

    public Context(String requestId) {
        this.requestId = requestId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Context setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public Context setEndpointId(String endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public Context setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }

    public Context setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
