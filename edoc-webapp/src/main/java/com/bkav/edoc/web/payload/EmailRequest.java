package com.bkav.edoc.web.payload;

import com.bkav.edoc.service.database.entity.EdocDocument;

import java.io.Serializable;
import java.util.List;

public class EmailRequest implements Serializable {
    private long receiverId;
    private long count;
    private List<EdocDocument> documents;

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<EdocDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<EdocDocument> documents) {
        this.documents = documents;
    }
}
