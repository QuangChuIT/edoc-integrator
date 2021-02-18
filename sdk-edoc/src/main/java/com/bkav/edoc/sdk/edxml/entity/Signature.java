package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

public class Signature {
    private SignedInfo signedInfo;
    private String signatureValue;
    private KeyInfo keyInfo;

    public Signature() {
    }

    public Signature(SignedInfo signedInfo, String signatureValue, KeyInfo keyInfo) {
        this.signedInfo = signedInfo;
        this.signatureValue = signatureValue;
        this.keyInfo = keyInfo;
    }

    public SignedInfo getSignedInfo() {
        return this.signedInfo;
    }

    public void setSignedInfo(SignedInfo signedInfo) {
        this.signedInfo = signedInfo;
    }

    public String getSignatureValue() {
        return this.signatureValue;
    }

    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }

    public KeyInfo getKeyInfo() {
        return this.keyInfo;
    }

    public void setKeyInfo(KeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("SignedInfo", this.signedInfo)
                .add("SignatureValue", this.signatureValue).add("KeyInfo", this.keyInfo).toString();
    }
}
