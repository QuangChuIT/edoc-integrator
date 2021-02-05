package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.resource.EdXmlConstant;
import com.bkav.edoc.sdk.util.Validator;
import com.google.common.base.Strings;
import org.jdom2.Attribute;
import org.jdom2.Element;

public abstract class CommonElement {

    public abstract void createElement(Element element);

    public static String getAttribute(Element element, String attrName) {
        Attribute attribute = element.getAttribute(EdXmlConstant.EDXML_PREFIX + ":" + attrName);
        return attribute == null ? null : attribute.getValue();
    }

    public static String getAttributeWithPrefix(Element element, String attrName) {
        Attribute attribute = element.getAttribute(attrName);
        return attribute == null ? null : attribute.getValue();
    }

    public void createAttWithoutPrefix(Element element, String attrName, String attrValue) {
        if (!Strings.isNullOrEmpty(attrValue) && !Strings.isNullOrEmpty(attrName)) {
            element.setAttribute(attrName, attrValue);
        }
    }

    public void createAttribute(Element element, String attrName, String attrValue) {
        if (!Strings.isNullOrEmpty(attrValue) && !Strings.isNullOrEmpty(attrName)) {
            element.setAttribute(attrName, attrValue);
        }
    }

    protected Element createElement(Element element, String eName, int eValue) {
        if (eValue < 0) {
            return null;
        } else {
            Element newElement = new Element(eName, EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            newElement.setText(String.valueOf(eValue));
            element.addContent(newElement);
            return newElement;
        }
    }

    public Element createElement(Element rootElement, String content) {
        Element element = new Element(content, EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
        rootElement.addContent(element);
        return rootElement;
    }

    protected Element createElement(Element rootElement, String eName, String eValue) {
        if (Validator.isNullOrEmpty(eValue) || Validator.isNullOrEmpty(eName)) {
            return null;
        } else {
            Element newElement = new Element(eName, EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            newElement.setText(eValue);
            rootElement.addContent(newElement);
            return newElement;
        }
    }

    public void createElement(Element element, CommonElement commonElement, String content) {
        if (commonElement != null) {
            if (!Validator.isNullOrEmpty(content)) {
                element = this.createElement(element, content);
            }
            commonElement.createElement(element);
        }
    }

    protected Element createWithoutPrefix(Element rootElement, String eName) {
        Element newElement = new Element(eName, "http://www.mic.gov.vn/TBT/QCVN_102_2016");
        rootElement.addContent(newElement);
        return newElement;
    }

    protected Element createElementWithoutPrefix(Element rootElement, String eName, String eValue) {
        if (Strings.isNullOrEmpty(eValue)) {
            return null;
        } else {
            Element element = new Element(eName, EdXmlConstant.EDXML_URI);
            element.setText(eValue);
            rootElement.addContent(element);
            return element;
        }
    }
}
