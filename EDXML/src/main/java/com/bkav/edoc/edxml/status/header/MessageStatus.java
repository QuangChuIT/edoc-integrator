package com.bkav.edoc.edxml.status.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.bkav.edoc.edxml.base.header.IMessageHeader;
import com.bkav.edoc.edxml.base.header.Organization;
import com.bkav.edoc.edxml.base.header.ResponseFor;
import com.bkav.edoc.edxml.base.header.StaffInfo;
import com.bkav.edoc.edxml.base.util.DateUtils;
import com.bkav.edoc.edxml.resource.StringPool;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "Status", namespace = StringPool.TARGET_NAMESPACE)
@XmlType(name = "Status", propOrder = {"from", "responseFor", "staffInfo", "statusCode", "description", "timestamp"})
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageStatus extends BaseElement implements IMessageHeader {

    @XmlElement(name = "ResponseFor")
    private ResponseFor responseFor;
    @XmlElement(name = "From")
    private Organization from;
    @XmlElement(name = "StatusCode")
    private String statusCode;
    @XmlElement(name = "Description")
    private String description;
    @XmlElement(name = "Timestamp")
    private Date timestamp;
    @XmlElement(name = "StaffInfo")
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
    public void accumulate(Element element) {
        Element messageHeader = this.accumulate(element, "MessageHeader");
        this.accumulate(messageHeader, this.from, "From");
        this.accumulate(messageHeader, this.responseFor, (String) null);
        this.accumulate(messageHeader, this.staffInfo, (String) null);
        this.accumulate(messageHeader, "StatusCode", this.statusCode);
        this.accumulate(messageHeader, "Description", this.description);
        this.accumulate(messageHeader, "Timestamp", DateUtils.format(this.timestamp, DateUtils.DEFAULT_DATETIME_FORMAT));
    }

    @Override
    public IMessageHeader fromContent(Element element) {
        MessageStatus messageStatus = new MessageStatus();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {
            for (Element childrenElement : elements) {
                if ("ResponseFor".equals(childrenElement.getName())) {
                    messageStatus.setResponseFor(ResponseFor.fromContent(childrenElement));
                } else if ("From".equals(childrenElement.getName())) {
                    messageStatus.setFrom(Organization.fromContent(childrenElement));
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("ResponseFor", this.responseFor)
                .add("From", this.from).add("StatusCode", this.statusCode)
                .add("Description", this.description).add("Timestamp", this.timestamp)
                .add("StaffInfo", this.staffInfo).toString();
    }
}
