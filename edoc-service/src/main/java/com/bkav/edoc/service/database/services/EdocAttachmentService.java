package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.EdocAttachmentDaoImpl;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.mineutil.Mapper;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EdocAttachmentService {
    private final EdocAttachmentDaoImpl attachmentDaoImpl = new EdocAttachmentDaoImpl();
    private final Mapper mapper = new Mapper();


    public EdocAttachment addAttachment(EdocAttachment edocAttachment) {
        Session currentSession = attachmentDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            currentSession.persist(edocAttachment);
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
        } finally {
            attachmentDaoImpl.closeCurrentSession(currentSession);
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
        List<EdocAttachment> attachments = attachmentDaoImpl.getAttachmentsByDocumentId(docId);
        return attachments;
    }

    public EdocAttachment getAttachmentById(Long attachmentId) {
        return attachmentDaoImpl.findById(attachmentId);
    }

    public boolean deleteAttachment(EdocAttachment attachment) {
        return attachmentDaoImpl.deleteAttachment(attachment);
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
