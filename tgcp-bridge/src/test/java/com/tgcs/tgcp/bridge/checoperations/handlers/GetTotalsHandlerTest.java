package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.totals.GetTotalsResponse;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.util.TestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GetTotalsHandler.class, XmlDocument.class})
@SpringBootTest(classes = {ConfProperties.class})
public class GetTotalsHandlerTest {

    @Autowired
    private GetTotalsHandler getTotalsHandler;

    @MockBean
    private TcpClient tcpClient;

    @Autowired
    private XmlDocument xmlMessage;


    BridgeSession bridgeSession = new BridgeSession();

    OrderAdapter orderAdapter = new OrderAdapter();


    @Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        xmlMessage.loadXml(TestSupport.getString("getTotals/getTotalsRequest.xml"));

        orderAdapter.setTotal(BigDecimal.valueOf(66.6));
        orderAdapter.setSubtotal(BigDecimal.valueOf(1));
        orderAdapter.setTotalTax(BigDecimal.valueOf(1));
        orderAdapter.setPaymentShortage(BigDecimal.valueOf(0.5));
        orderAdapter.setCouponTotal(BigDecimal.ZERO);
        orderAdapter.setTotalItemCount(BigDecimal.valueOf(1));
        orderAdapter.setCouponList(new ArrayList<>());
        orderAdapter.setTotalPriceModifications(BigDecimal.valueOf(-6.66));
        orderAdapter.setTotalPayments(BigDecimal.valueOf(1));

        bridgeSession.setOrderAdapter(orderAdapter);
        getTotalsHandler.setBridgeSession(bridgeSession);

    }

    @Test
    public void testProcessChecMessageGetTotalsResponse() {

        getTotalsHandler.processChecMessage(tcpClient, xmlMessage);

        verify(tcpClient).sendResponseToChecApp(any(GetTotalsResponse.class));
    }

    @Test
    public void testProcessChecMessageGetTotalsResponseValues() {

        GetTotalsResponse getTotalsResponse = getTotalsHandler.createGetTotalsResponse(orderAdapter, "104");

        assertEquals("66.6", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getTotal());
        assertEquals("1", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getSubtotal());
        assertEquals("1", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getTax());
        assertEquals("0.5", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getBalanceDue());
        assertEquals("0", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getCouponTotal());
        assertEquals("1", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getTotalItems());
        assertEquals("0", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getTotalCoupons());
        assertEquals("-6.66", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getTotalSavings());
        assertEquals("1", getTotalsResponse.getGetTotalsResult().getTransactionTotals().getTenderApplied());

    }

}
