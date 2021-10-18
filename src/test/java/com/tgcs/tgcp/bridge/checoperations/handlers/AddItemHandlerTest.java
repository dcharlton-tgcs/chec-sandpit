package com.tgcs.tgcp.bridge.checoperations.handlers;


import com.tgcs.tgcp.bridge.adapter.ChildItem;
import com.tgcs.tgcp.bridge.adapter.Item;
import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.additem.AddItemResponse;
import com.tgcs.tgcp.bridge.checoperations.model.additem.AddItemResult;
import com.tgcs.tgcp.bridge.checoperations.model.additem.LineItem;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.EReceiptLine;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.EReceiptLineTrack;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.POSReceiptEvent;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.ReceiptSection;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.totals.TotalsEvent;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.exception.*;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import com.tgcs.tgcp.bridge.webservice.ngp.WsErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AddItemHandler.class})
public class AddItemHandlerTest {

    @Autowired
    private AddItemHandler addItemHandler;

    @MockBean
    private TcpClient tcpClient;

    @MockBean
    private XmlDocument xmlDocument;

    @MockBean
    private BridgeSession bridgeSession;

    @MockBean
    private IWebService webService;

    @SpyBean
    private ExceptionHandler exceptionHandler;

    @Captor
    private ArgumentCaptor argumentCaptor;

    @MockBean
    private OrderItem orderItem;

    @MockBean
    private ChildItem childItem;

    @MockBean
    private OrderAdapter orderAdapter;

    EReceiptLineTrack eReceiptLineTrack;

