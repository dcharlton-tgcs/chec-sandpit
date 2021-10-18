package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.authorization.model.LoginRequest;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.TPNETLoginRequest;

public class LoginRequestBuilder {

    public static TPNETLoginRequest buildTPNET(ConfProperties properties)
    {
        return new TPNETLoginRequest()
                .username(properties.getUsername())
                .password(properties.getPassword())
                .context(ContextBuilder.build(properties));
    }

    public static LoginRequest buildNGP(ConfProperties properties)
    {
        return new LoginRequest()
                .username(properties.getUsername())
                .password(properties.getPassword());
    }


}
