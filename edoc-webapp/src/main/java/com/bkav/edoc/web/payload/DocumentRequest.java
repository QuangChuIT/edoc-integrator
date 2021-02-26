package com.bkav.edoc.web.payload;

import java.io.Serializable;
import java.util.List;

public class DocumentRequest implements Serializable {
    private String subject;
    private List<String> toOrganDomain;
    private int priority;
    private int documentType;
    private String codeNumber;
    private String codeNation;
    private String staffName;
    private String fromOrgan;
    private int promulgationAmount;
    private int pageAmount;
    private String promulgationDate;
    private String promulgationPlace;
    private String content;
    private String dueDate;
    private boolean draft;
    private String signerFullName;
    private String signerPosition;
    private String sphereOfPromulgation;
    private List<Long> attachmentIds;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getToOrganDomain() {
        return toOrganDomain;
    }

    public void setToOrganDomain(List<String> toOrganDomain) {
        this.toOrganDomain = toOrganDomain;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getDocumentType() {
        return documentType;
    }

    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getCodeNation() {
        return codeNation;
    }

    public void setCodeNation(String codeNation) {
        this.codeNation = codeNation;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getFromOrgan() {
        return fromOrgan;
    }

    public void setFromOrgan(String fromOrgan) {
        this.fromOrgan = fromOrgan;
    }

    public int getPromulgationAmount() {
        return promulgationAmount;
    }

    public void setPromulgationAmount(int promulgationAmount) {
        this.promulgationAmount = promulgationAmount;
    }

    public int getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(int pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getPromulgationDate() {
        return promulgationDate;
    }

    public void setPromulgationDate(String promulgationDate) {
        this.promulgationDate = promulgationDate;
    }

    public String getPromulgationPlace() {
        return promulgationPlace;
    }

    public void setPromulgationPlace(String promulgationPlace) {
        this.promulgationPlace = promulgationPlace;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getSignerFullName() {
        return signerFullName;
    }

    public void setSignerFullName(String signerFullName) {
        this.signerFullName = signerFullName;
    }

    public String getSignerPosition() {
        return signerPosition;
    }

    public void setSignerPosition(String signerPosition) {
        this.signerPosition = signerPosition;
    }

    public String getSphereOfPromulgation() {
        return sphereOfPromulgation;
    }

    public void setSphereOfPromulgation(String sphereOfPromulgation) {
        this.sphereOfPromulgation = sphereOfPromulgation;
    }

    public List<Long> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(List<Long> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public boolean getDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }
}
