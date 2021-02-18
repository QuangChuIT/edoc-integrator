package com.bkav.edoc.sdk.edxml.mineutil;

import com.bkav.edoc.sdk.edxml.entity.Attachment;
import com.bkav.edoc.sdk.edxml.entity.MessageHeader;
import com.bkav.edoc.sdk.edxml.entity.MessageStatus;
import com.bkav.edoc.sdk.edxml.entity.TraceHeaderList;
import com.bkav.edoc.sdk.edxml.util.AttachmentGlobalUtils;
import com.bkav.edoc.sdk.edxml.util.XmlUtil;
import com.bkav.edoc.sdk.resource.EdXmlConstant;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.w3c.dom.Document;

import javax.activation.DataHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExtractMime {
    /**
     * @param name
     * @param input
     * @return
     */
    public Attachment getAttachment(String name, DataHandler input) {
        Attachment attachment = new Attachment(name);
        String contentType = "";
        if (input.getContentType() == null || input.getContentType().equals("")) {
            contentType = AttachmentGlobalUtils.getContentType(attachment.getFormat());
        } else {
            contentType = input.getContentType();
        }
        attachment.setContentType(contentType);
        String encoding = "base64";
        attachment.setContentTransferEncoded(encoding);
        InputStream inputStream;
        attachment.setDescription(name);
        try {
            inputStream = input.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = new ByteArrayInputStream("".getBytes());
        }
        attachment.setInputStream(inputStream);

        return attachment;
    }

    /**
     * @param envelopeDoc
     * @return
     */
    public TraceHeaderList getTraceHeaderList(Document envelopeDoc) {

        org.jdom2.Document domEnvDoc = XmlUtil.convertFromDom(envelopeDoc);

        Element rootElement = domEnvDoc.getRootElement();

        TraceHeaderList traceHeaderList;

        Namespace envNs = xmlUtil.getEnvelopeNS(rootElement);

        Element parentNode = getSingerElement(rootElement, "Header", envNs);

        Element traceHeaderListNode = getSingerElement(parentNode,
                "TraceHeaderList", EdXmlConstant.EDXML_NS);

        traceHeaderList = TraceHeaderList.getData(traceHeaderListNode);

        return traceHeaderList;
    }

    /**
     * @param envelopDoc
     * @return
     * @throws Exception
     */
    public MessageStatus getStatus(Document envelopDoc) throws Exception {
        org.jdom2.Document domEnv = XmlUtil.convertFromDom(envelopDoc);

        Element rootElement = domEnv.getRootElement();

        Namespace envNs = xmlUtil.getEnvelopeNS(rootElement);

        Element headerNode = getSingerElement(rootElement, "Body", envNs);

        Element statusNode = headerNode.getChild("Status");
        MessageStatus status = new MessageStatus();

        status = status.getData(statusNode);

        return status;
    }


    /**
     * @param envelopeDoc
     * @return
     * @throws Exception
     */
    public MessageHeader getMessageHeader(Document envelopeDoc)
            throws Exception {

        org.jdom2.Document domENV = XmlUtil.convertFromDom(envelopeDoc);

        Element rootElement = domENV.getRootElement();

        MessageHeader messageHeader = new MessageHeader();

        Namespace envNs = xmlUtil.getEnvelopeNS(rootElement);

        Element headerNode = getSingerElement(rootElement, "Header", envNs);

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

    private static final XmlUtil xmlUtil = new XmlUtil();
}
