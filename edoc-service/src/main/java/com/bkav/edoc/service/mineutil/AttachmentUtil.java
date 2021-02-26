/**
 *
 */
package com.bkav.edoc.service.mineutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import javax.activation.DataHandler;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Report;
import org.apache.axiom.attachments.Attachments;
import org.apache.axis2.context.MessageContext;
import org.w3c.dom.Document;

import com.bkav.edoc.service.commonutil.Checker;

public class AttachmentUtil {
    private final Checker _checker;
    private static final ExtractMime extractMime = new ExtractMime();

    public AttachmentUtil() {
        _checker = new Checker();
    }


    public List<Attachment> getAttachments(Document envelope,
                                           Map<String, Object> attachments) {

        List<Attachment> attachmentsEntity = new ArrayList<>();

        Iterator<String> iter = attachments.keySet().iterator();

        Map<String, String> attNames = extractMime.getAttachmentName(envelope);

        while (iter.hasNext()) {
            String contentId = iter.next();

            DataHandler data = (DataHandler) attachments.get(contentId);

            Attachment attachmentEntity = extractMime.getAttachment(
                    attNames.get(contentId), data);

            attachmentsEntity.add(attachmentEntity);
        }
        attachments.clear();
        return attachmentsEntity;
    }

    public Report checkAllowAttachment(Document soapEnv,
                                       Map<String, Object> attachments) throws XMLStreamException,
            XPathExpressionException {
        Report report = new Report(true, null);
        XmlUtil xmlUtil = new XmlUtil();

        Iterator<Entry<String, Object>> iter = attachments.entrySet()
                .iterator();
        Entry<String, Object> entry;
        while (iter.hasNext()) {

            entry = iter.next();

            String contentId = entry.getKey();

            DataHandler attachment = (DataHandler) entry.getValue();

            String contentType = attachment.getContentType();

            String name = xmlUtil.getAttachmentName(soapEnv, contentId);

            report = _checker.checkAllowAttachment(name, contentType, "base64");

            if (!report.isIsSuccess()) {
                break;
            }

        }
        return report;
    }

    public Map<String, Object> GetAttachmentDocsByContext(MessageContext messageContext) {
        Attachments attachMap = messageContext.getAttachmentMap();

        Map<String, Object> attachments = new HashMap<>();

        // Get Attachment Ids
        String[] ids = attachMap.getAllContentIDs();

        String soapPartId = attachMap.getSOAPPartContentID();

        for (String id : ids) {
            if (!id.equals(soapPartId)) {
                DataHandler data = attachMap.getDataHandler(id);
                attachments.put(id, data);
            }
        }

        return attachments;
    }


}
