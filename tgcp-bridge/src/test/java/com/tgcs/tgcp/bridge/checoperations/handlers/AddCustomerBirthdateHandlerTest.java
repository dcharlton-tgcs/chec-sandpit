package com.tgcs.tgcp.bridge.checoperations.handlers;


import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.addcustomer.AddCustomerBirthdateResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AddCustomerBirthdateHandler.class})
public class AddCustomerBirthdateHandlerTest {

    @Autowired
    private AddCustomerBirthdateHandler addCustomerBirthdateHandler;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private XmlDocument xmlDocument;

    @MockBean
    private BridgeSession bridgeSession;


    @Captor
    ArgumentCaptor argumentCaptor;

    @Test
    public void testSendProcessChecMessage() {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_CUSTOMER_BIRTHDATE_REQUEST_ID, "")).thenReturn("11");
        addCustomerBirthdateHandler.setBridgeSession(bridgeSession);

        addCustomerBirthdateHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertEquals("11", ((AddCustomerBirthdateResponse) argumentCaptor.getValue()).getAddCustomerBirthdateResult().getRequestId());
    }

}