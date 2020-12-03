package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.EdocAttachmentDaoImpl;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.mineutil.Mapper;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.util.*;

public class EdocAttachmentService {
    private final EdocAttachmentDaoImpl attachmentDaoImpl = new EdocAttachmentDaoImpl();
    private final Mapper mapper = new Mapper();


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
