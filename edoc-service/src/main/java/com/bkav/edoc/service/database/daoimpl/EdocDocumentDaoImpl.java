package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocDocumentDao;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.HibernateUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EdocDocumentDaoImpl extends RootDaoImpl<EdocDocument, Long> implements EdocDocumentDao {

    public EdocDocumentDaoImpl() {
        super(EdocDocument.class);
    }

    public boolean checkExistDocument(String subject, String codeNumber, String codeNotation, Date promulgationDate,
                                      String fromOrganDomain, String toOrganDomain, List<String> attachmentNames) {
        Session currentSession = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.`name` FROM edoc_attachment a INNER JOIN edoc_document d ON a.document_id = d.document_id " +
                "WHERE d.subject = ? AND d.code_number = ? AND d.code_notation = ? " +
                "AND d.promulgation_date = ? AND d.from_organ_domain = ? AND d.to_organ_domain = ?");
        Query<String> query = currentSession.createNativeQuery(sql.toString());
        query.setParameter(0, subject);
        query.setParameter(1, codeNumber);
        query.setParameter(2, codeNotation);
        query.setParameter(3, promulgationDate);
        query.setParameter(4, fromOrganDomain);
        query.setParameter(5, toOrganDomain);
        List<String> selectedAttachmentNames = query.list();

        return selectedAttachmentNames.containsAll(attachmentNames);
    }

    public List<EdocDocument> selectForDailyCounter(Date date) {
        Session session = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ed from EdocDocument ed where DATE(sentDate)=DATE(:counterDate)");
        Query<EdocDocument> query = session.createQuery(sql.toString());
        query.setParameter("counterDate", date);
        return query.getResultList();
    }

    public EdocDocument checkExistDocument(String edXmlDocumentId) {
        Session currentSession = openCurrentSession();
        try {
            LOGGER.info("Check exist document for edxml document id " + edXmlDocumentId + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ed from EdocDocument ed where ed.edXMLDocId = :edXMLDocId");
            Query<EdocDocument> query = currentSession.createQuery(sql.toString());
            query.setParameter("edXMLDocId", edXmlDocumentId);
            List<EdocDocument> documents = query.list();
            if (documents != null && documents.size() > 0) {
                return documents.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("Error check Exist document for edxml document id " + edXmlDocumentId);
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
        }
        return null;
    }

    public EdocDocument searchDocumentByOrganDomainAndCode(String toOrganDomain, String code) {
        Session currentSession = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ed FROM EdocDocument ed where ed.fromOrganDomain = :fromOrganDomain " +
                "and ed.docCode = :code");
        Query<EdocDocument> query = currentSession.createQuery(sql.toString());
        query.setParameter("fromOrganDomain", toOrganDomain);
        query.setParameter("code", code);
        List<EdocDocument> result = query.list();
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<EdocDocument> findByOranDomain(String organId, String mode, int start, int size) {
        StringBuilder sql = new StringBuilder();
        Query<EdocDocument> query = null;
        Session currentSession = getCurrentSession();
        switch (mode) {
            case EdXmlConstant.INBOX_MODE:
                sql.append("SELECT ed from EdocDocument ed where ed.toOrganDomain like :organDomain and ed.draft=false");
                query = currentSession.createQuery(sql.toString());
                query.setParameter("organDomain", "%" + organId + "%");
                break;
            case EdXmlConstant.OUTBOX:
                sql.append("SELECT ed from EdocDocument ed where ed.fromOrganDomain = :organDomain and ed.draft=false");
                query = currentSession.createQuery(sql.toString());
                query.setParameter("organDomain", organId);
                break;
            case EdXmlConstant.INBOX_NOT_RECEIVED:
                sql.append("SELECT ed FROM EdocDocument ed INNER JOIN ed.notifications n WHERE n.taken = 0 " +
                        "and n.receiverId = :organDomain and ed.draft=false");
                query = currentSession.createQuery(sql.toString());
                query.setParameter("organDomain", organId);
                break;
            case EdXmlConstant.INBOX_RECEIVED:
                sql.append("SELECT ed FROM EdocDocument ed INNER JOIN ed.notifications n WHERE n.taken = 1 " +
                        "and n.receiverId = :organDomain and ed.draft=false");
                query = currentSession.createQuery(sql.toString());
                query.setParameter("organDomain", organId);
                break;
            case EdXmlConstant.DRAFT:
                sql.append("SELECT ed from EdocDocument ed WHERE ed.fromOrganDomain = :organDomain AND ed.draft=true");
                query = currentSession.createQuery(sql.toString());
                query.setParameter("organDomain", organId);
                break;
        }
        if (query != null) {
            query.setFirstResult(start);
            query.setMaxResults(size);
            return query.getResultList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<EdocDocument> getAllDocumentList() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<EdocDocument> query = builder.createQuery(EdocDocument.class);
        Root<EdocDocument> root = query.from(EdocDocument.class);
        query.select(root);
        Query<EdocDocument> q = session.createQuery(query);
        List<EdocDocument> documents = q.getResultList();
        session.getTransaction().commit();
        session.close();
        return documents;
    }

    @Override
    public List<EdocDocument> getDocuments(String organId, int start, int size) {
        Session session = getCurrentSession();
        try {
            StoredProcedureQuery storedProcedureQuery = session.createStoredProcedureQuery("GetDocumentsInbox", EdocDocument.class);
            storedProcedureQuery.registerStoredProcedureParameter("organId", String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("page", Integer.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("size", Integer.class, ParameterMode.IN);

            storedProcedureQuery.setParameter("organId", organId);
            storedProcedureQuery.setParameter("page", start);
            storedProcedureQuery.setParameter("size", size);

            return storedProcedureQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //KienNDc-InsertDocument
    @Override
    public EdocDocument addNewDocument(EdocDocument edocDocument) {
        try (Session session = openCurrentSession()) {
            session.beginTransaction();
            persist(edocDocument);
            session.flush();
            session.getTransaction().commit();
            return edocDocument;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean removeDocument(long documentId) {
        Session session = openCurrentSession();
        boolean result;
        try {
            session.beginTransaction();
            EdocDocument document = this.findById(documentId);
            if (document == null) {
                LOGGER.error("Error delete document not found document with id " + documentId);
                result = false;
            } else {
                session.delete(document);
                session.getTransaction().commit();
                result = true;
            }
        } catch (Exception e) {
            result = false;
            LOGGER.error("Error delete document with id " + documentId + " cause " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    private static final Logger LOGGER = Logger.getLogger(EdocDocumentDaoImpl.class);
}

