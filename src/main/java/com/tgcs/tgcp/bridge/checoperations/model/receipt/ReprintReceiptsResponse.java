package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ReprintReceiptsResponse", namespace = "http://bc.si.retail.ibm.com/POSBCSchema")
public class ReprintReceiptsResponse implements ResponseType {

    @XmlElement(name = "ReprintReceiptsResult")
    private ReprintReceiptsResult reprintReceiptsResult;

    public ReprintReceiptsResponse() {
    }

    public ReprintReceiptsResponse(ReprintReceiptsResult reprintReceiptsResult) {
        this.reprintReceiptsResult = reprintReceiptsResult;
    }

    public ReprintReceiptsResult getReprintReceiptsResult() {
        return reprintReceiptsResult;
    }

    public ReprintReceiptsResponse setReprintReceiptsResult(ReprintReceiptsResult reprintReceiptsResult) {
        this.reprintReceiptsResult = reprintReceiptsResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }
}
