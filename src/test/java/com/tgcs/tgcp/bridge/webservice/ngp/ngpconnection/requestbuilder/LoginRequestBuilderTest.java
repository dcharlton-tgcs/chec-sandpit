package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.authorization.model.LoginRequest;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.TPNETLoginRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfProperties.class})
public class LoginRequestBuilderTest {

    @SpyBean
    ConfProperties propertiesSpy;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(propertiesSpy, "username", "1");
        ReflectionTestUtils.setField(propertiesSpy, "password", "1");
    }

    @Test
    public void buildTPNET() {
        TPNETLoginRequest tpnetLoginRequest = LoginRequestBuilder.buildTPNET(propertiesSpy);
        assertNotNull(tpnetLoginRequest);
        assertEquals(tpnetLoginRequest.getUsername(), "1");
        assertEquals(tpnetLoginRequest.getPassword(), "1");
        assertNotNull(tpnetLoginRequest.getContext());
    }

    @Test
    public void buildNGP() {
        LoginRequest loginRequest = LoginRequestBuilder.buildNGP(propertiesSpy);
        assertNotNull(loginRequest);
        assertEquals(loginRequest.getUsername(), "1");
        assertEquals(loginRequest.getPassword(), "1");
    }
}