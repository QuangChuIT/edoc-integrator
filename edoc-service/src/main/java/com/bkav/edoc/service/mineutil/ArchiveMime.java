/**
 *
 */
package com.bkav.edoc.service.mineutil;

import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.body.Manifest;
import com.bkav.edoc.service.xml.base.body.Reference;
import com.bkav.edoc.service.xml.base.util.UUidUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import javax.activation.DataHandler;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveMime {

    public ArchiveMime() {
    }

    /**
     *
     * @param envelope
     * @param attachmentsByEntity
     * @return
     * @throws Exception
     */
    public Map<String, Object> createMime(Document envelope,
                                          List<Attachment> attachmentsByEntity) throws Exception {

        Map<String, Object> map = new HashMap<>();

        Map<String, String> attachmentIds = xmlUtil.getAttachmentIds(envelope);

        Attachments attachments = null;
        long dataAttSize = 0L;
        // add attachment to attachments of service
        if (attachmentsByEntity != null) {
            attachments = new Attachments();
            for (Attachment attachment : attachmentsByEntity) {
                InputStream attStream = attachment.getInputStream();
                if (attStream != null) {
                    // create content id for attachment
                    String contentId = RandomUtil.randomId();

                    String oldContentId = "";

                    if (attachmentIds != null) {
                        oldContentId = attachmentIds.get(attachment.getName());
                    }

                    if (oldContentId != null && oldContentId.length() > 0) {
                        contentId = oldContentId;
                    }

                    File attachFile = attachment.getContent();
                    String contentType = attachment.getContentType();

                    // TODO: Encode file
                    String contentTransferEncoded = BaseEncoding.base64().encode(ByteStreams.toByteArray(attStream));
                    attStream.close();

                    DataHandler data = new DataHandler(contentTransferEncoded, contentType);

                    attachments.addDataHandler(contentId, data);

                    dataAttSize += attachFile.length();

                } else {
                    if (attachment.getName().length() > 0) {
                        LOGGER.error("Can't read attachment, input stream of attachment null of attachment with name: "
                                + attachment.getName());
                    }
                }

            }
        }

        map.put(StringPool.ENVELOPE_SAVED_KEY, envelope);
        map.put(StringPool.ATTACHMENT_KEY, attachments);
        map.put(StringPool.ATTACHMENT_SIZE_KEY, dataAttSize);
        return map;
    }


    /**
     *
     * @param ed
     * @param attachmentsByEntity
     * @return
     * @throws Exception
     */
    public Map<String, Object> createMime(Ed ed,
                                          List<Attachment> attachmentsByEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();

        Document bodyChildDocument;

        Document messageHeaderDocument;

        Document traceHeaderDocument;

        Attachments attachments = null;

        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMNamespace ns = factoryOM.createOMNamespace(
                StringPool.TARGET_NAMESPACE, StringPool.EDXML_PREFIX);

        messageHeaderDocument = xmlUtil.getMessHeaderDoc((MessageHeader) ed.getHeader()
                .getMessageHeader(), ns);
        LOGGER.info("Success convert message header in create mine with attachment size " + attachmentsByEntity.size());
        traceHeaderDocument = xmlUtil.getTraceHeaderDoc(ed.getHeader()
                .getTraceHeaderList(), ns);
        LOGGER.info("Success convert trace header list in create mine with attachment size " + attachmentsByEntity.size());
        List<Reference> references = new ArrayList<>();

        long dataAttSize = 0L;

        // Create attachment
        if (attachmentsByEntity != null) {
            attachments = new Attachments();
            for (Attachment attachment : attachmentsByEntity) {
                InputStream attStream = attachment.getInputStream();
                if (attStream != null) {
                    // create content id for attachment
                    String contentId = UUidUtils.generate();

                    String contentType = attachment.getContentType();

                    File attachFile = attachment.getContent();

                    // TODO: Encode file
                    String contentTransferEncoded = BaseEncoding.base64().encode(ByteStreams.toByteArray(attStream));
                    attStream.close();
                    DataHandler data = new DataHandler(contentTransferEncoded, contentType);

                    attachments.addDataHandler(contentId, data);

                    dataAttSize += attachFile.length();

                    // Create reference on body
                    Reference reference = new Reference();
                    reference.setContentId("cid:" + contentId);
                    reference.setAttachmentName(attachment.getName());
                    reference.setContentType(contentType);
                    reference.setDescription(attachment.getDescription());
                    references.add(reference);

                } else {
                    if (attachment.getName().length() > 0) {
                        LOGGER.error("Can't read attachment, input stream of attachment null of attachment with name: "
                                + attachment.getName());
                    }
                }

            }
        }

        Manifest manifest = new Manifest();

        manifest.setReferences(references);

        bodyChildDocument = xmlUtil.getBodyChildDoc(manifest, ns);

        map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

        map.put(StringPool.MESSAGE_HEADER_KEY, messageHeaderDocument);

        map.put(StringPool.TRACE_HEADER_KEY, traceHeaderDocument);

        map.put(StringPool.ATTACHMENT_KEY, attachments);

        map.put(StringPool.ATTACHMENT_SIZE_KEY, dataAttSize);

        return map;

    }

    private final XmlUtil xmlUtil = new XmlUtil();
    private static final Logger LOGGER = Logger.getLogger(ArchiveMime.class);
}
