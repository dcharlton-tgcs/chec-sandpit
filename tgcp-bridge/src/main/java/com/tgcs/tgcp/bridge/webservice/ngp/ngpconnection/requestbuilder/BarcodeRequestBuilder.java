package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.adapter.ChecAdapter;
import com.tgcs.tgcp.bridge.adapter.OrderItem;
import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.model.BarcodeRequest;
import com.tgcs.tgcp.pos.model.BarcodeSymbology;

public class BarcodeRequestBuilder {

    public static BarcodeRequest build(ChecAdapter orderItem, ConfProperties properties) {
        if (!(orderItem instanceof OrderItem)) return null;
        OrderItem item = (OrderItem) orderItem;
        return new BarcodeRequest()
                .barcodeData(item.getItemIdentifier())
                .symbology(BarcodeSymbology.fromValue(item.getScanDataType()))
                .context(ContextBuilder.build(properties))
                .quantity(item.getQuantity());
    }

}
