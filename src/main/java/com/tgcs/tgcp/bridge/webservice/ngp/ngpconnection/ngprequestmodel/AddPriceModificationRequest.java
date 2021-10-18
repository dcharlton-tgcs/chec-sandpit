package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import com.tgcs.tgcp.pos.model.PriceModification;

public class AddPriceModificationRequest extends WebRequestBody {

    Context context;

    PriceModification priceModification;

    public Context getContext() {
        return context;
    }

    public AddPriceModificationRequest setContext(Context context) {
        this.context = context;
        return this;
    }

    public PriceModification getPriceModification() {
        return priceModification;
    }

    public AddPriceModificationRequest setPriceModification(PriceModification priceModification) {
        this.priceModification = priceModification;
        return this;
    }
}
