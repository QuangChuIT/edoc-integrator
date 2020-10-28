package com.bkav.edoc.service.database.entity;

import java.io.Serializable;
import java.util.Date;

public class EdocNotification implements Serializable {

    private Long notificationId;
    private String receiverId;
    private Integer sendNumber;
    private Date dateCreate;
    private Date modifiedDate;
    private Date dueDate;
    private EdocDocument document;

    public EdocNotification() {
    }

    public Boolean getTaken() {
        return taken;
    }

    public void setTaken(Boolean taken) {
        this.taken = taken;
    }

    private Boolean taken;

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(Integer sendNumber) {
        this.sendNumber = sendNumber;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
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

    public EdocDocument getDocument() {
        return document;
    }

    public void setDocument(EdocDocument document) {
        this.document = document;
    }
}
