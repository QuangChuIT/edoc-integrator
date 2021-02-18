package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

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

    public static ResponseFor getData(Element paramElement) {
        return new ResponseFor(EdxmlUtils.getString(paramElement, "OrganId"),
                EdxmlUtils.getString(paramElement, "Code"),
                DateUtils.parse(EdxmlUtils.getString(paramElement, "PromulgationDate"), DateUtils.DEFAULT_DATE_FORMAT),
                EdxmlUtils.getString(paramElement, "DocumentId"));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId",
                this.organId).add("Code", this.code).add("DocumentId",
                this.documentId).add("PromulgationDate", this.promulgationDate).toString();
    }

}
