package com.bkav.edoc.service.mineutil;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XpathUtil {

    /**
     * @param xpathValue
     * @return
     * @throws XPathExpressionException
     */
    public XPathExpression getXpathExpression(String xpathValue)
            throws XPathExpressionException {

        return xpath.compile(xpathValue);
    }

    private static final XPathFactory factory = XPathFactory.newInstance();

    private static XPath xpath;

    static {
        if (factory != null) {
            xpath = factory.newXPath();
        }
    }
}
