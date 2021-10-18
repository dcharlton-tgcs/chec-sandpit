package com.tgcs.tgcp.bridge.checoperations.model.addcoupon;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schema:AddCouponResponse")
public class AddCouponResponse implements ResponseType {

    @XmlAttribute(name = "xmlns:schema")
    protected String namespace = "http://bc.si.retail.ibm.com/POSBCSchema";

    @XmlElement(name = "AddCouponResult")
    private AddCouponResult addCouponResult;

    public AddCouponResult getAddCouponResult() {
        return addCouponResult;
    }

    public AddCouponResponse setAddCouponResult(AddCouponResult addCouponResult) {
        this.addCouponResult = addCouponResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }


}
