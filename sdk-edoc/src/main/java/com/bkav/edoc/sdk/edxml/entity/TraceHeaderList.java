package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class TraceHeaderList {
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

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public static TraceHeaderList getData(Element traceHeaderElement) {
        TraceHeaderList traceHeaderList = new TraceHeaderList();
        List<Element> elementList = traceHeaderElement.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("TraceHeader".equals(children.getName())) {
                    traceHeaderList.addTraceHeader(TraceHeader.getData(children));
                }
                if ("Business".equals(children.getName()) || "Bussiness".equals(children.getName())) {
                    traceHeaderList.setBusiness(Business.getData(children));
                }
            }

        }
        return traceHeaderList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass())
                .add("TraceHeader", this.traceHeaders)
                .add("Business", this.business)
                .add("BusinessInfo", this.businessInfo).toString();
    }

}
