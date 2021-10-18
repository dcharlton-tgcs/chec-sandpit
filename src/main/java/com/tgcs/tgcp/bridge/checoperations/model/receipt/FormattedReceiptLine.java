package com.tgcs.tgcp.bridge.checoperations.model.receipt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FormattedReceiptLine")
public class FormattedReceiptLine {

    @XmlElement(name = "Feeds")
    private String feeds = "1";

    @XmlElement(name = "Align")
    private String align = "center";

    @XmlElement(name = "LineCategory")
    private String lineCategory;

    @XmlElement(name = "LineType")
    private String lineType;

    @XmlElement(name = "TextReceiptLine")
    private TextReceiptLine textReceiptLine;

    public FormattedReceiptLine() {
    }

    public FormattedReceiptLine(LineCategory category, LineType type, String line)
    {
        this.lineCategory = category.getValue();
        this.lineType = type.getValue();
        this.textReceiptLine = new TextReceiptLine(line);
    }

    public String getFeeds() {
        return feeds;
    }

    public void setFeeds(String feeds) {
        this.feeds = feeds;
    }


    public String getLineCategory() {
        return lineCategory;
    }

    public void setLineCategory(String lineCategory) {
        this.lineCategory =  lineCategory;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public TextReceiptLine getTextReceiptLine() {
        return textReceiptLine;
    }

    public FormattedReceiptLine setTextReceiptLine(TextReceiptLine textReceiptLine) {
        this.textReceiptLine = textReceiptLine;
        return this;
    }
}




