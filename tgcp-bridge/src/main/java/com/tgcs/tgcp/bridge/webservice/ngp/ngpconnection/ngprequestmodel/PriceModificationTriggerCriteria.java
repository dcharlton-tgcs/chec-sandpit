package com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel;

import java.math.BigDecimal;
import java.util.Map;

public class PriceModificationTriggerCriteria {

    /**
     * pointsRequired (optional)
     */
    Integer pointsRequired;

    /**
     * triggeredAmount (optional)
     */
    BigDecimal triggeredAmount;

    /**
     * triggeredQuantity (optional)
     */
    BigDecimal triggeredQuantity;

    /**
     * orderTotalDueTrigger (optional)
     */
    BigDecimal orderTotalDueTrigger;

    /**
     * adjustmentAmount (optional)
     */
    BigDecimal adjustmentAmount;

    /**
     * customerMessage (optional)
     */

    Map<String, String> customerMessage;

    public Integer getPointsRequired() {
        return pointsRequired;
    }

    public PriceModificationTriggerCriteria setPointsRequired(Integer pointsRequired) {
        this.pointsRequired = pointsRequired;
        return this;
    }

    public BigDecimal getTriggeredAmount() {
        return triggeredAmount;
    }

    public PriceModificationTriggerCriteria setTriggeredAmount(BigDecimal triggeredAmount) {
        this.triggeredAmount = triggeredAmount;
        return this;
    }

    public BigDecimal getTriggeredQuantity() {
        return triggeredQuantity;
    }

    public PriceModificationTriggerCriteria setTriggeredQuantity(BigDecimal triggeredQuantity) {
        this.triggeredQuantity = triggeredQuantity;
        return this;
    }

    public BigDecimal getOrderTotalDueTrigger() {
        return orderTotalDueTrigger;
    }

    public PriceModificationTriggerCriteria setOrderTotalDueTrigger(BigDecimal orderTotalDueTrigger) {
        this.orderTotalDueTrigger = orderTotalDueTrigger;
        return this;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public PriceModificationTriggerCriteria setAdjustmentAmount(BigDecimal adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
        return this;
    }

    public Map<String, String> getCustomerMessage() {
        return customerMessage;
    }

    public PriceModificationTriggerCriteria setCustomerMessage(Map<String, String> customerMessage) {
        this.customerMessage = customerMessage;
        return this;
    }
}
