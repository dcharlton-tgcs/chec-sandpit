package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import org.springframework.stereotype.Component;
/*
    One of username+password or password is required.
    If only password is provided, it is assumed to be a token/key based auth to the backing service.
 */

@Component
public class LoginRequest extends WebRequestBody {
    /**
     * Username (optional)
     */
    String username;

    /**
     * The password or token/key from backing auth/sso/login service format: password
     */
    String password;

    /**
     * newPassword to update to. Assumption is that client already validated this is the correct password.
     */
    String newPassword;

    /**
     * Node ID for the login request. By default it is SYSTEM, could be overridden with tgcp.authorization.default-node.
     */
    String nodeId;

    /**
     * Used for looking up nodes when running against a changeplan (optional).
     */
    String changePlanName;

    public String getUsername() {
        return username;
    }

    public LoginRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public LoginRequest setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public LoginRequest setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getChangePlanName() {
        return changePlanName;
    }

    public LoginRequest setChangePlanName(String changePlanName) {
        this.changePlanName = changePlanName;
        return this;
    }
}
