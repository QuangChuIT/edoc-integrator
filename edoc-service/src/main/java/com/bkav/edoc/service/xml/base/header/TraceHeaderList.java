package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class TraceHeaderList extends BaseElement {
    private List<TraceHeader> traceHeaders;
    private Business business;
    private String businessInfo;

    public TraceHeaderList() {
    }

    public List<TraceHeader> getTraceHeaders() {
        return this.traceHeaders;
    }

    public void setTraceHeaders(List<TraceHeader> traceHeaders) {
        this.traceHeaders = traceHeaders;
    }

    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public void addTraceHeader(TraceHeader traceHeader) {
        if (this.traceHeaders == null) {
            this.traceHeaders = new ArrayList<>();
        }

        this.traceHeaders.add(traceHeader);
    }

    public static TraceHeaderList fromContent(Element traceHeaderElement) {
        TraceHeaderList traceHeaderList = new TraceHeaderList();
        List<Element> elementList = traceHeaderElement.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("TraceHeader".equals(children.getName())) {
                    traceHeaderList.addTraceHeader(TraceHeader.fromContent(children));
                }
                if ("Business".equals(children.getName()) || "Bussiness".equals(children.getName())) {
                    traceHeaderList.setBusiness(Business.fromContent(children));
                }
            }

        }
        return traceHeaderList;
    }

    public void accumulate(Element element) {
        Element traceHeaderList = this.accumulate(element, "TraceHeaderList");
        this.accumulate(traceHeaderList, this.business);
        if (this.traceHeaders != null && !this.traceHeaders.isEmpty()) {
            for (TraceHeader traceHeader : this.traceHeaders) {
                traceHeader.accumulate(traceHeaderList);
            }
        }
    }

    private void accumulate(Element element, BaseElement baseElement) {
        if (baseElement != null) {
            baseElement.accumulate(element);
        }
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass())
                .add("TraceHeader", this.traceHeaders)
                .add("Bussiness", this.business)
                .add("BusinessInfo", this.businessInfo).toString();
    }
}
