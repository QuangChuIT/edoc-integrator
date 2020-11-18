package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.EdocDocumentDetailDaoImpl;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDocumentDetail;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.Arrays;

public class EdocDocumentDetailService {

    private final EdocDocumentDetailDaoImpl documentDetailDaoImpl = new EdocDocumentDetailDaoImpl();
    private final Gson gson = new Gson();

    /**
     * Add document detail
     *
     * @param messageHeader
     * @param document
     * @return
     */
    public EdocDocumentDetail addDocumentDetail(MessageHeader messageHeader, EdocDocument document) {
        Session currentSession = documentDetailDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();

            // get info of document detail
            EdocDocumentDetail documentDetail = MapperUtil.modelToDocumentDetail(messageHeader);
            documentDetail.setDocument(document);
            documentDetailDaoImpl.persist(documentDetail);
            currentSession.getTransaction().commit();
            return documentDetail;
        } catch (Exception e) {
            log.error("Error save document detail for document id " + document.getDocumentId() + " cause " + e.getMessage());
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
            return null;
        } finally {
            documentDetailDaoImpl.closeCurrentSession();
        }
    }

    public EdocDocumentDetail addDocumentDetail(EdocDocumentDetail edocDocumentDetail) {
        Session session = documentDetailDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            documentDetailDaoImpl.persist(edocDocumentDetail);
            session.getTransaction().commit();
            return edocDocumentDetail;
        } catch (Exception e) {
            log.error("Error save document detail cause " + Arrays.toString(e.getStackTrace()));
            if (session != null) session.getTransaction().rollback();
            return null;
        } finally {
            assert session != null;
            session.close();
        }

    }

    private static final Logger log = Logger.getLogger(EdocDocumentDetailService.class);

}
