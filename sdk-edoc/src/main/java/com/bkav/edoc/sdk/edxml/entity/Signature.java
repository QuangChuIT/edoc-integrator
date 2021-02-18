package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

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

    public static Signature getData(Element element) {
        Signature signature = new Signature();
        List<Element> childrenElements = element.getChildren();
        if (childrenElements != null && childrenElements.size() != 0) {
            for (Element children : childrenElements) {
                if ("SignedInfo".equals(children.getName())) {
                    signature.setSignedInfo(SignedInfo.getData(children));
                }

                if ("SignatureValue".equals(children.getName())) {
                    signature.setSignatureValue(children.getText());
                }

                if ("KeyInfo".equals(children.getName())) {
                    signature.setKeyInfo(KeyInfo.getData(children));
                }
            }
        }
        return signature;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("SignedInfo", this.signedInfo)
                .add("SignatureValue", this.signatureValue).add("KeyInfo", this.keyInfo).toString();
    }
}
