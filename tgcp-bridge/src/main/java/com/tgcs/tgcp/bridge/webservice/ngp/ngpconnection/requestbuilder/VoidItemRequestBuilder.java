package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.requestbuilder;

import com.tgcs.tgcp.bridge.configuration.ConfProperties;
import com.tgcs.tgcp.pos.api.service.PosService;
import com.tgcs.tgcp.pos.model.*;

public class VoidItemRequestBuilder {

    public static VoidItemRequest build(ConfProperties properties) {
        ReasonCode reasonCode = new ReasonCode();
        reasonCode.setObjectType("OrderItem");
        reasonCode.setId("ITEM_VOID_REASON_CODE");

        Context context = ContextBuilder.build(properties);
        context.addReasonCodesItem(reasonCode);

        return new VoidItemRequest().context(context);
    }

    public static PosService.voidItemFromOrderBuilder createBuilder(Order order, OrderItem itemToVoid, ConfProperties properties) {
        return PosService.voidItemFromOrderBuilder.create(order.getId(), order.getVersion(),
                itemToVoid.getId(), itemToVoid.getPrices().get(0).getId(),
                VoidItemRequestBuilder.build(properties));
    }
}
