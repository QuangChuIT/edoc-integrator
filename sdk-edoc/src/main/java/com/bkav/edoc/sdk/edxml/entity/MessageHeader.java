package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.bkav.edoc.sdk.edxml.util.UUidUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageHeader extends CommonElement implements IElement<MessageHeader> {
    private Organization from;
    private List<Organization> toes;
    private String documentId = UUidUtils.generate();
    private Code code;
    private PromulgationInfo promulgationInfo;
    private DocumentType documentType;
    private String subject;
    private String content;
    private SignedInfo signedInfo;
    private SignerInfo signerInfo;
    private Date dueDate;
    private List<String> toPlaces;
    private OtherInfo otherInfo;
    private List<ResponseFor> responseFor;
    private int steeringType;
    private String applicationType;

    public MessageHeader() {
    }

    public MessageHeader(Organization from, List<Organization> toes, Code code, PromulgationInfo promulgationInfo, DocumentType documentType, String subject, String content, SignedInfo signedInfo, SignerInfo signerInfo, Date dueDate, List<String> toPlaces, OtherInfo otherInfo) {
        this.from = from;
        this.toes = toes;
        this.code = code;
        this.promulgationInfo = promulgationInfo;
        this.documentType = documentType;
        this.subject = subject;
        this.content = content;
        this.signedInfo = signedInfo;
        this.signerInfo = signerInfo;
        this.dueDate = dueDate;
        this.toPlaces = toPlaces;
        this.otherInfo = otherInfo;
    }

    public Organization getFrom() {
        return from;
    }

    public void setFrom(Organization from) {
        this.from = from;
    }

    public List<Organization> getToes() {
        return toes;
    }

    public void setToes(List<Organization> toes) {
        this.toes = toes;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void addTo(Organization organization) {
        if (this.toes == null) {
            this.toes = new ArrayList<>();
        }

        this.toes.add(organization);
    }

    public void addResponseFor(ResponseFor responseFor) {
        if (this.responseFor == null) {
            this.responseFor = new ArrayList<>();
        }

        this.responseFor.add(responseFor);
    }

    public PromulgationInfo getPromulgationInfo() {
        return promulgationInfo;
    }

    public void setPromulgationInfo(PromulgationInfo promulgationInfo) {
        this.promulgationInfo = promulgationInfo;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SignedInfo getSignedInfo() {
        return signedInfo;
    }

    public void setSignedInfo(SignedInfo signedInfo) {
        this.signedInfo = signedInfo;
    }

    public SignerInfo getSignerInfo() {
        return signerInfo;
    }

    public void setSignerInfo(SignerInfo signerInfo) {
        this.signerInfo = signerInfo;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<String> getToPlaces() {
        return toPlaces;
    }

    public void setToPlaces(List<String> toPlaces) {
        this.toPlaces = toPlaces;
    }

    public OtherInfo getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(OtherInfo otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<ResponseFor> getResponseFor() {
        return responseFor;
    }

    public void setResponseFor(List<ResponseFor> responseFor) {
        this.responseFor = responseFor;
    }

    public int getSteeringType() {
        return steeringType;
    }

    public void setSteeringType(int steeringType) {
        this.steeringType = steeringType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public void addToPlace(String toPlace) {
        if (this.toPlaces == null) {
            this.toPlaces = new ArrayList<>();
        }

        this.toPlaces.add(toPlace);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("From", this.from)
                .add("To", this.toes).add("DocumentId", this.documentId)
                .add("Code", this.code).add("PromulgationInfo", this.promulgationInfo)
                .add("DocumentType", this.documentType).add("Subject", this.subject)
                .add("Content", this.content).add("SignerInfo", this.signerInfo)
                .add("DueDate", this.dueDate).add("ToPlaces", this.toPlaces)
                .add("OtherInfo", this.otherInfo).add("ResponseFor", this.responseFor)
                .add("SteeringType", this.steeringType).add("ApplicationType", this.applicationType).toString();
    }

    @Override
    public MessageHeader getData(Element element) {
        MessageHeader messageHeader = new MessageHeader();

        List<Element> elementList = element.getChildren();

        if (elementList != null && elementList.size() > 0) {
            for (Element elementItem : elementList) {
                if ("From".equals(elementItem.getName())) {
                    messageHeader.setFrom(new Organization().getData(elementItem));
                } else if ("To".equals(elementItem.getName())) {
                    messageHeader.addTo(new Organization().getData(elementItem));
                } else if ("DocumentId".equals(elementItem.getName())) {
                    messageHeader.setDocumentId(elementItem.getText());
                } else if ("Code".equals(elementItem.getName())) {
                    messageHeader.setCode(new Code().getData(elementItem));
                } else if ("PromulgationInfo".equals(elementItem.getName())) {
                    messageHeader.setPromulgationInfo(new PromulgationInfo().getData(elementItem));
                } else if ("DocumentType".equals(elementItem.getName())) {
                    messageHeader.setDocumentType(new DocumentType().getData(elementItem));
                } else if ("Subject".equals(elementItem.getName())) {
                    messageHeader.setSubject(elementItem.getText());
                } else if ("Content".equals(elementItem.getName())) {
                    messageHeader.setContent(elementItem.getText());
                } else if ("SignerInfo".equals(elementItem.getName())) {
                    messageHeader.setSignerInfo(new SignerInfo().getData(elementItem));
                } else if ("DueDate".equals(elementItem.getName())) {
                    messageHeader.setDueDate(DateUtils.parse(elementItem.getText(), DateUtils.DEFAULT_DATE_FORMAT));
                } else {
                    if ("ToPlaces".equals(elementItem.getName())) {
                        List<Element> childrenElements = elementItem.getChildren();
                        if (childrenElements != null && childrenElements.size() != 0) {
                            for (Element children : childrenElements) {
                                if ("Place".equals(children.getName())) {
                                    messageHeader.addToPlace(children.getText());
                                }
                            }
                        }
                    }
                    if ("OtherInfo".equals(elementItem.getName())) {
                        messageHeader.setOtherInfo(new OtherInfo().getData(elementItem));
                    } else if ("ResponseFor".equals(elementItem.getName())) {
                        messageHeader.addResponseFor(new ResponseFor().getData(elementItem));
                    } else if ("SteeringType".equals(elementItem.getName())) {
                        messageHeader.setSteeringType(Integer.parseInt(elementItem.getText()));
                    }
                }
            }
        }
        return messageHeader;
    }

    @Override
    public void createElement(Element rootElement) {
        Element messageHeaderElement = this.createElement(rootElement, "MessageHeader");
        this.createElement(messageHeaderElement, this.from, "From");
        for (Organization organization : this.toes) {
            this.createElement(messageHeaderElement, organization, "To");
        }

        this.createElement(messageHeaderElement, "DocumentId", this.documentId);
        this.createElement(messageHeaderElement, this.code, (String) null);
        this.createElement(messageHeaderElement, this.promulgationInfo, (String) null);
        this.createElement(messageHeaderElement, this.documentType, (String) null);
        this.createElement(messageHeaderElement, "Subject", this.subject);
        this.createElement(messageHeaderElement, "Content", this.content);
        this.createElement(messageHeaderElement, this.signerInfo, (String) null);
        this.createElement(messageHeaderElement, "DueDate", DateUtils.format(this.dueDate));
        this.createElement(messageHeaderElement, this.otherInfo, (String) null);
        if (this.responseFor != null) {
            for (ResponseFor responseFor : this.responseFor) {
                this.createElement(messageHeaderElement, responseFor, null);
            }
        }
        this.createElement(messageHeaderElement, "SteeringType", this.steeringType);
        this.createElement(messageHeaderElement, "ApplicationType", this.applicationType);
        if (this.toPlaces != null && !this.toPlaces.isEmpty()) {
            Element toPlaceElement = this.createElement(messageHeaderElement, "ToPlaces");
            for (String toPlace : this.toPlaces) {
                this.createElement(toPlaceElement, "Place", toPlace);
            }
        }
    }
}
