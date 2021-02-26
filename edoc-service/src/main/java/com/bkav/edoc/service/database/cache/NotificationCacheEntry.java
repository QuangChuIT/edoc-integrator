package com.bkav.edoc.service.database.cache;

import java.io.Serializable;
import java.util.Date;

public class NotificationCacheEntry implements Serializable {
    private Long notificationId;
    private OrganizationCacheEntry toOrganization;
    private Integer sendNumber;
    private Date createDate;
    private Date modifiedDate;
    private Date dueDate;
    private long documentId;
    private Boolean taken;

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(Integer sendNumber) {
        this.sendNumber = sendNumber;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

    public OrganizationCacheEntry getToOrganization() {
        return toOrganization;
    }

    public void setToOrganization(OrganizationCacheEntry toOrganization) {
        this.toOrganization = toOrganization;
    }

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }
}
