package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.List;

public class KeyInfo extends CommonElement implements IElement<KeyInfo> {

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

    private void createWithoutPrefix(Element element, CommonElement commonElement, String elementName) {
        if (commonElement != null) {
            if (!Strings.isNullOrEmpty(elementName)) {
                element = this.createWithoutPrefix(element, elementName);
            }

            commonElement.createElement(element);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("X509Data", this.x509Data).toString();
    }

    @Override
    public void createElement(Element element) {
        Element keyInfoElement = this.createWithoutPrefix(element, "KeyInfo");
        this.createWithoutPrefix(keyInfoElement, this.x509Data, "X509Data");
    }

    @Override
    public KeyInfo getData(Element element) {
        KeyInfo keyInfo = new KeyInfo();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {

            for (Element thisElement : elements) {
                if ("X509Data".equals(thisElement.getName())) {
                    keyInfo.setX509Data(new X509Data().getData(thisElement));
                }
            }

        }
        return keyInfo;
    }
}
