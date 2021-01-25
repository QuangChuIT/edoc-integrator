package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.util.DateUtils;
import org.jdom2.Element;

import java.util.Date;

public class Feedback extends ResponseFor {
    public Feedback() {
    }

    public Feedback(String organId, String code, Date promulgationDate) {
        super(organId, code, promulgationDate);
    }

    public Feedback(String organId, String code, Date promulgationDate, String documentId) {
        super(organId, code, promulgationDate, documentId);
    }

    public void accumulate(Element parentElement) {
        Element newElement = this.accumulate(parentElement, "Feedback");
        this.accumulate(newElement, "OrganId", this.getOrganId());
        this.accumulate(newElement, "Code", this.getCode());
        this.accumulate(newElement, "DocumentId", this.getDocumentId());
        this.accumulate(newElement, "PromulgationDate", DateUtils.format(this.getPromulgationDate()));
    }
}
