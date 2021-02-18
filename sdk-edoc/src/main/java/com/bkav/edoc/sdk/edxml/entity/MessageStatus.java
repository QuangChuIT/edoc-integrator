package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

import java.util.Date;

public class MessageStatus {
    private ResponseFor responseFor;
    private Organization from;
    private String statusCode;
    private String description;
    private Date timestamp;
    private StaffInfo staffInfo;

    public MessageStatus() {
    }

    public MessageStatus(ResponseFor responseFor, Organization from, String statusCode, String description, StaffInfo staffInfo) {
        this.responseFor = responseFor;
        this.from = from;
        this.statusCode = statusCode;
        this.description = description;
        this.staffInfo = staffInfo;
    }

    public ResponseFor getResponseFor() {
        return responseFor;
    }

    public void setResponseFor(ResponseFor responseFor) {
        this.responseFor = responseFor;
    }

    public Organization getFrom() {
        return from;
    }

    public void setFrom(Organization from) {
        this.from = from;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public StaffInfo getStaffInfo() {
        return staffInfo;
    }

    public void setStaffInfo(StaffInfo staffInfo) {
        this.staffInfo = staffInfo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("ResponseFor", this.responseFor)
                .add("From", this.from).add("StatusCode", this.statusCode)
                .add("Description", this.description).add("Timestamp", this.timestamp)
                .add("StaffInfo", this.staffInfo).toString();
    }

}
