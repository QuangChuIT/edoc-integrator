package com.bkav.edoc.service.database.entity;

import java.io.Serializable;
import java.util.List;

public class EmailRequest implements Serializable {
    private String receiverId;
    private String receiverName;
    private long numberOfDocument;
    private List<EdocDocument> edocDocument;

    public EmailRequest() {
    }

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

    public long getNumberOfDocument() {
        return numberOfDocument;
    }

    public void setNumberOfDocument(long numberOfDocument) {
        this.numberOfDocument = numberOfDocument;
    }

    public List<EdocDocument> getEdocDocument() {
        return edocDocument;
    }

    public void setEdocDocument(List<EdocDocument> edocDocument) {
        this.edocDocument = edocDocument;
    }
}
