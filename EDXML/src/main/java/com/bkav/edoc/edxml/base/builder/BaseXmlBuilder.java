package com.bkav.edoc.edxml.base.builder;

import com.bkav.edoc.edxml.base.Base;
import com.bkav.edoc.edxml.base.Content;
import com.bkav.edoc.edxml.base.attachment.Attachment;
import com.bkav.edoc.edxml.base.body.Body;
import com.bkav.edoc.edxml.base.body.Manifest;
import com.bkav.edoc.edxml.base.body.Reference;
import com.bkav.edoc.edxml.base.util.BaseXmlUtils;
import com.bkav.edoc.edxml.resource.EdXmlConstant;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BaseXmlBuilder {

    private static final String FILE_EXTENSION = "edxml";

    public BaseXmlBuilder() {
    }

    protected static Document buildDocument(Base base, String fileName) throws BuildException {
        try {
            Element rootElement = new Element(EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            Document document = new Document(rootElement);
            Element envelopElement = new Element("edXMLEnvelope", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            rootElement.addContent(envelopElement);
            base.getHeader().accumulate(envelopElement);
            Manifest manifest = new Manifest();
            if (base.getAttachments() != null && base.getAttachments().size() > 0) {
                for (Attachment attachment : base.getAttachments()) {
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

            base.setBody(new Body(manifest));
            base.getBody().accumulate(envelopElement);
            Element attachmentEncoded = new Element("AttachmentEncoded", EdXmlConstant.EDXML_URI);
            rootElement.addContent(attachmentEncoded);
            buildAttachments(attachmentEncoded, base.getAttachments(), fileName);
            return document;
        } catch (Exception e) {
            throw new BuildException("Error occurs during building edXML", e);
        }
    }

    private static void buildAttachments(Element element, List<Attachment> attachments, String fileName) throws IOException {
        if (attachments != null && attachments.size() > 0) {
            for (Attachment attachment : attachments) {
                attachment.accumulate(element, fileName);
            }

        }
    }

    public static Content build(Base base, String fileName, String path) throws BuildException {
        File tmpFile = Files.createTempDir();
        Document document = buildDocument(base, tmpFile.getPath());
        Content content = null;

        try {
            content = BaseXmlUtils.buildContent(document, fileName, FILE_EXTENSION, path);
            return content;
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
