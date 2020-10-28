package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class EdocDocumentType implements Serializable {
    private Integer documentType;
    private String value;

    public EdocDocumentType() {
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
