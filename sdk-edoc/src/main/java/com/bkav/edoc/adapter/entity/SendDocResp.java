package com.bkav.edoc.adapter.entity;

public class SendDocResp extends BaseResp {
    private Long docId;

    public SendDocResp() {
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }
}
