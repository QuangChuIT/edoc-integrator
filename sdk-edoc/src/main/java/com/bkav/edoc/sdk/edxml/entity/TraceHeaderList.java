package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass())
                .add("TraceHeader", this.traceHeaders)
                .add("Bussiness", this.business)
                .add("BusinessInfo", this.businessInfo).toString();
    }

}
