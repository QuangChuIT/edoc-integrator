package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

import java.util.Date;

public class ResponseFor {

    private String organId;
    private String code;
    private Date promulgationDate;
    private String documentId;

    public ResponseFor() {
    }

    public ResponseFor(String organId, String code, Date promulgationDate, String documentId) {
        this.organId = organId;
        this.code = code;
        this.promulgationDate = promulgationDate;
        this.documentId = documentId;
    }

    public ResponseFor(String organId, String code, Date promulgationDate) {
        this.organId = organId;
        this.code = code;
        this.promulgationDate = promulgationDate;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getPromulgationDate() {
        return promulgationDate;
    }

    public void setPromulgationDate(Date promulgationDate) {
        this.promulgationDate = promulgationDate;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId",
                this.organId).add("Code", this.code).add("DocumentId",
                this.documentId).add("PromulgationDate", this.promulgationDate).toString();
    }

}
