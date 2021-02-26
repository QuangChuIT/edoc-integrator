package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.List;

public class SignatureEdoc extends BaseElement {

    private KeyInfoEdoc keyInfo;

    public SignatureEdoc(KeyInfoEdoc keyInfo) {
        this.keyInfo = keyInfo;
    }

    public SignatureEdoc() {
    }

    public KeyInfoEdoc getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(KeyInfoEdoc keyInfo) {
        this.keyInfo = keyInfo;
    }

    @Override
    public void accumulate(Element parentElement) {
        Element signature = this.accumulateWithoutPrefix(parentElement, "Signature");
        this.accumulateWithoutPrefix(signature, this.keyInfo, "KeyInfo");
    }

    private void accumulateWithoutPrefix(Element element, BaseElement baseElement, String elementName) {
        if (baseElement != null) {
            if (!Strings.isNullOrEmpty(elementName)) {
                element = this.accumulateWithoutPrefix(element, elementName);
            }
            baseElement.accumulate(element);
        }
    }

    public static SignatureEdoc fromContent(Element element) {
        SignatureEdoc signatureEdoc = new SignatureEdoc();
        List<Element> childrenElements = element.getChildren();
        if (childrenElements != null && childrenElements.size() != 0) {
            for (Element children : childrenElements) {
                if ("KeyInfo".equals(children.getName())) {
                    signatureEdoc.setKeyInfo(KeyInfoEdoc.fromContent(children));
                }
            }
        }
        return signatureEdoc;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("KeyInfo", this.keyInfo).toString();
    }
}
