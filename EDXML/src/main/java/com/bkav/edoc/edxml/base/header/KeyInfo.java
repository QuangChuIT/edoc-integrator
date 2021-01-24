package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.List;

public class KeyInfo extends BaseElement {

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

    public static KeyInfo fromContent(Element element) {
        KeyInfo keyInfo = new KeyInfo();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {

            for (Element thisElement : elements) {
                if ("X509Data".equals(thisElement.getName())) {
                    keyInfo.setX509Data(X509Data.fromContent(thisElement));
                }
            }

        }
        return keyInfo;
    }

    public void accumulate(Element element) {
        Element keyInfoElement = this.accumulateWithoutPrefix(element, "KeyInfo");
        this.accumulateWithoutPrefix(keyInfoElement, this.x509Data, "X509Data");
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
        return MoreObjects.toStringHelper(super.getClass()).add("X509Data", this.x509Data).toString();
    }
}
