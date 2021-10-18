package com.tgcs.tgcp.bridge.checoperations.model.unsupportedoperation;

import com.tgcs.tgcp.bridge.checoperations.model.NodeType;
import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://bc.si.retail.ibm.com/POSBCSchema")
@XmlSeeAlso(UnsupportedOperationResult.class)
public class UnsupportedOperationResponse implements ResponseType, NodeType {

    @XmlAnyElement
    public Object unsupportedOperationResult;

    public Object getUnsupportedOperationResult() {
        return unsupportedOperationResult;
    }

    public UnsupportedOperationResponse setUnsupportedOperationResult(Object unsupportedOperationResult) {
        this.unsupportedOperationResult = unsupportedOperationResult;
        return this;
    }

    @Override
    public String getMessageType() {
        return ResponseTypeValues.RESPONSE.getValue();
    }

    @Override
    public String getNodeType() {
        return NodeTypeValues.RESPONSE.getValue();
    }
}