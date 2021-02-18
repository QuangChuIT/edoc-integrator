package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.Date;
import java.util.List;

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

    public MessageStatus getData(Element element) {
        MessageStatus messageStatus = new MessageStatus();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {
            for (Element childrenElement : elements) {
                if ("ResponseFor".equals(childrenElement.getName())) {
                    messageStatus.setResponseFor(ResponseFor.getData(childrenElement));
                } else if ("From".equals(childrenElement.getName())) {
                    messageStatus.setFrom(Organization.getData(childrenElement));
                } else if ("StatusCode".equals(childrenElement.getName())) {
                    messageStatus.setStatusCode(childrenElement.getTextTrim());
                } else if ("Description".equals(childrenElement.getName())) {
                    messageStatus.setDescription(childrenElement.getText());
                } else if ("Timestamp".equals(childrenElement.getName())) {
                    messageStatus.setTimestamp(DateUtils.parse(childrenElement.getTextTrim(), DateUtils.DEFAULT_DATETIME_FORMAT));
                } else if ("StaffInfo".equals(childrenElement.getName())) {
                    messageStatus.setStaffInfo(StaffInfo.fromNode(childrenElement));
                }
            }
        }
        return messageStatus;
    }

}
