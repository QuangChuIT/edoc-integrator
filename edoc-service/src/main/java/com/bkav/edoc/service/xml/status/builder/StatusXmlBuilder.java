package com.bkav.edoc.service.xml.status.builder;

import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.xml.base.Content;
import com.bkav.edoc.service.xml.base.builder.BuildException;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.google.common.io.Files;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;

public class StatusXmlBuilder {
    private static final String FILE_EXTENSION = "edxml";

    public StatusXmlBuilder() {
    }

    protected static Document buildDocument(MessageStatus messageStatus) throws BuildException {
        try {
            Element rootElement = new Element(EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            Document document = new Document(rootElement);
            Element edXMLEnvelope = new Element("edXMLEnvelope", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            Element headerElement = new Element("edXMLHeader", EdXmlConstant.EDXML_PREFIX, EdXmlConstant.EDXML_URI);
            edXMLEnvelope.addContent(headerElement);
            rootElement.addContent(edXMLEnvelope);
            messageStatus.accumulate(headerElement);
            return document;
        } catch (Exception e) {
            throw new BuildException("Error occurs during building edXML", e);
        }
    }

    public static Content build(MessageStatus status, String path) throws BuildException {
        File tmpFile = Files.createTempDir();
        Document document = buildDocument(status);
        // ghi file ra thu muc
        String fileName = "GetStatus" + "." + status.getFrom().getOrganId();
        Content content;
        try {
            content = BaseXmlUtils.buildContent(document, fileName, "edxml", path);
        } catch (Exception e) {
            throw new BuildException("Error occurs during write content to file", e);
        } finally {
            File[] files = tmpFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    file.delete();
                }
            }

            tmpFile.delete();
        }
        return content;
    }

}
