package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.List;

public class KeyInfoEdoc extends BaseElement {

    private String organId;
    private String token;

    @Override
    public void accumulate(Element parentElement) {
        Element keyInfoElement = this.accumulateWithoutPrefix(parentElement, "KeyInfo");
        this.accumulateWithoutPrefix(keyInfoElement, this.organId, "OrganId");
        this.accumulateWithoutPrefix(keyInfoElement, this.token, "Token");
    }

    public static KeyInfoEdoc fromContent(Element element) {
        KeyInfoEdoc keyInfo = new KeyInfoEdoc();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {

            for (Element thisElement : elements) {
                if ("OrganId".equals(thisElement.getName())) {
                    keyInfo.setOrganId(thisElement.getText());
                }
                if ("Token".equals(thisElement.getName())) {
                    keyInfo.setToken(thisElement.getText());
                }
            }

        }
        return keyInfo;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        return MoreObjects.toStringHelper(super.getClass())
                .add("OrganId", this.organId)
                .add("Token", this.token)
                .toString();
    }
}
