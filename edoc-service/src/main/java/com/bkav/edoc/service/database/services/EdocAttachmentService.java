package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocAttachmentDaoImpl;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.mineutil.Mapper;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class EdocAttachmentService {
    private final EdocAttachmentDaoImpl attachmentDaoImpl = new EdocAttachmentDaoImpl();
    private final AttachmentGlobalUtil attUtil = new AttachmentGlobalUtil();
    private final Mapper mapper = new Mapper();

    public Set<EdocAttachment> addAttachments(EdocDocument document,
                                              List<Attachment> attachments, List<AttachmentCacheEntry> edocAttachmentCacheEntries) {
        String rootPath = attUtil.getAttachmentPath();
        Calendar cal = Calendar.getInstance();
        String SEPARATOR = EdXmlConstant.SEPARATOR;
        Set<EdocAttachment> edocAttachmentSet = new HashSet<>();
        String domain = document.getFromOrganDomain();
        long docId = document.getDocumentId();
        Session session = attachmentDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            for (int i = 0; i < attachments.size(); i++) {
                Attachment attachment = attachments.get(i);
                String dataPath = domain + SEPARATOR +
                        cal.get(Calendar.YEAR) + SEPARATOR +
                        (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                        cal.get(Calendar.DAY_OF_MONTH) + SEPARATOR +
                        docId + "_" + (i + 1);

                String specPath = rootPath +
                        (rootPath.endsWith(SEPARATOR) ? "" : SEPARATOR) +
                        dataPath;
                long size;
                InputStream is = attachment.getInputStream();
                size = attUtil.saveToFile(specPath, is);
                is.close();
                if (size > 0) {
                    String name = attachment.getName();
                    String type = attachment.getContentType();
                    String organDomain = document.getFromOrganDomain();
                    String toOrganDomain = document.getToOrganDomain();
                    EdocAttachment edocAttachment = new EdocAttachment();
                    edocAttachment.setOrganDomain(organDomain);
                    edocAttachment.setName(name);
                    edocAttachment.setType(type);
                    edocAttachment.setToOrganDomain(toOrganDomain);
                    edocAttachment.setCreateDate(new Date());
                    edocAttachment.setFullPath(dataPath);
                    edocAttachment.setSize(String.valueOf(size));
                    edocAttachment.setDocument(document);
                    attachmentDaoImpl.persist(edocAttachment);
                    AttachmentCacheEntry attachmentCacheEntry = MapperUtil.modelToAttachmentCache(edocAttachment);
                    attachmentCacheEntry.setDocumentId(docId);
                    edocAttachmentCacheEntries.add(attachmentCacheEntry);
                    edocAttachmentSet.add(edocAttachment);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error save attachments for document id " + document.getDocumentId() + " cause " + e.getMessage());
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            attachmentDaoImpl.closeCurrentSession();
        }
        return edocAttachmentSet;
    }

    public EdocAttachment addAttachment(EdocAttachment edocAttachment) {
        Session currentSession = attachmentDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            attachmentDaoImpl.persist(edocAttachment);
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
        } finally {
            attachmentDaoImpl.closeCurrentSession();
        }
        return edocAttachment;
    }

    /**
     * get list edoc attachment
     *
     * @param docId
     * @return
     */
    public List<EdocAttachment> getEdocAttachmentsByDocId(long docId) {
        attachmentDaoImpl.openCurrentSession();

        List<EdocAttachment> attachments = attachmentDaoImpl.getAttachmentsByDocumentId(docId);

        attachmentDaoImpl.closeCurrentSession();
        return attachments;
    }

    public EdocAttachment getAttachmentById(Long attachmentId) {
        attachmentDaoImpl.openCurrentSession();

        EdocAttachment attachment = attachmentDaoImpl.findById(attachmentId);

        attachmentDaoImpl.closeCurrentSession();

        return attachment;
    }

    public boolean deleteAttachment(EdocAttachment attachment) {
        attachmentDaoImpl.openCurrentSession();
        boolean result = attachmentDaoImpl.deleteAttachment(attachment);
        attachmentDaoImpl.closeCurrentSession();
        return result;
    }

    /**
     * get list attachment
     *
     * @param documentId
     * @return
     * @throws IOException
     */
    public List<Attachment> getAttachmentsByDocumentId(long documentId) throws IOException {

        List<EdocAttachment> attachments = attachmentDaoImpl.getAttachmentsByDocumentId(documentId);
        ListIterator<EdocAttachment> iterator = attachments.listIterator();

        List<Attachment> attachmentEntities = new ArrayList<>();

        while (iterator.hasNext()) {

            EdocAttachment attachment = iterator.next();

            Attachment result = mapper.attachmentModelToServiceEntity(attachment);

            attachmentEntities.add(result);

        }
        return attachmentEntities;
    }

    public void updateAttachment(EdocAttachment attachment) {
        attachmentDaoImpl.updateAttachment(attachment);
    }

    private static final Logger LOGGER = Logger.getLogger(EdocAttachmentService.class);

    //KienNDc-InsertAttachment
    public void insertAttachment(EdocAttachment edocAttachment) {
        attachmentDaoImpl.insertAttachments(edocAttachment);
    }
}
