package com.bkav.edoc.service.xml.status.header;

import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.xml.base.BaseElement;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.ResponseFor;
import com.bkav.edoc.service.xml.base.header.StaffInfo;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "Status", namespace = StringPool.TARGET_NAMESPACE)
@XmlType(name = "Status", propOrder = {"from", "responseFor", "staffInfo", "statusCode", "description", "timestamp"})
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageStatus extends BaseElement {

    @XmlElement(name = "ResponseFor")
    private ResponseFor responseFor;
    @XmlElement(name = "From")
    private Organization from;
    @XmlElement(name = "StatusCode")
    private String statusCode;
    @XmlElement(name = "Description")
    private String description;
    @XmlElement(name = "Timestamp")
    @XmlJavaTypeAdapter(DateAdapter.class)
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
        Element messageHeader = this.accumulate(element, "Status");
        this.accumulate(messageHeader, this.from, "From");
        this.accumulate(messageHeader, this.responseFor, (String) null);
        this.accumulate(messageHeader, this.staffInfo, (String) null);
        this.accumulate(messageHeader, "StatusCode", this.statusCode);
        this.accumulate(messageHeader, "Description", this.description);
        this.accumulate(messageHeader, "Timestamp", DateUtils.format(this.timestamp, DateUtils.DEFAULT_DATETIME_FORMAT));
    }

    public static Element getContent(Document document) {
        Element element = document.getRootElement().getChild("edXMLEnvelope", Namespace.getNamespace(EdXmlConstant.EDXML_URI));
        if (element != null) {
            List<Element> elementChildren = element.getChildren();
            Element header = null;
            if (elementChildren != null && elementChildren.size() > 0) {
                for (Element children : elementChildren) {
                    if ("edXMLHeader".equals(children.getName())) {
                        header = children;
                    }
                }
                if (header != null) {
                    List<Element> elements = header.getChildren();
                    if (elements != null && elements.size() > 0) {
                        for (Element item : elements) {
                            if ("Status".equals(item.getName()) || "MessageHeader".equals(item.getName())) {
                                return item;
                            }
                        }
                    }
                }
            }

        }
        return null;
    }

    public static MessageStatus getData(Element element) {
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
