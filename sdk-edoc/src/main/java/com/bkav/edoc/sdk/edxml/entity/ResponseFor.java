package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.Date;

public class ResponseFor extends CommonElement implements IElement<ResponseFor> {

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

    @Override
    public void createElement(Element element) {
        Element responseFor = this.createElement(element, "ResponseFor");
        this.createElement(responseFor, "OrganId", this.organId);
        this.createElement(responseFor, "Code", this.code);
        this.createElement(responseFor, "DocumentId", this.documentId);
        this.createElement(responseFor, "PromulgationDate", DateUtils.format(this.promulgationDate));
    }

    @Override
    public ResponseFor getData(Element element) {
        return new ResponseFor(EdxmlUtils.getString(element, "OrganId"),
                EdxmlUtils.getString(element, "Code"),
                DateUtils.parse(EdxmlUtils.getString(element, "PromulgationDate"), DateUtils.DEFAULT_DATE_FORMAT),
                EdxmlUtils.getString(element, "DocumentId"));
    }
}
