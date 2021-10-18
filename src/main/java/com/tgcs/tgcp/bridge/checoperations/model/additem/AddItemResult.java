package com.tgcs.tgcp.bridge.checoperations.model.additem;



import com.tgcs.tgcp.bridge.checoperations.model.ExceptionResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddItemResult")
public class AddItemResult {

    @XmlElement(name = "RequestID")
    private String requestId;

    @XmlElement(name = "LineItem")
    private List<LineItem> lineItemList = new ArrayList<>();

    @XmlElement(name = "ExceptionResult")
    private ExceptionResult exceptionResult;

    public AddItemResult(){
    }

    public String getRequestId() {
        return requestId;
    }

    public AddItemResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public List<LineItem> getLineItemList() {
        return lineItemList;
    }

    public AddItemResult setLineItemList(List<LineItem> lineItemList) {
        this.lineItemList = lineItemList;
        return this;
    }

    public ExceptionResult getExceptionResult() {
        return exceptionResult;
    }

    public AddItemResult setExceptionResult(ExceptionResult exceptionResult) {
        this.exceptionResult = exceptionResult;
        return this;
    }
}
