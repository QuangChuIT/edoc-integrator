package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

import java.util.Date;

public class TraceHeader {
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

}
