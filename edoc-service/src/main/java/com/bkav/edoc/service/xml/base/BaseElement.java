package com.bkav.edoc.service.xml.base;

import com.bkav.edoc.service.resource.EdXmlConstant;
import com.google.common.base.Strings;
import org.jdom2.Attribute;
import org.jdom2.Element;

public abstract class BaseElement {

    public BaseElement() {
    }

    public abstract void accumulate(Element parentElement);

    protected Element accumulate(Element element, String elementName) {
        Element newElement = new Element(elementName, EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
        element.addContent(newElement);
        return newElement;
    }

    protected Element accumulate(Element parentElement, String elementName, String elementValue) {
        if (Strings.isNullOrEmpty(elementValue)) {
            return null;
        } else {
            Element newElement = new Element(elementName, EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            newElement.setText(elementValue);
            parentElement.addContent(newElement);
            return newElement;
        }
    }

    protected Element accumulateWithoutPrefix(Element parentElement, String elementName, String elementValue) {
        if (Strings.isNullOrEmpty(elementValue)) {
            return null;
        } else {
            Element newElement = new Element(elementName, EdXmlConstant.EDXML_URI);
            newElement.setText(elementValue);
            parentElement.addContent(newElement);
            return newElement;
        }
    }

    protected Element accumulateWithoutPrefix(Element element, String elementName) {
        Element newElement = new Element(elementName, EdXmlConstant.EDXML_URI);
        element.addContent(newElement);
        return newElement;
    }

    protected Element accumulate(Element element, String elementName, int elementValue) {
        if (elementValue < 0) {
            return null;
        } else {
            Element newElement = new Element(elementName, EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            newElement.setText(String.valueOf(elementValue));
            element.addContent(newElement);
            return newElement;
        }
    }

    protected void accumulate(Element element, BaseElement baseElement, String elementName) {
        if (baseElement != null) {
            if (!Strings.isNullOrEmpty(elementName)) {
                element = this.accumulate(element, elementName);
            }

            baseElement.accumulate(element);
        }
    }

    protected void accumulateAttribute(Element element, String attrName, String attributeValue) {
        if (!Strings.isNullOrEmpty(attributeValue)) {
            element.setAttribute(attrName, attributeValue);
        }
    }

    protected void accumulateAttWithoutPrefix(Element element, String attrName, String attrValue) {
        if (!Strings.isNullOrEmpty(attrValue)) {
            element.setAttribute(attrName, attrValue);
        }
    }

    protected static String getAttribute(Element element, String attrName) {
        Attribute attribute = element.getAttribute(EdXmlConstant.EDXML_PREFIX + attrName);
        return attribute == null ? null : attribute.getValue();
    }

    protected static String getAttributeWithPrefix(Element element, String attributeName) {
        Attribute attribute = element.getAttribute(attributeName);
        return attribute == null ? null : attribute.getValue();
    }
}