    @Before
    public void setUp() throws Exception {
        addItemHandler.setBridgeSession(bridgeSession);

        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_BARCODE_SCAN_DATA_LABEL, "")).thenReturn("100001");
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_REQUEST_ID, "")).thenReturn("10");

        when(orderItem.getId()).thenReturn("1");
        when(orderItem.getDescription()).thenReturn("test");
        when(orderItem.getExtendedPrice()).thenReturn(BigDecimal.valueOf(1));
        when(orderItem.getQuantity()).thenReturn(BigDecimal.valueOf(1));

        when(orderAdapter.getOrderId()).thenReturn("1");
        when(orderAdapter.getTotal()).thenReturn(BigDecimal.valueOf(1));
        when(orderAdapter.getSubtotal()).thenReturn(BigDecimal.valueOf(1));
        when(orderAdapter.getTotalTax()).thenReturn(BigDecimal.valueOf(1));
        when(orderAdapter.getPaymentShortage()).thenReturn(BigDecimal.valueOf(1));
        when(orderAdapter.getTotalPayments()).thenReturn(BigDecimal.valueOf(1));
        when(orderAdapter.getLastModifiedTimestamp()).thenReturn("2020-02-10T07:35:04.579+0000");
        when(orderAdapter.getCouponTotal()).thenReturn(BigDecimal.valueOf(-0.33));
        when(orderAdapter.getTotalItemCount()).thenReturn(BigDecimal.valueOf(1));
        when(orderAdapter.getTotalPriceModifications()).thenReturn(BigDecimal.valueOf(-6.66));

        when(webService.addItem(any(OrderItem.class))).thenReturn(orderAdapter);
        when(bridgeSession.getOrderAdapter()).thenReturn(orderAdapter);
        when(bridgeSession.getAddedItem(any(), any())).thenReturn(Optional.of(orderItem));

        eReceiptLineTrack = new EReceiptLineTrack();
        eReceiptLineTrack.put(new EReceiptLine("1", "1", new Item()));
        eReceiptLineTrack.setHeaderPrinted(true);

        when(bridgeSession.getReceiptItemsCache()).thenReturn(eReceiptLineTrack);

        when(orderItem.getParentOrderItemId()).thenReturn("");

    }

    @Test
    public void testProcessChecMessageFirstItem() throws Exception {
        eReceiptLineTrack.setHeaderPrinted(false);
//        when(bridgeSession.getReceiptItemsCache().containsContent(orderItem.getId())).thenReturn(true);

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(6)).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).addItem(any(OrderItem.class));

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEventHeader = (POSReceiptEvent) events.get(1);
        assertEquals(ReceiptSection.HEADER.getValue(), posReceiptEventHeader.getSection());

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(2);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());
        assertEquals(POSReceiptEvent.UpdateType.Remove.toString(), posReceiptEventItem.getUpdateType());

        posReceiptEventItem = (POSReceiptEvent) events.get(3);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());
        assertEquals(POSReceiptEvent.UpdateType.Add.toString(), posReceiptEventItem.getUpdateType());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(4);
        assertNotNull(totalsEvent.getTransactionTotals());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(5);
        assertNotNull(addItemResponse.getAddItemResult().getLineItemList());
    }

    @Test
    public void testProcessChecMessageSecondItem() throws Exception {
        when(orderItem.getId()).thenReturn("2");
        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());
        verify(webService).addItem(any(OrderItem.class));

        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("1", totalsEvent.getTransactionTotals().getTotal());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(2);
        assertNull(addItemResponse.getAddItemResult().getExceptionResult());
    }

    @Test
    public void testProcessChecMessageWeightItem() {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_SCALE_WEIGHT, "")).thenReturn("2.3");

        when(xmlDocument.checkPathExists(ChecMessagePath.ADD_ITEM_SCALE_WEIGHT)).thenReturn(true);
        when(orderItem.isWeightedItem()).thenReturn(true);
        when(orderItem.getQuantity()).thenReturn(BigDecimal.valueOf(2.3));

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("10", totalsEvent.getRequestId());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(2);
        assertEquals(1, addItemResponse.getAddItemResult().getLineItemList().size());
        assertEquals("2.3", addItemResponse.getAddItemResult().getLineItemList().get(0).getWeight());
    }

    @Test
    public void testProcessChecMessageQuantityItem() {
        when(orderItem.getId()).thenReturn("2");
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_QUANTITY, "")).thenReturn("3");

        when(xmlDocument.checkPathExists(ChecMessagePath.ADD_ITEM_QUANTITY)).thenReturn(true);
        when(orderItem.isWeightedItem()).thenReturn(false);
        when(orderItem.getQuantity()).thenReturn(BigDecimal.valueOf(3));

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("10", totalsEvent.getRequestId());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(2);
        assertEquals(1, addItemResponse.getAddItemResult().getLineItemList().size());
        assertEquals("3", addItemResponse.getAddItemResult().getLineItemList().get(0).getQuantity());
    }

    @Test
    public void testProcessChecMessageWeightItemWithoutWeight() throws Exception {
        ChecOperationException checOperationException = new ChecOperationException("INVALID_ARGUMENT:ITEM_WEIGHT_REQUIRED",
                "A weight is required for this item.", new Exception());

        doThrow(Exception.class).when(webService).addItem(any());
        doReturn(checOperationException).when(exceptionHandler).retrieveWebServiceError(any());

        addItemHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(checOperationException.getErrorCode(),
                ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
        assertEquals(checOperationException.getErrorMessage(), ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getMessage());
    }

    @Test
    public void testProcessChecMessageQuantityRequiredItemWithoutQuantity() throws Exception {
        WsErrorResponse ex = new WsErrorResponse();
        ex.errorType(ExceptionType.NGP_VALIDATION_ERROR.getValue());
        ex.error(ExceptionDescription.QUANTITY_REQUIRED.getValue());

        doThrow(ex).when(webService).addItem(any());

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(ErrorCode.ITEM_QUANTITY_REQUIRED.getValue(), ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
    }

    @Test
    public void testProcessChecMessageNotForSaleItem() throws Exception {
        WsErrorResponse ex = new WsErrorResponse();
        ex.errorType(ExceptionType.NGP_VALIDATION_ERROR.getValue());
        ex.error(ExceptionDescription.NOT_FOR_SALE.getValue());

        doThrow(ex).when(webService).addItem(any());

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(ErrorCode.ITEM_NOT_FOR_SALE.getValue(), ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
    }

    @Test
    public void testProcessChecMessageBarcodeNotFound() throws Exception {
        WsErrorResponse ex = new WsErrorResponse();
        ex.errorType(ExceptionType.GENERAL.getValue());
        ex.error(ExceptionDescription.BARCODE_NOT_FOUND.getValue());

        doThrow(ex).when(webService).addItem(any());

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(ErrorCode.ITEM_NOT_FOUND.getValue(), ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
    }

    @Test
    public void testProcessChecMessageKeyedItem() {
        when(orderItem.getId()).thenReturn("2");
        when(xmlDocument.checkPathExists(ChecMessagePath.ADD_ITEM_KEYED_ITEM_ID)).thenReturn(true);
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_KEYED_ITEM_ID, "")).thenReturn("100001");

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());
        assertNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
    }

    @Test
    public void testProcessChecMessageItemNotOnFile() throws Exception {
        ChecOperationException checOperationException = new ChecOperationException("ITEM_NOT_FOUND",
                "No match found for barcode. barcodeData: 100001 , symbology: SCAN_SDT_EAN13", new Exception());

        doThrow(Exception.class).when(webService).addItem(any(OrderItem.class));
        doReturn(checOperationException).when(exceptionHandler).retrieveWebServiceError(any());

        addItemHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(checOperationException.getErrorCode(),
                ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
        assertEquals(checOperationException.getErrorMessage(), ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getMessage());

    }

    @Test
    public void testProcessChecMessageWebServiceTimeout() throws Exception {
        ChecOperationException checOperationException = new ChecOperationException(
                WebServiceException.TIMEOUT, WebServiceException.TIMEOUT, new Throwable());


        doThrow(Exception.class).when(webService).addItem(any());
        doReturn(checOperationException).when(exceptionHandler).retrieveWebServiceError(any());

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(2)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSBCStatusEvent posbcStatusEvent = (POSBCStatusEvent) events.get(0);
        assertEquals(POSBCStatusType.POS_CONNECTION_LOST.toString(), posbcStatusEvent.getPosbcStatus().getStatus());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(1);
        assertNotNull(addItemResponse.getAddItemResult().getExceptionResult());
        assertEquals(checOperationException.getErrorCode(), addItemResponse.getAddItemResult().getExceptionResult().getErrorCode());
        assertEquals(checOperationException.getErrorMessage(), addItemResponse.getAddItemResult().getExceptionResult().getMessage());
    }

    @Test
    public void testProcessChecMessageLinkedItem() {
        when(orderItem.getId()).thenReturn("2");
        eReceiptLineTrack.setHeaderPrinted(false);
        when(orderItem.hasLinkedItems()).thenReturn(true);
        when(bridgeSession.getLinkedItem(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(orderItem);
        when(orderItem.getChildItemList()).thenReturn(Arrays.asList(childItem));
        when(orderItem.getExtendedPrice()).thenReturn(new BigDecimal(6.66).setScale(2, BigDecimal.ROUND_HALF_EVEN));

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(5)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        TransactionStatusEvent transactionStatusEvent = (TransactionStatusEvent) events.get(0);
        assertEquals(TransactionStatusType.TRANSACTION_START.getValue(), transactionStatusEvent.getTransactionStatus().getStatus());

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(1);
        assertEquals(ReceiptSection.HEADER.getValue(), posReceiptEventItem.getSection());

        posReceiptEventItem = (POSReceiptEvent) events.get(2);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(3);
        assertEquals("10", totalsEvent.getRequestId());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(4);
        assertEquals(2, addItemResponse.getAddItemResult().getLineItemList().size());
        assertEquals("1", addItemResponse.getAddItemResult().getLineItemList().get(0).getQuantity());

    }

    @Test
    public void testProcessChecMessageTwoLinkedItem() {
        when(bridgeSession.getLinkedItem(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(orderItem);
        when(orderItem.getId()).thenReturn("2");
        when(orderItem.getChildItemList()).thenReturn(Arrays.asList(childItem, childItem));
        when(orderItem.getExtendedPrice()).thenReturn(new BigDecimal(6.66).setScale(2, BigDecimal.ROUND_HALF_EVEN));

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("10", totalsEvent.getRequestId());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(2);
        assertEquals(3, addItemResponse.getAddItemResult().getLineItemList().size());
        assertEquals("1", addItemResponse.getAddItemResult().getLineItemList().get(0).getQuantity());
        assertEquals("1", addItemResponse.getAddItemResult().getLineItemList().get(1).getQuantity());

    }

    /**
     * Test the adding of an item that will not be found in the resulting order. If this happens an AddItemResult containing the
     * ITEM_NOT_FOUND exception is expected.
     *
     * @throws Exception
     */
    @Test
    public void testProcessChecMessageAddedItemNotFound() throws Exception {
        bridgeSession = new BridgeSession();
        addItemHandler.setBridgeSession(bridgeSession);

        OrderItem orderItem = new OrderItem();
        orderItem.setId("123");
        orderItem.setItemIdentifier("987654312");
        orderItem.setParentOrderItemId("");

        OrderAdapter orderAdapter = new OrderAdapter();
        orderAdapter.setItemList(Collections.singletonList(orderItem));
        when(webService.addItem(any(OrderItem.class))).thenReturn(orderAdapter);

        when(xmlDocument.checkPathExists(ChecMessagePath.ADD_ITEM_KEYED_ITEM_ID)).thenReturn(true);
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_KEYED_ITEM_ID, "")).thenReturn("100001");

        addItemHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals("ITEM_NOT_FOUND", ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
    }

    /**
     * Test the adding of an item that will be found in the resulting order.
     *
     * @throws Exception
     */
    @Test
    public void testProcessChecMessageAddedItemFound() throws Exception {
        bridgeSession = new BridgeSession();
        bridgeSession.getReceiptItemsCache().setHeaderPrinted(true);
        addItemHandler.setBridgeSession(bridgeSession);

        OrderItem orderItem = new OrderItem();
        orderItem.setId("123");
        orderItem.setItemIdentifier("987654312");
        orderItem.setQuantity(BigDecimal.TEN);
        orderItem.setExtendedPrice(BigDecimal.ONE);
        orderItem.setDescription("");
        orderItem.setParentOrderItemId("");
        orderItem.setChildItemList(Collections.emptyList());

        OrderAdapter orderAdapter = new OrderAdapter();
        BigDecimal z = BigDecimal.ZERO;
        orderAdapter.setTotal(z).setTotalPriceModifications(z).setSubtotal(z).setTotalTax(z).setTotalPayments(z).setPaymentShortage(z).setTotalItemCount(z).setCouponTotal(z).setCouponList(Collections.emptyList()).setLastModifiedTimestamp("2007-12-03T10:15:30+01:00");
        orderAdapter.setItemList(Collections.singletonList(orderItem));
        when(webService.addItem(any(OrderItem.class))).thenReturn(orderAdapter);

        when(xmlDocument.checkPathExists(ChecMessagePath.ADD_ITEM_KEYED_ITEM_ID)).thenReturn(true);
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_KEYED_ITEM_ID, "")).thenReturn("987654312");

        addItemHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        AddItemResult addItemResult = ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult();
        assertNull(addItemResult.getExceptionResult());

        LineItem object = addItemResult.getLineItemList().get(0);
        assertNotNull(object);
        assertEquals("987654312", object.getItemIdentifier());
    }

    @Test
    public void testProcessChecMessageVoidNormalItem() throws Exception {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_VOID_FLAG, "false")).thenReturn("true");
        when(orderItem.getItemIdentifier()).thenReturn("100001");
        when(bridgeSession.getItem(orderItem.getItemIdentifier())).thenReturn(orderItem);
//        when(bridgeSession.getReceiptItemsCache().containsContent(orderItem.getId())).thenReturn(true);

        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(orderItem).setVoidFlag(true);
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("1", totalsEvent.getTransactionTotals().getTotal());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(2);
        assertNull(addItemResponse.getAddItemResult().getExceptionResult());
    }

    @Test
    public void testProcessChecMessageVoidLinkedItem() throws Exception {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_VOID_FLAG, "false")).thenReturn("true");
        when(orderItem.hasLinkedItems()).thenReturn(true);
        when(bridgeSession.getLinkedItem(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(orderItem);
        when(orderItem.getChildItemList()).thenReturn(Arrays.asList(childItem));
        when(orderItem.getExtendedPrice()).thenReturn(new BigDecimal(6.66).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        when(orderItem.getItemIdentifier()).thenReturn("100001");
        when(bridgeSession.getItem(orderItem.getItemIdentifier())).thenReturn(orderItem);


        addItemHandler.processChecMessage(tcpClient, xmlDocument);

        verify(orderItem, times(2)).setVoidFlag(true);
        verify(tcpClient, times(3)).sendResponseToChecApp(argumentCaptor.capture());

        List events = argumentCaptor.getAllValues();

        POSReceiptEvent posReceiptEventItem = (POSReceiptEvent) events.get(0);
        assertEquals(ReceiptSection.BODY.getValue(), posReceiptEventItem.getSection());

        TotalsEvent totalsEvent = (TotalsEvent) events.get(1);
        assertEquals("1", totalsEvent.getTransactionTotals().getTotal());

        AddItemResponse addItemResponse = (AddItemResponse) events.get(2);
        assertNull(addItemResponse.getAddItemResult().getExceptionResult());
    }

    @Test
    public void testProcessChecMessageVoidItemNotFoundInCacheException() throws Exception {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_VOID_FLAG, "false")).thenReturn("true");

        ChecOperationException checOperationException = new ChecOperationException("VOID_ITEM_NOT_FOUND",
                "Item not in transaction", new Exception());

        addItemHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(checOperationException.getErrorCode(),
                ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
    }

    @Test
    public void testProcessChecMessageVoidItemNotFoundInOrderException() throws Exception {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_VOID_FLAG, "false")).thenReturn("true");
        when(orderItem.getItemIdentifier()).thenReturn("100001");
        when(bridgeSession.getItem(orderItem.getItemIdentifier())).thenReturn(orderItem);
