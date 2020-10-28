package com.bkav.edoc.service.database.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EdocTraceHeaderList implements Serializable {

    public enum BusinessDocType {
        NEW, REVOKE, UPDATE, REPLACE
    }

    private Long documentId;
    private BusinessDocType businessDocType;
    private String businessDocReason;
    private Integer paper;
    private String department;
    private String staff;
    private String mobile;
    private String email;
    private EdocDocument document;
    private Set<EdocTraceHeader> traceHeaders = new HashSet<>();
    private String businessInfo;

    public EdocTraceHeaderList() {
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public Set<EdocTraceHeader> getTraceHeaders() {
        return traceHeaders;
    }

    public void setTraceHeaders(Set<EdocTraceHeader> traceHeaders) {
        this.traceHeaders = traceHeaders;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public BusinessDocType getBusinessDocType() {
        return businessDocType;
    }

    public void setBusinessDocType(BusinessDocType businessDocType) {
        this.businessDocType = businessDocType;
    }

    public String getBusinessDocReason() {
        return businessDocReason;
    }

    public void setBusinessDocReason(String businessDocReason) {
        this.businessDocReason = businessDocReason;
    }

    public Integer getPaper() {
        return paper;
    }

    public void setPaper(Integer paper) {
        this.paper = paper;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDocument(EdocDocument document) {
        this.document = document;
    }

    public EdocDocument getDocument() {
        return document;
    }
}
