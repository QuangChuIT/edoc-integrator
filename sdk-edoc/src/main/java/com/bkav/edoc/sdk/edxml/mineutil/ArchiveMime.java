package com.bkav.edoc.sdk.edxml.mineutil;

import com.bkav.edoc.sdk.edxml.entity.Attachment;
import com.bkav.edoc.sdk.edxml.entity.Manifest;
import com.bkav.edoc.sdk.edxml.entity.Reference;
import com.bkav.edoc.sdk.edxml.entity.env.Envelop;
import com.bkav.edoc.sdk.edxml.util.UUidUtils;
import com.bkav.edoc.sdk.edxml.util.XmlUtil;
import com.bkav.edoc.sdk.util.StringPool;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.w3c.dom.Document;

import javax.activation.DataHandler;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveMime {
    /**
     * @param envelop
     * @param attachmentsByEntity
     * @return
     * @throws Exception
     */
    public Map<String, Object> createMime(Envelop envelop,
                                          List<Attachment> attachmentsByEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();

        org.w3c.dom.Document bodyChildDocument;

        org.w3c.dom.Document messageHeaderDocument;

        Document traceHeaderDocument;

        Attachments attachments = null;

        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMNamespace ns = factoryOM.createOMNamespace(
                StringPool.TARGET_NAMESPACE, StringPool.EDXML_PREFIX);

        messageHeaderDocument = xmlUtil.getMessHeaderDoc(envelop.getHeader()
                .getMessageHeader(), ns);
        System.out.println("Success convert message header in create mine with attachment size " + attachmentsByEntity.size() + " !!!!!!!!!!!!!!!!!!!!!");
        traceHeaderDocument = xmlUtil.getTraceHeaderDoc(envelop.getHeader()
                .getTraceHeaderList(), ns);
        System.out.println("Success convert trace header list in create mine with attachment size " + attachmentsByEntity.size() + " !!!!!!!!!!!!!!!!!!!!!!!!!");
        List<Reference> references = new ArrayList<>();

        long dataAttSize = 0L;

        // Create attachment
        if (attachmentsByEntity.size() > 0) {
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
                        System.out.println("Can't read attachment, input stream of attachment null of attachment with name: "
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
}
