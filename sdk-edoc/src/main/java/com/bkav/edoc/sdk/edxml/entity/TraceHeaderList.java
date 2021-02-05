package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class TraceHeaderList extends CommonElement implements IElement<TraceHeaderList> {
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass())
                .add("TraceHeader", this.traceHeaders)
                .add("Bussiness", this.business)
                .add("BusinessInfo", this.businessInfo).toString();
    }

    @Override
    public void createElement(Element rootElement) {
        Element traceHeaderList = this.createElement(rootElement, "TraceHeaderList");
        this.createTraceHeader(traceHeaderList, this.business);
        if (this.traceHeaders != null && !this.traceHeaders.isEmpty()) {
            for (TraceHeader traceHeader : this.traceHeaders) {
                traceHeader.createElement(traceHeaderList);
            }
        }
    }

    private void createTraceHeader(Element paramElement, CommonElement commonElement) {
        if (commonElement != null) {
            commonElement.createElement(paramElement);
        }
    }

    @Override
    public TraceHeaderList getData(Element rootElement) {
        TraceHeaderList traceHeaderList = new TraceHeaderList();
        List<Element> elementList = rootElement.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("TraceHeader".equals(children.getName())) {
                    traceHeaderList.addTraceHeader(new TraceHeader().getData(children));
                }
                if ("Business".equals(children.getName()) || "Bussiness".equals(children.getName())) {
                    traceHeaderList.setBusiness(new Business().getData(children));
                }
            }

        }
        return traceHeaderList;
    }
}
