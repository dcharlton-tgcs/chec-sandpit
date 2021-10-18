package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.CustomerName;
import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderCustomer;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.addcustomer.AddCustomerResponse;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.EReceiptLineTrack;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.POSReceiptEvent;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.ReceiptSection;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.exception.WebServiceException;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AddCustomerHandler.class})
@SpringBootTest(classes = {ConfProperties.class})
public class AddCustomerHandlerTest {

    @Autowired
    AddCustomerHandler addCustomerHandler;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private XmlDocument xmlDocument;

    @MockBean
    private BridgeSession bridgeSession;

    @MockBean
    private IWebService webService;

    @MockBean
    private ExceptionHandler exceptionHandler;

    @Captor
    ArgumentCaptor argumentCaptor;

    @MockBean
    OrderCustomer orderCustomer;

    @MockBean
    CustomerName customerName;

    @MockBean
    private OrderAdapter orderAdapter;

    @SpyBean
    ConfProperties confProperties;

    @MockBean
    EReceiptLineTrack eReceiptLineTrack;

    @Before
    public void setUp() {
        addCustomerHandler.setBridgeSession(bridgeSession);
        doReturn("John").when(customerName).getFirstName();
        doReturn("Doe").when(customerName).getLastName();
        doReturn("1000000234567890").when(orderCustomer).getLoyaltyCard();
        doReturn(customerName).when(orderCustomer).getCustomerName();
        doReturn(orderCustomer).when(orderAdapter).getOrderCustomer();
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_CUSTOMER_REQUEST_ID, "")).thenReturn("666");
        when(bridgeSession.getOrderAdapter()).thenReturn(orderAdapter);
        when(bridgeSession.getOrderAdapter().getOrderCustomer()).thenReturn(null).thenReturn(orderCustomer);
        when(bridgeSession.getReceiptItemsCache()).thenReturn(eReceiptLineTrack);

        ReflectionTestUtils.setField(confProperties, "loyaltyCardFirstScanOnly", true);
        ReflectionTestUtils.setField(confProperties, "loyaltyCardIgnoreAdditionalScans", true);

    }

    @Test
    public void processChecMessageFirstCustomerTest() throws Exception {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_CUSTOMER_CUSTOMER_ID, "")).thenReturn("");
        when(bridgeSession.getReceiptItemsCache().isHeaderPrinted()).thenReturn(false);

        addCustomerHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(4)).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).addCustomer(any(OrderCustomer.class));

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEventHeader = (POSReceiptEvent) events.get(1);
        assertEquals(ReceiptSection.HEADER.getValue(), posReceiptEventHeader.getSection());

        POSReceiptEvent posReceiptEventCustomer = (POSReceiptEvent) events.get(2);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventCustomer.getSection());

        AddCustomerResponse addCustomerResponse = (AddCustomerResponse) events.get(3);
        assertNotNull(addCustomerResponse.getAddCustomerResult().getCustomerInfo());

    }

    @Test
    public void processChecMessageWebServiceTimeoutTest() throws Exception {
        ChecOperationException checOperationException = new ChecOperationException(
                WebServiceException.TIMEOUT, WebServiceException.TIMEOUT, new Throwable());

        doThrow(Exception.class).when(webService).addCustomer(any());
        when(exceptionHandler.retrieveWebServiceError(any())).thenReturn(checOperationException);

        addCustomerHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(2)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSBCStatusEvent posbcStatusEvent = (POSBCStatusEvent) events.get(0);
        assertEquals(POSBCStatusType.POS_CONNECTION_LOST.toString(), posbcStatusEvent.getPosbcStatus().getStatus());

        AddCustomerResponse addCustomerResponse = (AddCustomerResponse) events.get(1);
        assertNotNull(addCustomerResponse.getAddCustomerResult().getExceptionResult());
        assertEquals(checOperationException.getErrorCode(), addCustomerResponse.getAddCustomerResult().getExceptionResult().getErrorCode());
        assertEquals(checOperationException.getErrorMessage(), addCustomerResponse.getAddCustomerResult().getExceptionResult().getMessage());

    }

    @Test
    public void testRejectLoyaltyCard() {
        when(bridgeSession.getOrderAdapter()).thenReturn(orderAdapter);
        when(bridgeSession.getOrderAdapter().getItemList()).thenReturn(Arrays.asList(new OrderItem()));

        addCustomerHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        AddCustomerResponse addCustomerResponse = (AddCustomerResponse) argumentCaptor.getValue();
        assertNotNull(addCustomerResponse.getAddCustomerResult().getExceptionResult());
        assertEquals("LOYALTY_AFTER_ITEM", addCustomerResponse.getAddCustomerResult().getExceptionResult().getErrorCode());
        assertEquals("Loyalty card can not be scanned at this time", addCustomerResponse.getAddCustomerResult().getExceptionResult().getMessage());

    }

    @Test
    public void testLoyaltyCardAlreadyScanned() {
        when(bridgeSession.getOrderAdapter()).thenReturn(orderAdapter);
        when(bridgeSession.getOrderAdapter().getOrderCustomer()).thenReturn(new OrderCustomer());

        addCustomerHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        AddCustomerResponse addCustomerResponse = (AddCustomerResponse) argumentCaptor.getValue();
        assertNotNull(addCustomerResponse.getAddCustomerResult().getExceptionResult());
        assertEquals("LOYALTY_ALREADY_SCANNED", addCustomerResponse.getAddCustomerResult().getExceptionResult().getErrorCode());
        assertEquals("Loyalty card already scanned", addCustomerResponse.getAddCustomerResult().getExceptionResult().getMessage());

    }

    @Test
    public void testLoyaltyCardFirstItemOnlyDisabled() throws Exception {
        ReflectionTestUtils.setField(confProperties, "loyaltyCardFirstScanOnly", false);

        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_CUSTOMER_CUSTOMER_ID, "")).thenReturn("");
        when(bridgeSession.getReceiptItemsCache().isHeaderPrinted()).thenReturn(true);

        addCustomerHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(2)).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).addCustomer(any(OrderCustomer.class));

        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEventCustomer = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventCustomer.getSection());

        AddCustomerResponse addCustomerResponse = (AddCustomerResponse) events.get(1);
        assertNotNull(addCustomerResponse.getAddCustomerResult().getCustomerInfo());

    }

    @Test
    public void testLoyaltyCardIgnoreAdditionalScansDisabled() throws Exception {
        ReflectionTestUtils.setField(confProperties, "loyaltyCardIgnoreAdditionalScans", false);

        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_CUSTOMER_CUSTOMER_ID, "")).thenReturn("");
        when(bridgeSession.getReceiptItemsCache().isHeaderPrinted()).thenReturn(false);
        when(bridgeSession.getOrderAdapter().getOrderCustomer()).thenReturn(orderCustomer);

        addCustomerHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(4)).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).addCustomer(any(OrderCustomer.class));

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEventHeader = (POSReceiptEvent) events.get(1);
        assertEquals(ReceiptSection.HEADER.getValue(), posReceiptEventHeader.getSection());

        POSReceiptEvent posReceiptEventCustomer = (POSReceiptEvent) events.get(2);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventCustomer.getSection());

        AddCustomerResponse addCustomerResponse = (AddCustomerResponse) events.get(3);
        assertNotNull(addCustomerResponse.getAddCustomerResult().getCustomerInfo());
    }
}
