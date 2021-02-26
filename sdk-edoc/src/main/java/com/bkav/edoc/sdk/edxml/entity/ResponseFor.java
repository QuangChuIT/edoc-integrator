package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.DateAdapter;
import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.bkav.edoc.sdk.resource.EdXmlConstant;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@XmlRootElement(name = "ResponseFor", namespace = EdXmlConstant.EDXML_URI)
@XmlType(name = "ResponseFor", propOrder = {"organId", "code", "promulgationDate", "documentId"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseFor {

    @XmlElement(name = "OrganId")
    private String organId;
    @XmlElement(name = "Code")
    private String code;
    @XmlElement(name = "PromulgationDate")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date promulgationDate;
    @XmlElement(name = "DocumentId")
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
