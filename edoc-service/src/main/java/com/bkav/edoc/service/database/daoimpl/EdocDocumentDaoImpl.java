package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.dao.EdocDocumentDao;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class EdocDocumentDaoImpl extends RootDaoImpl<EdocDocument, Long> implements EdocDocumentDao {

    public EdocDocumentDaoImpl() {
        super(EdocDocument.class);
    }

    public boolean checkExistDocument(String subject, String codeNumber, String codeNotation, Date promulgationDate,
                                      String fromOrganDomain, String toOrganDomain, List<String> attachmentNames) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.`name` FROM edoc_attachment a INNER JOIN edoc_document d ON a.document_id = d.document_id " +
                    "WHERE d.subject = ? AND d.code_number = ? AND d.code_notation = ? " +
                    "AND d.promulgation_date = ? AND d.from_organ_domain = ? AND d.to_organ_domain = ?");
            Query<String> query = currentSession.createNativeQuery(sql.toString(), String.class);
            query.setParameter(0, subject);
            query.setParameter(1, codeNumber);
            query.setParameter(2, codeNotation);
            query.setParameter(3, promulgationDate);
            query.setParameter(4, fromOrganDomain);
            query.setParameter(5, toOrganDomain);
            List<String> selectedAttachmentNames = query.list();
            return selectedAttachmentNames.containsAll(attachmentNames);
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        } finally {
            currentSession.close();
        }
    }

    public EdocDocument getDocumentByCodeDomain(String docCode, String organDomain) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ed from EdocDocument ed where ed.docCode = :docCode AND (ed.fromOrganDomain = :organDomain or ed.toOrganDomain = :organDomain)");
            Query<EdocDocument> query = session.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("docCode", docCode);
            query.setParameter("organDomain", organDomain);
            List<EdocDocument> documents = query.list();
            if (documents != null && documents.size() > 0) {
                return documents.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("Error find document with document code " + docCode + " in database !!!!!!!!!!!" + " cause " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public EdocDocument getDocumentById(long documentId) {
        Session session = openCurrentSession();
        EdocDocument document = null;
        try {
            session.beginTransaction();
            document = this.findById(documentId);
            if (document == null) {
                LOGGER.error("Not found document with document id " + documentId);

            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error find document with document id " + documentId + " in database !!!!!!!!!!!" + " cause " + Arrays.toString(e.getStackTrace()));
        } finally {
            closeCurrentSession(session);
        }
        return document;
    }

    public List<EdocDocument> selectForDailyCounter(Date date) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ed from EdocDocument ed where DATE(sentDate)=DATE(:counterDate)");
            Query<EdocDocument> query = session.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("counterDate", date);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        } finally {
            closeCurrentSession(session);
        }

    }

    public EdocDocument checkExistDocument(String edXmlDocumentId) {
        Session currentSession = openCurrentSession();
        try {
            LOGGER.info("Check exist document for edxml document id " + edXmlDocumentId + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ed from EdocDocument ed where ed.edXMLDocId = :edXMLDocId");
            Query<EdocDocument> query = currentSession.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("edXMLDocId", edXmlDocumentId);
            List<EdocDocument> documents = query.list();
            if (documents != null && documents.size() > 0) {
                return documents.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("Error check Exist document for edxml document id " + edXmlDocumentId + " cause " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
        }
        return null;
    }

    public EdocDocument searchDocumentByOrganDomainAndCode(String fromOrganDomain, String toOrganDomain, String code) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ed FROM EdocDocument ed where ed.fromOrganDomain = :fromOrganDomain " +
                    "and ed.docCode = :code");
            Query<EdocDocument> query = currentSession.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("fromOrganDomain", toOrganDomain);
            query.setParameter("code", code);
            List<EdocDocument> result = query.list();
            if (result != null && result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error find document by organ domain an code with organ domain "
                    + toOrganDomain + " code " + code + " cause " + Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            closeCurrentSession(currentSession);
        }
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
        Session session = openCurrentSession();
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
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    //KienNDc-InsertDocument
    @Override
    public EdocDocument addNewDocument(EdocDocument edocDocument) {
        persist(edocDocument);
        return edocDocument;
    }

    @Override
    public EdocDocument getDocumentByDocCode(String docCode) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ed FROM EdocDocument ed where ed.docCode = :code");
            Query<EdocDocument> query = currentSession.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("code", docCode);
            List<EdocDocument> result = query.list();
            if (result != null && result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error find document by code " + docCode + " cause " + Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            closeCurrentSession(currentSession);
        }
    }

    public List<EdocDocument> getDocumentsByDocCode(String docCode) {
        Session sessions = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("Select ed from EdocDocument ed where ed.docCode = :docCode");
            Query<EdocDocument> query = sessions.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("docCode", docCode);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(sessions);
        }
        return new ArrayList<>();
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
                LOGGER.error("Successfully delete document  with document with id " + documentId);
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

    public int countReceivedExtDoc(String fromDate, String toDate, boolean received_ext, String organDomain) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("Select ed from EdocDocument ed where ed.receivedExt = :received_ext and date(ed.createDate) > date(:fromDate) and date(ed.createDate) < date(:toDate) and ed.toOrganDomain = :organDomain");
            Query<EdocDocument> query = session.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("received_ext", received_ext);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            query.setParameter("organDomain", organDomain);

            return query.getResultList().size();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return 0;
    }

    public List<Long> getDocCodeByOrganDomain (String fromDate, String toDate, String organDomain) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ed.documentId FROM EdocDocument ed where date(ed.createDate) > date(:fromDate) and date(ed.createDate) < date(:toDate) and ed.toOrganDomain = :organDomain group by ed.documentId");
            Query<Long> query = session.createQuery(sql.toString(), Long.class);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            query.setParameter("organDomain", organDomain);

            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return null;
    }

    public List<EdocDocument> getDocumentByDate (Date date) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("Select ed From EdocDocument ed where date(ed.createDate) = date(:date)");
            Query<EdocDocument> query = session.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("date", date);
            List<EdocDocument> results = query.getResultList();
            if (results.size() > 0)
                return results;
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return new ArrayList<>();
    }

    public List<Date> getDateInRange(Date fromDate, Date toDate) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            if (fromDate == null && toDate == null) {
                sql.append("Select distinct date(ed.createDate) from EdocDocument ed");
                Query<Date> query = session.createQuery(sql.toString(), Date.class);
                return query.getResultList();
            }
            else {
                sql.append("Select distinct date(ed.createDate) from EdocDocument ed where date(ed.createDate) >= :fromDate and date(createDate) <= :toDate");
                Query<Date> query = session.createQuery(sql.toString(), Date.class);
                query.setParameter("fromDate", fromDate);
                query.setParameter("toDate", toDate);
                return query.getResultList();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return new ArrayList<>();
    }

    public List<String> getDocCodeByCounterDate (Date _counterDate) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("Select docCode from EdocDocument where date(createDate) = :_counterDate group by docCode");
            Query<String> query = session.createQuery(sql.toString(), String.class);
            query.setParameter("_counterDate", _counterDate);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return new ArrayList<>();
    }

    public List<EdocDocument> getAllDocumentNotTaken() {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select ed from EdocDocument ed, EdocNotification en, EdocDynamicContact co " +
                    "where ed.documentId = en.document.documentId and ed.toOrganDomain like concat('%', en.receiverId, '%') " +
                    "and en.receiverId = co.domain and co.receiveNotify = 1 and co.agency = 1 and en.taken = 0");
            Query<EdocDocument> query = session.createQuery(sql.toString(), EdocDocument.class);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        String yesterday = "2021-01-26";
        java.sql.Date yes = java.sql.Date.valueOf(yesterday);
        String now = "2021-01-28";
        java.sql.Date no = java.sql.Date.valueOf(now);

        EdocDocumentDaoImpl edocDocumentDao = new EdocDocumentDaoImpl();
        //System.out.println(edocDocumentDao.getDateInRange(yes, no));
        System.out.println(edocDocumentDao.getAllDocumentNotTaken().size());
    }

    private static final Logger LOGGER = Logger.getLogger(EdocDocumentDaoImpl.class);
}

