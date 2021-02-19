package com.bkav.edoc.sdk.edxml.mineutil;

import com.bkav.edoc.sdk.edxml.entity.*;
import com.bkav.edoc.sdk.edxml.entity.env.Body;
import com.bkav.edoc.sdk.edxml.entity.env.Envelop;
import com.bkav.edoc.sdk.edxml.entity.env.Header;
import com.bkav.edoc.sdk.edxml.util.XmlUtils;
import com.bkav.edoc.sdk.resource.EdXmlConstant;
import com.google.common.io.Files;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExtractMime {
    private static final ExtractMime INSTANCE = new ExtractMime();

    public static ExtractMime getInstance() {
        return INSTANCE;
    }

    public Envelop parser(InputStream inputStream) {
        Envelop envelop = null;
        try {
            Document document = XmlUtils.getDocument(inputStream);
            if (document != null) {
                MessageHeader messageHeader = new MessageHeader();
                Header header = new Header();
                header.setMessageHeader(this.getMessageHeader(document));
                header.setSignature(this.getSignature(document));
                header.setTraceHeaderList(this.getTraceHeaderList(document));
                Body body = getBody(document);
                List<Attachment> attachments = this.getAttachments(document);
                envelop = new Envelop();
                envelop.setHeader(header);
                envelop.setBody(body);
                envelop.setAttachments(attachments);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return envelop;
    }

    public Body getBody(Document document) {
        Element rootElement = document.getRootElement();

        Element envelope = getSingerElement(rootElement, "edXMLEnvelope", EdXmlConstant.EDXML_NS);

        Element bodyNode = getSingerElement(envelope,
                "edXMLBody", EdXmlConstant.EDXML_NS);
        return Body.getData(bodyNode);
    }

    /**
     * @param document
     * @return
     */
    public TraceHeaderList getTraceHeaderList(Document document) {

        Element rootElement = document.getRootElement();

        TraceHeaderList traceHeaderList;
        Element envelope = getSingerElement(rootElement, "edXMLEnvelope", EdXmlConstant.EDXML_NS);
        Element singerElement = getSingerElement(envelope, "edXMLHeader", EdXmlConstant.EDXML_NS);
        Element traceHeaderListNode = getSingerElement(singerElement, "TraceHeaderList", EdXmlConstant.EDXML_NS);

        traceHeaderList = TraceHeaderList.getData(traceHeaderListNode);

        return traceHeaderList;
    }

    public List<Attachment> getAttachments(Document document) {
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
                            attachment = Attachment.getData(value, fileTmp.getPath());
                            if (attachment != null) {
                                attachments.add(attachment);
                            }
                        }
                        return attachments;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

    }

    /**
     * @param document
     * @return
     * @throws Exception
     */
    public MessageStatus getStatus(Document document) throws Exception {

        Element rootElement = document.getRootElement();

        Element headerNode = getSingerElement(rootElement, "Body", EdXmlConstant.EDXML_NS);

        Element statusNode = headerNode.getChild("Status");
        MessageStatus status = new MessageStatus();

        status = status.getData(statusNode);

        return status;
    }


    public Signature getSignature(Document document) {
        Element rootElement = document.getRootElement();
        Element envelope = getSingerElement(rootElement, "edXMLEnvelope", EdXmlConstant.EDXML_NS);
        Element singerElement = getSingerElement(envelope, "edXMLHeader", EdXmlConstant.EDXML_NS);
        Element headerNode = getSingerElement(singerElement, "Signature", EdXmlConstant.EDXML_NS);

        return Signature.getData(headerNode);
    }

    /**
     * @param document
     * @return
     * @throws Exception
     */
    public MessageHeader getMessageHeader(Document document) {

        Element rootElement = document.getRootElement();

        MessageHeader messageHeader = new MessageHeader();
        Element envelope = getSingerElement(rootElement, "edXMLEnvelope", EdXmlConstant.EDXML_NS);
        Element headerNode = getSingerElement(envelope, "edXMLHeader", EdXmlConstant.EDXML_NS);
        Element messageHeaderNode = getSingerElement(headerNode,
                "MessageHeader", EdXmlConstant.EDXML_NS);

        messageHeader = messageHeader.getData(messageHeaderNode);

        return messageHeader;
    }

    public Element getSingerElement(Element rootElement, String childName,
                                    Namespace ns) {
        if (rootElement == null) {
            return null;
        }
        List<Element> list;
        if (ns == null) {
            list = rootElement.getChildren(childName);
        } else {
            list = rootElement.getChildren(childName, ns);
        }
        if (list == null || list.isEmpty())
            return null;
        return list.get(0);
    }

    public List<Element> getMultiElement(Element rootElement, String childName,
                                         Namespace ns) {
        if (rootElement == null) {
            return null;
        }
        List<Element> list;
        if (ns == null) {
            list = rootElement.getChildren(childName);
        } else {
            list = rootElement.getChildren(childName, ns);
        }
        return list;
    }
}
