package com.tgcs.tgcp.bridge.checoperations.handlers;


import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.additem.AddItemResponse;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.LineType;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.POSReceiptEvent;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.ReceiptSection;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.totals.TotalsEvent;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.util.TestSupport;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import com.tgcs.tgcp.bridge.webservice.ngp.NgpSession;
import com.tgcs.tgcp.bridge.webservice.ngp.NgpToChecConvertor;
import com.tgcs.tgcp.pos.model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AddItemHandler.class, XmlDocument.class, ExceptionHandler.class,
        BridgeSession.class})
public class AddItemHandlerTest_NoMocks {

    @Autowired
    AddItemHandler addItemHandler;

    @Autowired
    ExceptionHandler exceptionHandler;

    @Autowired
    private XmlDocument xmlMessage;

    @Autowired
    private BridgeSession bridgeSession;

    @MockBean
    private IWebService iWebService;

    @MockBean
    private NgpSession ngpSession;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private ConfProperties confProperties;

    @Captor
    ArgumentCaptor argumentCaptor;

    @Before
    public void setUp() {
        addItemHandler.setBridgeSession(bridgeSession);
    }

    @After
    public void clearSession() {
        bridgeSession.clearOrder();
    }


    @Test
    public void testProcessChecMessageDiscountedItem() throws Exception {

        xmlMessage.loadXml(TestSupport.getString("addItem/addItemRequestDiscountedItem.xml"));

        Order currentNGPOrder = TestSupport.getObject(Order.class, "addItem/OrderWithOneDiscount.json");
        OrderAdapter currentAdapterOrder = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);

        when(iWebService.addItem(any(OrderItem.class))).thenReturn(currentAdapterOrder);

        addItemHandler.processChecMessage(tcpClient, xmlMessage);

        verify(tcpClient, times(5)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEventHeader = (POSReceiptEvent) events.get(1);
        assertEquals(ReceiptSection.HEADER.getValue(), posReceiptEventHeader.getSection());

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(2);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());
        assertEquals(POSReceiptEvent.UpdateType.Add.toString(), posReceiptEventItem.getUpdateType());
        assertEquals(LineType.ITEM.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(1).getLineType());
        assertEquals(LineType.COUPON.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(2).getLineType());

        TotalsEvent totalsEvent = (TotalsEvent)  events.get(3);
        assertNotNull(totalsEvent.getTransactionTotals());
        assertEquals( "1", totalsEvent.getTransactionTotals().getTotalItems() );
        assertTrue(Float.parseFloat(totalsEvent.getTransactionTotals().getTotal()) > 0);
        assertTrue(Float.parseFloat(totalsEvent.getTransactionTotals().getTotalSavings()) < 0 );

