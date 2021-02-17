package com.bkav.edoc.sdk.entity;

import java.util.List;

public class GetPendingDocIDsResp extends BaseResp {
    private List<Long> docIDs;

    public List<Long> getDocIDs() {
        return docIDs;
    }

    public void setDocIDs(List<Long> docIDs) {
        this.docIDs = docIDs;
    }

    @Override
    public String toString() {
        return "GetPendingDocIDsResp{" +
                "docIDs=" + docIDs +
                '}';
    }
}
