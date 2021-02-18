package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

public class KeyInfo {

    private X509Data x509Data;

    public KeyInfo() {
    }

    public KeyInfo(X509Data paramX509Data) {
        this.x509Data = paramX509Data;
    }

    public X509Data getX509Data() {
        return this.x509Data;
    }

    public void setX509Data(X509Data paramX509Data) {
        this.x509Data = paramX509Data;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("X509Data", this.x509Data).toString();
    }

}
