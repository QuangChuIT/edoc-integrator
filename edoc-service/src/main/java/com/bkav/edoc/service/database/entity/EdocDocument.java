package com.bkav.edoc.service.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EdocDocument implements Serializable, Cloneable {

    private Long documentId;
    private String edXMLDocId;
    private Date createDate;
    private Date modifiedDate;
    private String subject;
    private String codeNumber;
    private String codeNotation;
    private String promulgationPlace;
    private Date promulgationDate;
    private int documentType;
    private String documentTypeName;
    private int documentTypeDetail;
    private boolean draft;
    private Date sentDate;
    private String docCode;
    private boolean sendExt;
    private String documentExtId;
    private Boolean visited;
    private String toOrganDomain;
    private boolean receivedExt;
    private String fromOrganDomain;
    private Boolean visible;
    @JsonIgnore
    private EdocDocumentDetail documentDetail;
    @JsonIgnore
    private EdocTraceHeaderList traceHeaderList;
    private int priority;
    @JsonIgnore
    private Set<EdocNotification> notifications = new HashSet<>();
    @JsonIgnore
    private Set<EdocTrace> traces = new HashSet<>();
    @JsonIgnore
    private Set<EdocAttachment> attachments = new HashSet<>();

    public EdocDocument() {
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getEdXMLDocId() {
        return edXMLDocId;
    }

    public void setEdXMLDocId(String edXMLDocId) {
        this.edXMLDocId = edXMLDocId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getCodeNotation() {
        return codeNotation;
    }

    public void setCodeNotation(String codeNotation) {
        this.codeNotation = codeNotation;
    }

    public String getPromulgationPlace() {
        return promulgationPlace;
    }

    public void setPromulgationPlace(String promulgationPlace) {
        this.promulgationPlace = promulgationPlace;
    }

    public Date getPromulgationDate() {
        return promulgationDate;
    }

    public void setPromulgationDate(Date promulgationDate) {
        this.promulgationDate = promulgationDate;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    public int getDocumentTypeDetail() {
        return documentTypeDetail;
    }

    public void setDocumentTypeDetail(int documentTypeDetail) {
        this.documentTypeDetail = documentTypeDetail;
    }

    public boolean getDraft() {
        return draft;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public EdocTraceHeaderList getTraceHeaderList() {
        return traceHeaderList;
    }

    public void setTraceHeaderList(EdocTraceHeaderList traceHeaderList) {
        this.traceHeaderList = traceHeaderList;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getToOrganDomain() {
        return toOrganDomain;
    }

    public void setToOrganDomain(String toOrganDomain) {
        this.toOrganDomain = toOrganDomain;
    }

    public String getFromOrganDomain() {
        return fromOrganDomain;
    }

    public void setFromOrganDomain(String fromOrganDomain) {
        this.fromOrganDomain = fromOrganDomain;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public EdocDocumentDetail getDocumentDetail() {
        return documentDetail;
    }

    public void setDocumentDetail(EdocDocumentDetail documentDetail) {
        this.documentDetail = documentDetail;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setNotifications(Set<EdocNotification> notifications) {
        this.notifications = notifications;
    }

    public Set<EdocNotification> getNotifications() {
        return notifications;
    }

    public Set<EdocTrace> getTraces() {
        return traces;
    }

    public void setTraces(Set<EdocTrace> traces) {
        this.traces = traces;
    }

    public Set<EdocAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<EdocAttachment> attachments) {
        this.attachments = attachments;
    }

    public boolean isDraft() {
        return draft;
    }

    public boolean getSendExt() {
        return sendExt;
    }

    public void setSendExt(boolean sendExt) {
        this.sendExt = sendExt;
    }

    public String getDocumentExtId() {
        return documentExtId;
    }

    public void setDocumentExtId(String documentExtId) {
        this.documentExtId = documentExtId;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public boolean isReceivedExt() {
        return receivedExt;
    }

    public void setReceivedExt(boolean receivedExt) {
        this.receivedExt = receivedExt;
    }

    public boolean getReceivedExt() { return this.receivedExt;}

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        EdocDocument obj = new EdocDocument();
        obj.setDocumentId(this.documentId);
        obj.setSubject(this.subject);
        obj.setCreateDate(this.createDate);
        obj.setDocCode(this.docCode);
        obj.setDocumentDetail(this.documentDetail);
        obj.setAttachments(this.attachments);
        obj.setCodeNotation(this.codeNotation);
        obj.setCodeNumber(this.codeNumber);
        obj.setDocumentExtId(this.documentExtId);
        obj.setDocumentType(this.documentType);
        obj.setDocumentTypeDetail(this.documentTypeDetail);
        obj.setDocumentTypeName(this.documentTypeName);
        obj.setDraft(this.draft);
        obj.setFromOrganDomain(this.fromOrganDomain);
        obj.setEdXMLDocId(this.edXMLDocId);
        obj.setModifiedDate(this.modifiedDate);
        obj.setNotifications(this.notifications);
        obj.setPriority(this.priority);
        obj.setPromulgationDate(this.promulgationDate);
        obj.setPromulgationPlace(this.promulgationPlace);
        obj.setReceivedExt(this.receivedExt);
        obj.setSendExt(this.sendExt);
        obj.setSentDate(this.sentDate);
        obj.setToOrganDomain(this.toOrganDomain);
        obj.setTraceHeaderList(this.traceHeaderList);
        obj.setTraces(this.traces);
        obj.setVisible(this.visible);
        obj.setVisited(this.visited);
        return obj;
    }
}
