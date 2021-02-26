package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class EmailPDFRequest implements Serializable {
    private String organName;
    private byte[] bytes;

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