        AddItemResponse addItemResponse = (AddItemResponse) events.get(4);
        assertNotNull(addItemResponse.getAddItemResult().getLineItemList());
        assertEquals("102", addItemResponse.getAddItemResult().getRequestId());
        assertEquals(xmlMessage.getSilentNodeTextContent(ChecMessagePath.ADD_ITEM_BARCODE_SCAN_DATA_LABEL), addItemResponse.getAddItemResult().getLineItemList().get(0).getItemIdentifier());
    }

    @Test
    public void testProcessChecMessageUpdateDiscountedItem() throws Exception {

        xmlMessage.loadXml(TestSupport.getString("addItem/addItemRequestDiscountedItem.xml"));

        Order previousNGPOrder = TestSupport.getObject(Order.class, "addItem/OrderWithOneDiscount.json");
        OrderAdapter previousAdapterOrder = NgpToChecConvertor.convertNgpOrderToChecOrder(previousNGPOrder);
        bridgeSession.setOrderAdapter(previousAdapterOrder);

        Order currentNGPOrder = TestSupport.getObject(Order.class, "addItem/OrderWithUpdatedDiscount.json");
        OrderAdapter currentAdapterOrder = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);

        when(iWebService.addItem(any(OrderItem.class))).thenReturn(currentAdapterOrder);

        addItemHandler.processChecMessage(tcpClient, xmlMessage);

        verify(tcpClient, times(6)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEventHeader = (POSReceiptEvent) events.get(1);
        assertEquals(ReceiptSection.HEADER.getValue(), posReceiptEventHeader.getSection());

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(2);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());
        assertEquals(POSReceiptEvent.UpdateType.Add.toString(), posReceiptEventItem.getUpdateType());
        assertEquals(LineType.ITEM.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(1).getLineType());
        assertEquals(LineType.COUPON.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(2).getLineType());

        posReceiptEventItem = (POSReceiptEvent) events.get(3);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());
        assertEquals(POSReceiptEvent.UpdateType.Modify.toString(), posReceiptEventItem.getUpdateType());
        assertEquals(LineType.ITEM.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(1).getLineType());
        assertEquals(LineType.COUPON.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(2).getLineType());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(4);
        assertNotNull(totalsEvent.getTransactionTotals());
        assertEquals( "2", totalsEvent.getTransactionTotals().getTotalItems() );
        assertEquals("8.24", totalsEvent.getTransactionTotals().getTotal());
        assertEquals("-2.74", totalsEvent.getTransactionTotals().getTotalSavings());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(5);
        assertNotNull(addItemResponse.getAddItemResult().getLineItemList());
        assertEquals("102", addItemResponse.getAddItemResult().getRequestId());
        assertEquals(xmlMessage.getSilentNodeTextContent(ChecMessagePath.ADD_ITEM_BARCODE_SCAN_DATA_LABEL), addItemResponse.getAddItemResult().getLineItemList().get(0).getItemIdentifier());
    }

    @Test
    public void testProcessChecMessageUpdateDiscountWeightedItem() throws Exception {
        xmlMessage.loadXml(TestSupport.getString("addItem/addItemRequestWeightedItem.xml"));

        Order previousNGPOrder = TestSupport.getObject(Order.class, "addItem/OrderWithWeightedItemNoDiscount.json");
        OrderAdapter previousAdapterOrder = NgpToChecConvertor.convertNgpOrderToChecOrder(previousNGPOrder);
        bridgeSession.setOrderAdapter(previousAdapterOrder);

        Order currentNGPOrder = TestSupport.getObject(Order.class, "addItem/OrderWithWeightedUpdatedDiscountedItem.json");
        OrderAdapter currentAdapterOrder = NgpToChecConvertor.convertNgpOrderToChecOrder(currentNGPOrder);

        when(iWebService.addItem(any(OrderItem.class))).thenReturn(currentAdapterOrder);

        addItemHandler.processChecMessage(tcpClient, xmlMessage);

        verify(tcpClient, times(5)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEventHeader = (POSReceiptEvent) events.get(1);
        assertEquals(ReceiptSection.HEADER.getValue(), posReceiptEventHeader.getSection());

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(2);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());
        assertEquals(POSReceiptEvent.UpdateType.Add.toString(), posReceiptEventItem.getUpdateType());
        assertEquals(LineType.ITEM.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(1).getLineType());
        assertEquals(LineType.COUPON.getValue(), posReceiptEventItem.getFormattedReceiptLineList().getReceiptLineList().get(2).getLineType());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(3);
        assertNotNull(totalsEvent.getTransactionTotals());
        assertEquals( "2", totalsEvent.getTransactionTotals().getTotalItems() );
        assertEquals("4.74", totalsEvent.getTransactionTotals().getTotal());
        assertEquals("-1.20", totalsEvent.getTransactionTotals().getTotalSavings());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(4);
        assertNotNull(addItemResponse.getAddItemResult().getLineItemList());
        assertEquals("102", addItemResponse.getAddItemResult().getRequestId());
        assertEquals(xmlMessage.getSilentNodeTextContent(ChecMessagePath.ADD_ITEM_BARCODE_SCAN_DATA_LABEL), addItemResponse.getAddItemResult().getLineItemList().get(0).getItemIdentifier());

    }

}
