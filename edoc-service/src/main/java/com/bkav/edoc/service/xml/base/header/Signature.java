package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.Iterator;
import java.util.List;

public class Signature extends BaseElement {
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

    public static Signature fromContent(Element element) {
        Signature signature = new Signature();
        List<Element> childrenElements = element.getChildren();
        if (childrenElements != null && childrenElements.size() != 0) {
            for (Element children : childrenElements) {
                if ("SignedInfo".equals(children.getName())) {
                    signature.setSignedInfo(SignedInfo.fromContent(children));
                }

                if ("SignatureValue".equals(children.getName())) {
                    signature.setSignatureValue(children.getText());
                }

                if ("KeyInfo".equals(children.getName())) {
                    signature.setKeyInfo(KeyInfo.fromContent(children));
                }
            }
        }
        return signature;
    }

    public void accumulate(Element element) {
        Element signature = this.accumulateWithoutPrefix(element, "Signature");
        this.accumulateWithoutPrefix(signature, this.signedInfo, (String) null);
        this.accumulateWithoutPrefix(signature, "SignatureValue", this.signatureValue);
        this.accumulateWithoutPrefix(signature, this.keyInfo, (String) null);
    }

    private void accumulateWithoutPrefix(Element element, BaseElement baseElement, String elementName) {
        if (baseElement != null) {
            if (!Strings.isNullOrEmpty(elementName)) {
                element = this.accumulateWithoutPrefix(element, elementName);
            }

            baseElement.accumulate(element);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("SignedInfo", this.signedInfo)
                .add("SignatureValue", this.signatureValue).add("KeyInfo", this.keyInfo).toString();
    }
}
