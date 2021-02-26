package com.bkav.edoc.sdk.edxml.mineutil;

import com.bkav.edoc.sdk.edxml.entity.Attachment;
import com.bkav.edoc.sdk.edxml.entity.Manifest;
import com.bkav.edoc.sdk.edxml.entity.MessageStatus;
import com.bkav.edoc.sdk.edxml.entity.Reference;
import com.bkav.edoc.sdk.edxml.entity.env.Envelop;
import com.bkav.edoc.sdk.edxml.util.UUidUtils;
import com.bkav.edoc.sdk.edxml.util.XmlUtil;
import com.bkav.edoc.sdk.resource.EdXmlConstant;
import com.google.common.io.Files;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.util.XMLUtils;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArchiveMime {
    private static final ArchiveMime INSTANCE = new ArchiveMime();

    public static ArchiveMime getInstance() {
        return INSTANCE;
    }

    public File createStatus(MessageStatus messageStatus, String fileName, String path) {
        try {
            Document document = XmlUtil.convertEntityToDocument(MessageStatus.class, messageStatus);
            if (document != null) {
                OMElement node = XMLUtils.toOM(document.getDocumentElement());
                OMFactory factoryOM = OMAbstractFactory.getOMFactory();
                OMNamespace ns = factoryOM.createOMNamespace(
                        EdXmlConstant.EDXML_URI, EdXmlConstant.EDXML_PREFIX);
                OMNamespace omNamespace = factoryOM.createOMNamespace(EdXmlConstant.EDXML_URI, "");
                OMElement rootElement = factoryOM.createOMElement("edXML", omNamespace);
                OMElement envelopElement = factoryOM.createOMElement("edXMLEnvelope", ns);
                OMElement headerElement = factoryOM.createOMElement("edXMLHeader", ns);
                headerElement.addChild(node);
                envelopElement.addChild(headerElement);
                rootElement.addChild(envelopElement);
                Document status = XMLUtils.toDOM(rootElement).getOwnerDocument();
                return XmlUtil.buildContent(status, fileName, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File createMime(Envelop envelop, String fileName, String path) {
        File tmpFile = Files.createTempDir();
        try {
            OMFactory factoryOM = OMAbstractFactory.getOMFactory();

            OMNamespace ns = factoryOM.createOMNamespace(
                    EdXmlConstant.EDXML_URI, EdXmlConstant.EDXML_PREFIX);
            OMNamespace omNamespace = factoryOM.createOMNamespace(EdXmlConstant.EDXML_URI, "");
            OMElement rootElement = factoryOM.createOMElement("edXML", omNamespace);
            OMElement envelopElement = factoryOM.createOMElement("edXMLEnvelope", ns);
            OMElement headerElement = factoryOM.createOMElement("edXMLHeader", ns);
            // Create MessageHeader
            OMElement messageHeader = XmlUtil.getMessHeaderDoc(envelop.getHeader()
                    .getMessageHeader(), ns);
            System.out.println("Success convert message header in create mine with attachment size " + envelop.getAttachments().size() + " !!!!!!!!!!!!!!!!!!!!!");
            // Create TraceHeaderList
            OMElement traceHeaderList = XmlUtil.getTraceHeaderDoc(envelop.getHeader()
                    .getTraceHeaderList(), ns);
            System.out.println("Success convert trace header list in create mine with attachment size " + envelop.getAttachments().size() + " !!!!!!!!!!!!!!!!!!!!!!!!!");
            // Create Signature
            OMElement signature = XmlUtil.getSignatureDoc(envelop.getHeader().getSignature());
            headerElement.addChild(messageHeader);
            headerElement.addChild(traceHeaderList);
            headerElement.addChild(signature);
            envelopElement.addChild(headerElement);
            List<Reference> references = new ArrayList<>();
            List<Attachment> attachments = new ArrayList<>();
            // Create body
            if (envelop.getAttachments().size() > 0) {
                for (Attachment attachment : envelop.getAttachments()) {
                    // create content id for attachment
                    String contentId = UUidUtils.generate();
                    String contentType = attachment.getContentType();
                    // Create reference on body
                    Reference reference = new Reference();
                    reference.setContentId("cid:" + contentId);
                    reference.setAttachmentName(attachment.getName());
                    reference.setContentType(contentType);
                    reference.setDescription(attachment.getDescription());
                    references.add(reference);
                    attachment.setContentId(contentId);
                    attachments.add(attachment);
                }
            }

            Manifest manifest = new Manifest();

            manifest.setReferences(references);

            OMElement body = XmlUtil.getBodyChildDoc(manifest, ns);
            envelopElement.addChild(body);
            // Create Attachment
            OMElement attachmentEncoded = XmlUtil.getAttachmentDoc(attachments, ns, tmpFile.getAbsolutePath());
            rootElement.addChild(envelopElement);
            rootElement.addChild(attachmentEncoded);
            Document document = XMLUtils.toDOM(rootElement).getOwnerDocument();
            return XmlUtil.buildContent(document, fileName, path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            File[] files = tmpFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    file.delete();
                }
            }

            tmpFile.delete();
        }
        return null;
    }
}
