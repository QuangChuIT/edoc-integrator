package com.bkav.edoc.service.database.cache;

import com.bkav.edoc.service.database.entity.EdocPriority;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class DocumentCacheEntry implements Serializable {

    private Long documentId;
    private String edXMLDocId;
    private Date createDate;
    private Date modifiedDate;
    private String subject;
    private String codeNumber;
    private String codeNotation;
    private String docCode;
    private String promulgationPlace;
    private String shortenSubject;
    private Date promulgationDate;
    private int documentType;
    private String documentTypeName;
    private int documentTypeDetail;
    private boolean draft;
    private Date sentDate;
    private Boolean sendExt;
    private Boolean visited;
    private EdocPriority priority;
    private OrganizationCacheEntry fromOrgan;
    private List<OrganizationCacheEntry> toOrgan;
    private Boolean visible;
    private DocumentDetailCacheEntry documentDetail;
    private TraceHeaderListCacheEntry traceHeaderList;
    private List<NotificationCacheEntry> notifications;
    private List<TraceCacheEntry> traces;
    private List<AttachmentCacheEntry> attachments;
    private Boolean sendSuccess;
    private String transactionStatus;

    public DocumentCacheEntry() {
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

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public OrganizationCacheEntry getFromOrgan() {
        return fromOrgan;
    }

    public void setFromOrgan(OrganizationCacheEntry fromOrgan) {
        this.fromOrgan = fromOrgan;
    }

    public List<OrganizationCacheEntry> getToOrgan() {
        return toOrgan;
    }

    public void setToOrgan(List<OrganizationCacheEntry> toOrgan) {
        this.toOrgan = toOrgan;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public EdocPriority getPriority() {
        return priority;
    }

    public void setPriority(EdocPriority priority) {
        this.priority = priority;
    }

    public DocumentDetailCacheEntry getDocumentDetail() {
        return documentDetail;
    }

    public void setDocumentDetail(DocumentDetailCacheEntry documentDetail) {
        this.documentDetail = documentDetail;
    }

    public TraceHeaderListCacheEntry getTraceHeaderList() {
        return traceHeaderList;
    }

    public void setTraceHeaderList(TraceHeaderListCacheEntry traceHeaderList) {
        this.traceHeaderList = traceHeaderList;
    }

    public List<NotificationCacheEntry> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationCacheEntry> notifications) {
        this.notifications = notifications;
    }

    public List<TraceCacheEntry> getTraces() {
        return traces;
    }

    public void setTraces(List<TraceCacheEntry> traces) {
        this.traces = traces;
    }

    public List<AttachmentCacheEntry> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentCacheEntry> attachments) {
        this.attachments = attachments;
    }

    public String getShortenSubject() {
        return shortenSubject;
    }

    public void setShortenSubject(String shortenSubject) {
        this.shortenSubject = shortenSubject;
    }

    public Boolean getSendExt() {
        return sendExt;
    }

    public void setSendExt(Boolean sendExt) {
        this.sendExt = sendExt;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public Boolean getSendSuccess() {
        return sendSuccess;
    }

    public void setSendSuccess(Boolean sendSuccess) {
        this.sendSuccess = sendSuccess;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
