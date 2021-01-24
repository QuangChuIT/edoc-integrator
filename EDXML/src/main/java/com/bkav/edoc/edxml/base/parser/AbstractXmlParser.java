package com.bkav.edoc.edxml.base.parser;

import com.bkav.edoc.edxml.base.Base;
import com.bkav.edoc.edxml.base.attachment.Attachment;
import com.bkav.edoc.edxml.base.body.Body;
import com.bkav.edoc.edxml.base.header.Header;
import com.bkav.edoc.edxml.base.header.IMessageHeader;
import com.bkav.edoc.edxml.base.util.BaseXmlUtils;
import com.google.common.io.Files;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractXmlParser<T extends Base> {
    public static final String LINE_SEPARATOR = "\n";

    public AbstractXmlParser() {
    }

    public abstract T parse(InputStream inputStream) throws ParserException;

    public Header parseHeader(InputStream inputStream) throws IllegalAccessException, InstantiationException, ParserException {
        Header header;
        try {
            Document document = BaseXmlUtils.getDocument(inputStream);
            Element headerElement = Header.getContent(document);
            if (headerElement != null) {
                header = Header.fromContent(headerElement, this.getMessageHeader().newInstance());
            } else {
                header = new Header();
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException var11) {
                throw new ParserException("Error occurs during parsing", var11);
            }
        }
        return header;
    }

    public abstract Class<? extends IMessageHeader> getMessageHeader();

    public Body parseBody(InputStream inputStream) throws ParserException {
        Document document = BaseXmlUtils.getDocument(inputStream);

        Body body;
        try {
            Element bodyElement = Body.getContent(document);
            if (bodyElement == null) {
                body = new Body();
            } else {
                body = Body.fromContent(bodyElement);
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException var14) {
                throw new ParserException("Error occurs during parsing", var14);
            }
        }

        return body;
    }

    public List<Attachment> parseAttachment(InputStream inputStream) throws ParserException {
        Document document = BaseXmlUtils.getDocument(inputStream);
        return this.parseAttachment(document);
    }

    public List<Attachment> parseAttachment(Document document) throws ParserException {
        Element element = document.getRootElement();
        if (element == null) {
            return null;
        } else {
            List<Element> elementList = element.getChildren();
            if (elementList != null && elementList.size() != 0) {
                Element elementChildren = null;
                for (Element elementEntry : elementList) {
                    if ("AttachmentEncoded".equals((elementEntry.getName()))) {
                        elementChildren = elementEntry;
                    }
                }
                if (elementChildren != null && elementChildren.getChildren() != null && elementChildren.getChildren().size() != 0) {
                    File fileTmp = Files.createTempDir();
                    try {
                        List<Element> elements = elementChildren.getChildren();
                        Attachment attachment;
                        List<Attachment> attachments = new ArrayList<>();
                        for (Element value : elements) {
                            attachment = Attachment.fromContent(value, fileTmp.getPath());
                            if (attachment != null) {
                                attachments.add(attachment);
                            }
                        }
                        return attachments;
                    } catch (Exception e) {
                        /*BaseXmlUtils.deleteFolder(fileTmp);*/
                        throw new ParserException("Error occurs during parsing attachment", e);
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public static int indexOfBytes(byte[] array, byte[] newArray, int value) {
        if (array != null && array.length != 0 && newArray != null && newArray.length != 0) {
            int i = 0;
            int j = 0;
            for (byte k = newArray[i]; value < array.length; value++) {
                if (k == array[value]) {
                    if (i == 0) {
                        j = value;
                    } else if (i == newArray.length - 1) {
                        return j;
                    }
                    ++i;
                } else {
                    j = -1;
                    i = 0;
                }
                k = newArray[i];
            }
            return j;
        } else {
            return -1;
        }
    }
}
