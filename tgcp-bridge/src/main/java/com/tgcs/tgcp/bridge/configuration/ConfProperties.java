package com.tgcs.tgcp.bridge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfProperties {

    @Value("${tcp-server.address}")
    private String host;

    @Value("${tcp-server.port}")
    private int port;

    @Value("${context.deviceId}")
    private String deviceId;

    @Value("${context.endpointId}")
    private String endpointId;

    @Value("${context.nodeId}")
    private String nodeId;

    @Value("${pos.service.type}")
    private String posType;

    @Value("${cash-management.enabled}")
    private boolean cashManagementEnabled;

    @Value("${web-service.username}")
    private String username;

    @Value("${web-service.password}")
    private String password;

    @Value("${pos.handle.eft}")
    private boolean posHandleEft;

    @Value("${pos.printer.handle}")
    private boolean posHandlePrinter;

    @Value("${loyalty-card.first.item.only.enabled}")
    private boolean loyaltyCardFirstScanOnly;

    @Value("${loyalty-card.ignore.additional.scans.enabled}")
    private boolean loyaltyCardIgnoreAdditionalScans;

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getPosType() {
        return posType;
    }

    public boolean isCashManagementEnabled() {
        return cashManagementEnabled;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getPosHandleEft() {
        return posHandleEft;
    }

    public boolean isPosHandlePrinter() {
        return posHandlePrinter;
    }

    public boolean isLoyaltyCardFirstScanOnly() {
        return loyaltyCardFirstScanOnly;
    }

    public boolean isLoyaltyCardIgnoreAdditionalScans() {
        return loyaltyCardIgnoreAdditionalScans;
    }
}
