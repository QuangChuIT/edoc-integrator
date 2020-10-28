package com.bkav.edoc.service.database.cache;

import java.io.Serializable;
import java.util.Date;

public class TraceCacheEntry implements Serializable {
    private Long traceId;
    private Date timeStamp;
    private Date serverTimeStamp;
    private OrganizationCacheEntry fromOrgan;
    private OrganizationCacheEntry toOrgan;
    private String code;
    private String staffName;
    private String comment;
    private Date promulgationDate;
    private String department;
    private Integer statusCode;
    private String statusValue;
    private Boolean enable;
    private String staffEmail;
    private String staffMobile;
    private String edxmlDocumentId;
    private Long documentId;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public String getEdxmlDocumentId() {
        return edxmlDocumentId;
    }

    public void setEdxmlDocumentId(String edxmlDocumentId) {
        this.edxmlDocumentId = edxmlDocumentId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public OrganizationCacheEntry getFromOrgan() {
        return fromOrgan;
    }

    public void setFromOrgan(OrganizationCacheEntry fromOrgan) {
        this.fromOrgan = fromOrgan;
    }

    public OrganizationCacheEntry getToOrgan() {
        return toOrgan;
    }

    public void setToOrgan(OrganizationCacheEntry toOrgan) {
        this.toOrgan = toOrgan;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }
}
