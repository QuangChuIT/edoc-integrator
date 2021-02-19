/**
 *
 */
package com.bkav.edoc.service.mineutil;

import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.CheckPermission;
import com.bkav.edoc.service.xml.base.header.SignatureEdoc;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.w3c.dom.Document;

import javax.activation.DataHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractMime {

    /**
     *
     * @param name
     * @param input
     * @return
     */
    public Attachment getAttachment(String name, DataHandler input) {
        Attachment attachment = new Attachment(name);
        String contentType = "";
        if (input.getContentType() == null || input.getContentType().equals("")) {
            contentType = AttachmentGlobalUtil.getContentType(attachment.getFormat());
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
            LOGGER.error(e.getMessage());
            inputStream = new ByteArrayInputStream("".getBytes());
        }
        attachment.setInputStream(inputStream);

        return attachment;
    }

    /**
     *
     * @param envelope
     * @return
     */
    public Map<String, String> getAttachmentName(Document envelope) {

        Map<String, String> attNames = new HashMap<>();
        org.jdom2.Document domEnvDoc = XmlUtil.convertFromDom(envelope);

        Element envElement = domEnvDoc.getRootElement();

        Namespace envNs = envElement.getNamespace();

        Element body = getSingerElement(envElement, EdXmlConstant.BODY_TAG,
                envNs);
        if (body != null) {
            Element manifestNode = getSingerElement(body,
                    EdXmlConstant.MANIFEST_TAG, EdXmlConstant.EDXML_NS);
            List<Element> referenceNodes = getMultiElement(manifestNode,
                    EdXmlConstant.REFERENCE_TAG, EdXmlConstant.EDXML_NS);
            if (referenceNodes != null && referenceNodes.size() > 0) {
                for (Element item : referenceNodes) {
                    String name = item.getChildText(
                            EdXmlConstant.ATTACHMENT_NAME_TAG,
                            EdXmlConstant.EDXML_NS);
                    List<Attribute> attributes = item.getAttributes();
                    String hrefValue = null;
                    for (Attribute attribute : attributes) {
                        if (attribute.getName().equals(EdXmlConstant.HREF_ATTR)) {
                            hrefValue = attribute.getValue();
                        }
                    }
                    if (hrefValue != null && hrefValue.contains("cid:")) {
                        hrefValue = hrefValue.replace("cid:", "");
                    }
                    attNames.put(hrefValue, name);
                }
            }
        }

        return attNames;
    }

    /**
     *
     * @param envelope
     * @param documentName
     * @return
     */
    public String getOrganId(Document envelope, String documentName) {

        org.jdom2.Document domEnvDoc = XmlUtil.convertFromDom(envelope);

        Element envElement = domEnvDoc.getRootElement();

        Namespace envNs = envElement.getNamespace();

        Element body = getSingerElement(envElement, EdXmlConstant.BODY_TAG,
                envNs);
        if (body != null) {
            Element pendingDocumentNode = getSingerElement(body,
                    documentName, null);
            if (pendingDocumentNode != null) {
                return pendingDocumentNode.getChildText("OrganId", null);
            }
        }

        return null;
    }

    /**
     *
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

        traceHeaderList = TraceHeaderList.fromContent(traceHeaderListNode);

        return traceHeaderList;
    }

    /**
     *
     * @param envelopDoc
     * @return
     * @throws Exception
     */
    public MessageStatus getStatus(Document envelopDoc) {
        org.jdom2.Document domEnv = XmlUtil.convertFromDom(envelopDoc);

        Element rootElement = domEnv.getRootElement();

        Namespace envNs = xmlUtil.getEnvelopeNS(rootElement);

        Element headerNode = getSingerElement(rootElement, "Body", envNs);

        Element statusNode = headerNode.getChild("Status");
        MessageStatus status;

        status = MessageStatus.getData(statusNode);

        return status;
    }

    /**
     *
     * @param envelop
     * @return
     */
    public CheckPermission getCheckPermission(Document envelop) {

        org.jdom2.Document domEnv = XmlUtil.convertFromDom(envelop);

        Element rootElement = domEnv.getRootElement();

        Namespace envNs = xmlUtil.getEnvelopeNS(rootElement);

        Element headerNode = getSingerElement(rootElement, "Header", envNs);

        Element checkPermissionNode = headerNode.getChild("CheckPermission");

        return CheckPermission.fromContent(checkPermissionNode);

    }

    public SignatureEdoc getSignature(Document envelop) {
        org.jdom2.Document domEvn = XmlUtil.convertFromDom(envelop);

        Element rootElement = domEvn.getRootElement();

        Namespace envNS = xmlUtil.getEnvelopeNS(rootElement);

        Element headerNode = getSingerElement(rootElement, "Header", envNS);

        Element messageHeaderNode = getSingerElement(headerNode,
                "Signature", EdXmlConstant.EDXML_NS);

        return SignatureEdoc.fromContent(messageHeaderNode);
    }

    public CheckPermission getCheckPermissionFromRq(Document envelop, String elementName) {

        org.jdom2.Document domEnv = XmlUtil.convertFromDom(envelop);

        Element rootElement = domEnv.getRootElement();

        Namespace envNs = xmlUtil.getEnvelopeNS(rootElement);

        Element headerNode = getSingerElement(rootElement, "Body", envNs);

        Element checkPermissionNode = headerNode.getChild(elementName);

        return CheckPermission.fromContent(checkPermissionNode);
    }

    /**
     *
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

        messageHeader = messageHeader.fromContent(messageHeaderNode);

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


    private final static Logger LOGGER = Logger.getLogger(ExtractMime.class);

    private static final XmlUtil xmlUtil = new XmlUtil();

}
