package com.bkav.edoc.service.database.entity;

import java.util.Date;

public class TelegramMessage {
    private String receiverId;
    private String receiverName;
    private EdocDocument document;
    private Date createDate;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public EdocDocument getDocument() {
        return document;
    }

    public void setDocument(EdocDocument document) {
        this.document = document;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