//        when(bridgeSession.getReceiptItemsCache().containsContent(orderItem.getId())).thenReturn(true);

        ChecOperationException checOperationException = new ChecOperationException("VOID_ITEM_NOT_FOUND",
                "Item not in transaction", new Exception());

        VoidItemException voidItemException = new VoidItemException(ErrorCode.VOID_ITEM_NOT_FOUND.name(), ErrorCode.VOID_ITEM_NOT_FOUND.getValue());
        when(webService.voidItem(any())).thenThrow(voidItemException);

        addItemHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(checOperationException.getErrorCode(),
                ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());
    }

    @Test
    public void testProcessChecMessageVoidItemGenericException() throws Exception {
        when(xmlDocument.getOrDefaultNodeTextContent(ChecMessagePath.ADD_ITEM_VOID_FLAG, "false")).thenReturn("true");
        when(orderItem.getItemIdentifier()).thenReturn("100001");
        when(bridgeSession.getItem(orderItem.getItemIdentifier())).thenReturn(orderItem);
//        when(bridgeSession.getReceiptItemsCache().containsContent(orderItem.getId())).thenReturn(true);

        ChecOperationException checOperationException = new ChecOperationException("VOID_ITEM_FAILURE",
                "error", new Exception());

        Exception exception = new Exception();
        when(webService.voidItem(any())).thenThrow(exception);

        addItemHandler.processChecMessage(tcpClient, xmlDocument);
        verify(tcpClient).sendResponseToChecApp(argumentCaptor.capture());

        assertNotNull(((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult());
        assertEquals(checOperationException.getErrorCode(),
                ((AddItemResponse) argumentCaptor.getValue()).getAddItemResult().getExceptionResult().getErrorCode());

    }

}