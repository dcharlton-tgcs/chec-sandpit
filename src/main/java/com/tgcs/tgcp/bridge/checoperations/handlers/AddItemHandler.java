package com.tgcs.tgcp.bridge.checoperations.handlers;

import com.tgcs.tgcp.bridge.adapter.ChildItem;
import com.tgcs.tgcp.bridge.adapter.OrderAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.checoperations.BridgeSession;
import com.tgcs.tgcp.bridge.checoperations.ChecOperationException;
import com.tgcs.tgcp.bridge.checoperations.helper.OrderHelper;
import com.tgcs.tgcp.bridge.checoperations.model.ChecMessagePath;
import com.tgcs.tgcp.bridge.checoperations.model.ErrorCode;
import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;
import com.tgcs.tgcp.bridge.checoperations.model.additem.AddItemResponse;
import com.tgcs.tgcp.bridge.checoperations.model.additem.AddItemResult;
import com.tgcs.tgcp.bridge.checoperations.model.additem.LineItem;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.EReceiptLine;
import com.tgcs.tgcp.bridge.checoperations.model.receipt.POSReceiptEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusEvent;
import com.tgcs.tgcp.bridge.checoperations.model.status.POSBCStatusType;
import com.tgcs.tgcp.bridge.checoperations.model.status.TransactionStatusType;
import com.tgcs.tgcp.bridge.common.XmlDocument;
import com.tgcs.tgcp.bridge.exception.ExceptionHandler;
import com.tgcs.tgcp.bridge.exception.VoidItemException;
import com.tgcs.tgcp.bridge.exception.WebServiceException;
import com.tgcs.tgcp.bridge.tcpserver.TcpClient;
import com.tgcs.tgcp.bridge.webservice.IWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class AddItemHandler implements IHandler, ChecMessagePath {
    private static Logger logger = LoggerFactory.getLogger(AddItemHandler.class);
    private BridgeSession bridgeSession;
    private IWebService webService;

    @Autowired
    private ExceptionHandler exceptionHandler;

    public AddItemHandler(IWebService webService) {
        this.webService = webService;
    }

    @Override
    public void setBridgeSession(BridgeSession bridgeSession) {
        this.bridgeSession = bridgeSession;
    }

    @Override
    public void processChecMessage(TcpClient client, XmlDocument message) {
        OrderItem orderItem = createOrderItemFromMessage(message);
        String requestId = message.getOrDefaultNodeTextContent(ADD_ITEM_REQUEST_ID, "");
        ArrayList<OrderItem> linkedItems = new ArrayList<>();
        try {
            if (orderItem.isVoidFlag()) {
                orderItem = bridgeSession.getItem(orderItem.getItemIdentifier());
                if (orderItem == null)
                    throw new ChecOperationException(ErrorCode.VOID_ITEM_NOT_FOUND.name(), ErrorCode.VOID_ITEM_NOT_FOUND.getValue(), null);

                orderItem.setVoidFlag(true);
                for (ChildItem e : orderItem.getChildItemList()) {
                    OrderItem linkedItem = bridgeSession.getLinkedItem(e.getId(), orderItem.getParentIdentificationNumber());
                    linkedItem.setVoidFlag(true);
                    linkedItems.add(linkedItem);
                }

                bridgeSession.setOrderAdapter(sendVoidItemRequest(orderItem));
                sendVoidItemEventsToConnectedApp(orderItem, client);
            } else {
                bridgeSession.setOrderAdapter(sendAddItemRequest(orderItem));
                orderItem = getOrderItem(orderItem);
                for (ChildItem e : orderItem.getChildItemList()) {
                    linkedItems.add(bridgeSession.getLinkedItem(e.getId(), orderItem.getParentIdentificationNumber()));
                }
                sendAddItemEventsToConnectedApp(orderItem, requestId, client);
            }
        } catch (ChecOperationException e) {
            // if we lost connection to the POS, we will send a status event to CHEC
            if (WebServiceException.TIMEOUT.equalsIgnoreCase(e.getErrorCode())) {
                POSBCStatusEvent posbcStatus = POSBCStatusEvent.createPOSBCStatus(POSBCStatusType.SEVERITY_ERROR.getValue(), POSBCStatusType.POS_CONNECTION_LOST.toString(), POSBCStatusType.POS_CONNECTION_LOST.getValue());
                client.sendResponseToChecApp(posbcStatus);
            }

            client.sendResponseToChecApp(createExceptionAddItemResponse(e, requestId));
            return;
        }

        List<OrderItem> modifiedItems = OrderHelper.compareStates(bridgeSession.getOrderAdapterPreviousState(), bridgeSession.getOrderAdapter());
        updateDiscountsReceiptLine(modifiedItems, client);

        // Send total event
        client.sendResponseToChecApp(Ngp2ChecMapper.createTotalEventFromOrder(bridgeSession.getOrderAdapter(), requestId));

        // Send add item response
        client.sendResponseToChecApp(createAddItemResponse(orderItem, linkedItems, requestId));
    }

    private OrderItem createOrderItemFromMessage(XmlDocument message) {
        OrderItem result = new OrderItem();
        String itemPath = ADD_ITEM_BARCODE_SCAN_DATA_LABEL;

        if (message.checkPathExists(ADD_ITEM_KEYED_ITEM_ID)) {
            itemPath = ADD_ITEM_KEYED_ITEM_ID;
            result.setItemEntryMethod(ITEM_ENTRY_METHOD_KEYED);
        }

        result.setItemIdentifier(message.getOrDefaultNodeTextContent(itemPath, ""));
        result.setScanDataType(message.getOrDefaultNodeTextContent(ADD_ITEM_BARCODE_SCAN_DATA_TYPE, ""));
        result.setVoidFlag(Boolean.parseBoolean(message.getOrDefaultNodeTextContent(ADD_ITEM_VOID_FLAG, "false")));

        // If the message received from CHEC contains the ScaleWeight path, set it in the object for NGP
        if (message.checkPathExists(ADD_ITEM_SCALE_WEIGHT)) {
            // CHEC handles quantity as an integer with 3 decimals, so we need to divide by 1000 by moving the decimal point.
            result.setQuantity(new BigDecimal(message.getOrDefaultNodeTextContent(ADD_ITEM_SCALE_WEIGHT, "")).movePointLeft(3));
        }

        if (message.checkPathExists(ADD_ITEM_QUANTITY)) {
            result.setQuantity(new BigDecimal(message.getOrDefaultNodeTextContent(ADD_ITEM_QUANTITY, "")));
        }

        //FIXME check for ItemIDIncludesCheckDigit?

        return result;
    }

    private OrderAdapter sendAddItemRequest(OrderItem orderItem) throws ChecOperationException {
        try {
            return webService.addItem(orderItem);
        } catch (Exception e) {
            logger.error("Failed to deal with AddItem");
            throw createChecOperationException(e);
        }
    }

    private OrderAdapter sendVoidItemRequest(OrderItem orderItem) throws ChecOperationException {
        try {
            return webService.voidItem(orderItem);
        } catch (VoidItemException ve) {
            throw new ChecOperationException(ve.getErrorCode(), ve.getErrorMessage(), ve);
        } catch (Exception e) {
            logger.error("Failed to void item");
            ChecOperationException ce = createChecOperationException(e);
            ce.setErrorCode(ErrorCode.VOID_ITEM_FAILURE.name());
            throw ce;
        }
    }

    protected void sendAddItemEventsToConnectedApp(OrderItem orderItem, String requestId, TcpClient client) {
        // If first item, Send POS Receipt Header Event and Transaction Start Event
        if (!bridgeSession.getReceiptItemsCache().isHeaderPrinted()) {
            client.sendResponseToChecApp(Ngp2ChecMapper.createTransactionStatusEvent(bridgeSession.getOrderAdapter(), requestId, TransactionStatusType.TRANSACTION_START.getValue()));
            client.sendResponseToChecApp(Ngp2ChecMapper.createPOSReceiptEventHeader(requestId));
            bridgeSession.getReceiptItemsCache().setHeaderPrinted(true);
        }

        // Send POS Receipt Item Event for every item except child linked items
        if (orderItem.getParentOrderItemId().isEmpty()) {
            //Check if item already in order, delete the old receipt li
            if (!orderItem.isWeightedItem() && bridgeSession.getReceiptItemsCache().containsContent(orderItem.getId())) {
                String reqId = bridgeSession.getReceiptItemsCache().getLineInfoByLineContent(orderItem.getId()).getLineId();
                client.sendResponseToChecApp(Ngp2ChecMapper.createRemovePOSReceiptEventItemLine(reqId));
                bridgeSession.getReceiptItemsCache().updateLineIdForContent(orderItem.getId(), requestId);
            }
            client.sendResponseToChecApp(Ngp2ChecMapper.createPOSReceiptEventItem(bridgeSession, requestId, orderItem));
        }
        bridgeSession.getReceiptItemsCache().putIfAbsent(new EReceiptLine(orderItem.getId(), requestId, orderItem));
    }

    protected void updateDiscountsReceiptLine(List<OrderItem> modifiedItems, TcpClient client) {
        // Send POS Receipt Item Event for every item except child linked items
        List<OrderItem> weightItems = modifiedItems.stream()
                .filter(e ->e.isWeightedItem())
                .collect(Collectors.toList());

        updateReceiptLineForWeightItems(weightItems, client);
        modifiedItems.removeAll(weightItems);

        for(OrderItem orderItem : modifiedItems) {
            if (orderItem.getParentOrderItemId().trim().isEmpty()) {
                String reqId = bridgeSession.getReceiptItemsCache().getLineInfoByLineContent(orderItem.getId()).getLineId();
//                client.sendResponseToChecApp(Ngp2ChecMapper.createRemovePOSReceiptEventItemLine(reqId));
//                bridgeSession.getReceiptItemsCache().updateLineIdForContent(orderItem.getId(), requestId);
                POSReceiptEvent posReceiptEvent = Ngp2ChecMapper.createPOSReceiptEventItem(bridgeSession, reqId, orderItem);
                posReceiptEvent.setUpdateType(POSReceiptEvent.UpdateType.Modify.name());
                client.sendResponseToChecApp(posReceiptEvent);
            }
        }
    }

    private void updateReceiptLineForWeightItems(List<OrderItem> modifiedItems, TcpClient client) {
        List<OrderItem> itemsToReAdd = bridgeSession.getOrderAdapter().getItemList().stream()
                .filter(e -> modifiedItems.contains(e))
                .collect(Collectors.toList());

        String reqId = "";
        for(OrderItem itm : itemsToReAdd){
            EReceiptLine line = bridgeSession.getReceiptItemsCache().getLineInfoByLineContent(itm.getId());
            reqId = line.getLineId();
            POSReceiptEvent posReceiptEvent = Ngp2ChecMapper.createPOSReceiptEventItem(bridgeSession, reqId, itm);
            posReceiptEvent.setUpdateType(POSReceiptEvent.UpdateType.Modify.name());
        }
    }



    private OrderItem getOrderItem(OrderItem orderItem) throws ChecOperationException {
        Optional<OrderItem> itemOptional = bridgeSession.getAddedItem(orderItem.getItemIdentifier(), orderItem.getId());
        if (!itemOptional.isPresent()) { // item that was just added not found in the transaction, abort with ITEM_NOT_FOUND
            throw new ChecOperationException(ErrorCode.ITEM_NOT_FOUND.getValue(), "", null);
        }

        return itemOptional.get();
    }

    protected void sendVoidItemEventsToConnectedApp(OrderItem orderItem, TcpClient client) {
        if (bridgeSession.getReceiptItemsCache().containsContent(orderItem.getId())) {
            String reqId = bridgeSession.getReceiptItemsCache().getLineInfoByLineContent(orderItem.getId()).getLineId();
            client.sendResponseToChecApp(Ngp2ChecMapper.createRemovePOSReceiptEventItemLine(reqId));
            bridgeSession.getReceiptItemsCache().removeLineByContent(orderItem.getId());
        }
    }

    /**
     * This method will be called when we receive an exception from NGP.
     * It will parse the error and set the ErrorCode and ErrorMessage fields needed to provide CHEC a proper response.
     *
     * @param e The exception received from NGP
     * @return ChecOperationException
     */
    private ChecOperationException createChecOperationException(Exception e) {

        return exceptionHandler.retrieveWebServiceError(e);
    }

    private AddItemResponse createExceptionAddItemResponse(ChecOperationException e, String requestId) {
        return new AddItemResponse()
                .setAddItemResult(new AddItemResult().setRequestId(requestId)
                        .setExceptionResult(new ExceptionResult()
                                .setErrorCode(e.getErrorCode())
                                .setMessage(e.getErrorMessage())));
    }

    private AddItemResponse createAddItemResponse(OrderItem orderItem, ArrayList<OrderItem> linkedItems, String requestId) {
        List<LineItem> lineItemList = new ArrayList<>();
        lineItemList.add(createLineItem(orderItem));

        for (OrderItem linkedItem : linkedItems) {
            lineItemList.add(createLineItem(linkedItem));
        }
        return new AddItemResponse()
                .setAddItemResult(new AddItemResult()
                        .setRequestId(requestId)
                        .setLineItemList(lineItemList));
    }

    private LineItem createLineItem(OrderItem orderItem) {
        return new LineItem()
                .setDescription(orderItem.getDescription())
                .setItemIdentifier(orderItem.getItemIdentifier())
                .setItemEntryMethod(orderItem.getItemEntryMethod())
                .setRegularUnitPrice(orderItem.getExtendedPrice().toString())
                // NGP handles only one field, quantity, for both quantity and weight. We need to use the field
                // accordingly in either the setQuantity or setWeight method
                .setQuantity(orderItem.isWeightedItem() ? "1" : orderItem.getQuantity().toString())
                .setExtendedPrice(orderItem.getExtendedPrice().toString())
                .setRestrictedAge(String.valueOf(orderItem.getRestrictedAge()))
                .setWeight(orderItem.isWeightedItem() ? orderItem.getQuantity().toString() : "0")
                .setVoidFlag(orderItem.isVoidFlag());
    }
}
