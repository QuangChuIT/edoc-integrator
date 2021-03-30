package com.bkav.edoc.payload;

import java.io.Serializable;

public class GetPendingResult implements Serializable {
    private Long docId;
    private String organId;

    public GetPendingResult() {
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }
}
