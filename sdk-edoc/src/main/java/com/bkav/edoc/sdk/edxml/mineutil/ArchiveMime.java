package com.bkav.edoc.sdk.edxml.mineutil;

import com.bkav.edoc.sdk.edxml.entity.Attachment;
import com.bkav.edoc.sdk.edxml.entity.Manifest;
import com.bkav.edoc.sdk.edxml.entity.Organization;
import com.bkav.edoc.sdk.edxml.entity.Reference;
import com.bkav.edoc.sdk.edxml.entity.env.Envelop;
import com.bkav.edoc.sdk.resource.EdXmlConstant;
import com.google.common.base.Strings;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;
import java.util.UUID;

public class ArchiveMime {
    public String createMime(Envelop envelop, List<Attachment> attachments) throws Exception {
        try {
            Element rootElement = new Element(EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            Document doc = new Document(rootElement);
            Element envelopElement = new Element("edXMLEnvelope", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            rootElement.addContent(envelopElement);
            // Create Header
            Element headerElement = new Element("edXMLHeader", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            envelopElement.addContent(headerElement);
            // Create message header
            Element messageHeaderElement = new Element("MessageHeader", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            headerElement.addContent(messageHeaderElement);

            Element fromElement = new Element("From", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);

            Organization fromOrgan = envelop.getHeader().getMessageHeader().getFrom();

            Element fromOrganId = new Element("OrganId", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            Manifest manifest = new Manifest();
            if (attachments != null && attachments.size() > 0) {
                for (Attachment attachment : attachments) {
                    String contentId = attachment.getContentId();
                    if (Strings.isNullOrEmpty(contentId)) {
                        contentId = UUID.randomUUID().toString();
                    }
                    Reference reference = new Reference();
                    reference.setContentId(contentId);
                    reference.setAttachmentName(attachment.getName());
                    reference.setContentType(attachment.getContentType());
                    reference.setDescription(attachment.getDescription());
                    manifest.addReference(reference);
                }
            }

            /*base.setBody(new Body(manifest));
            base.getBody().accumulate(envelopElement);
            Element attachmentEncoded = new Element("AttachmentEncoded", EdXmlConstant.EDXML_URI);
            rootElement.addContent(attachmentEncoded);
            buildAttachments(attachmentEncoded, base.getAttachments(), fileName);*/
            return "";
        } catch (Exception e) {
            throw new Exception("Error occurs during building edXML", e);
        }
    }
}
