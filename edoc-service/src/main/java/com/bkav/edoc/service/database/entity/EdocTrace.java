package com.bkav.edoc.service.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class EdocTrace implements Serializable {

    private Long traceId;
    private Date timeStamp;
    private Date serverTimeStamp;
    private String organizationInCharge;
    private String organName;
    private String organAdd;
    private String email;
    private String telephone;
    private String fromOrganDomain;
    private String fax;
    private String website;
    private String toOrganDomain;
    private String code;
    private String staffName;
    private String comment;
    private Date promulgationDate;
    private String department;
    private Integer statusCode;
    private Boolean enable;
    private String staffEmail;
    private String staffMobile;
    private String edxmlDocumentId;
    @JsonIgnore
    private EdocDocument document;

    public EdocTrace() {
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getEdxmlDocumentId() {
        return edxmlDocumentId;
    }

    public void setEdxmlDocumentId(String edxmlDocumentId) {
        this.edxmlDocumentId = edxmlDocumentId;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getServerTimeStamp() {
        return serverTimeStamp;
    }

    public void setServerTimeStamp(Date serverTimeStamp) {
        this.serverTimeStamp = serverTimeStamp;
    }

    public String getOrganizationInCharge() {
        return organizationInCharge;
    }

    public void setOrganizationInCharge(String organizationInCharge) {
        this.organizationInCharge = organizationInCharge;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganAdd() {
        return organAdd;
    }

    public void setOrganAdd(String organAdd) {
        this.organAdd = organAdd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFromOrganDomain() {
        return fromOrganDomain;
    }

    public void setFromOrganDomain(String fromOrganDomain) {
        this.fromOrganDomain = fromOrganDomain;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getToOrganDomain() {
        return toOrganDomain;
    }

    public void setToOrganDomain(String toOrganDomain) {
        this.toOrganDomain = toOrganDomain;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPromulgationDate() {
        return promulgationDate;
    }

    public void setPromulgationDate(Date promulgationDate) {
        this.promulgationDate = promulgationDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(String staffMobile) {
        this.staffMobile = staffMobile;
    }

    public void setDocument(EdocDocument document) {
        this.document = document;
    }

    public EdocDocument getDocument() {
        return document;
    }
}
