package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.Date;
import java.util.List;

public class TraceHeader extends CommonElement implements IElement<TraceHeader> {
    private String organId;
    private Date timestamp;

    public TraceHeader() {
    }

    public TraceHeader(String organId, Date timestamp) {
        this.organId = organId;
        this.timestamp = timestamp;
    }

    public String getOrganId() {
        return this.organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId", this.organId).add("Timestamp", this.timestamp).toString();
    }

    @Override
    public void createElement(Element rootElement) {
        Element traceHeader = this.createElement(rootElement, "TraceHeader");
        this.createElement(traceHeader, "OrganId", this.organId);
        this.createElement(traceHeader, "Timestamp", DateUtils.format(this.timestamp, "yyyy/MM/dd HH:mm:ss"));
    }

    @Override
    public TraceHeader getData(Element element) {
        TraceHeader traceHeader = new TraceHeader();
        List<Element> childrenElements = element.getChildren();
        if (childrenElements != null && childrenElements.size() != 0) {
            for (Element children : childrenElements) {
                if ("OrganId".equals(children.getName())) {
                    traceHeader.setOrganId(children.getText());
                }
                if ("Timestamp".equals(children.getName())) {
                    traceHeader.setTimestamp(DateUtils.parse(children.getText(), DateUtils.DEFAULT_DATETIME_FORMAT));
                }
            }

        }
        return traceHeader;
    }
}
