package com.tgcs.tgcp.bridge.checoperations.model.addcoupon;

import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddCouponResult")
public class AddCouponResult {
    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "CouponItem")
    private CouponItem couponItem;

    @XmlElement(name = "ExceptionResult")
    private ExceptionResult exceptionResult;

    public String getRequestId() {
        return requestId;
    }

    public AddCouponResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public CouponItem getCouponItem() {
        return couponItem;
    }

    public AddCouponResult setCouponItem(CouponItem couponItem) {
        this.couponItem = couponItem;
        return this;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

    public AddCouponResult setExceptionResult(ExceptionResult exceptionResult) {
        this.exceptionResult = exceptionResult;
        return this;
    }
}
