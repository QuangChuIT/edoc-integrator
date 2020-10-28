package com.bkav.edoc.service.database.entity;

import java.io.Serializable;
import java.util.Date;

public class EdocTraceHeader implements Serializable {

    private Long traceHeaderId;
    private String organDomain;
    private Date timeStamp;
    private EdocTraceHeaderList traceHeaderList;

    public EdocTraceHeader() {
    }

    public EdocTraceHeaderList getTraceHeaderList() {
        return traceHeaderList;
    }

    public void setTraceHeaderList(EdocTraceHeaderList traceHeaderList) {
        this.traceHeaderList = traceHeaderList;
    }

    public String getOrganDomain() {
        return organDomain;
    }

    public void setOrganDomain(String organDomain) {
        this.organDomain = organDomain;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getTraceHeaderId() {
        return traceHeaderId;
    }

    public void setTraceHeaderId(Long traceHeaderId) {
        this.traceHeaderId = traceHeaderId;
    }
}