package com.bkav.edoc.edxml.base.parser;

import com.bkav.edoc.edxml.base.Base;
import com.bkav.edoc.edxml.base.body.Body;
import com.bkav.edoc.edxml.base.header.BaseMessageHeader;
import com.bkav.edoc.edxml.base.header.Header;
import com.bkav.edoc.edxml.base.header.IMessageHeader;
import com.bkav.edoc.edxml.base.util.BaseXmlUtils;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.IOException;
import java.io.InputStream;

public class BaseXmlParser extends AbstractXmlParser {
    private static final BaseXmlParser INSTANCE = new BaseXmlParser();

    public BaseXmlParser() {
    }

    public static BaseXmlParser getInstance() {
        return INSTANCE;
    }

    public Base parse(InputStream inputStream) throws ParserException, ParserException {
        Base base = null;
        try {
            Document document = BaseXmlUtils.getDocument(inputStream);
            Element element = Header.getContent(document);
            if (element != null) {
                Base localBase = new Base(Header.fromContent(element, new BaseMessageHeader()), this.parseAttachment(document));
                Element elementBody = Body.getContent(document);
                if (elementBody != null) {
                    localBase.setBody(Body.fromContent(elementBody));
                    base = localBase;
                }
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new ParserException("Error occurs during parsing", e);
            }
        }

        return base;
    }

    public Class<? extends IMessageHeader> getMessageHeader() {
        return BaseMessageHeader.class;
    }
}
