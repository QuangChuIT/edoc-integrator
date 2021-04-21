package com.bkav.edoc.payload;

import java.io.Serializable;

public class CheckExistDocumentRequest implements Serializable {
    private String fromOrgan;
    private String toOrgan;
    private String docCode;

    public CheckExistDocumentRequest() {
    }

    public String getFromOrgan() {
        return fromOrgan;
    }

    public void setFromOrgan(String fromOrgan) {
        this.fromOrgan = fromOrgan;
    }

    public String getToOrgan() {
        return toOrgan;
    }

    public void setToOrgan(String toOrgan) {
        this.toOrgan = toOrgan;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }
}
