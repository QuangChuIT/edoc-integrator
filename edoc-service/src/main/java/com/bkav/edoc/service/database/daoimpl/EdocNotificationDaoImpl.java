package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocNotificationDao;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocNotification;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EdocNotificationDaoImpl extends RootDaoImpl<EdocNotification, Long> implements EdocNotificationDao {

    public EdocNotificationDaoImpl() {
        super(EdocNotification.class);
    }

    /**
     * get document id by domain
     *
     * @param organId
     * @return
     */
    public List<Long> getDocumentIdsByOrganId(String organId) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en.document.id FROM EdocNotification en where en.receiverId=:receiverId and en.taken=:taken");
            Query<Long> query = currentSession.createQuery(sql.toString(), Long.class);
            query.setParameter("receiverId", organId);
            query.setParameter("taken", false);
            return query.list();
        } catch (Exception e) {
            LOGGER.error("Error when get document pending for organ " + organId + " cause " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
        }
        return null;
    }

    /**
     * check allow of this domain with document
     *
     * @param documentId
     * @param organId
     * @return
     */
    public boolean checkAllowWithDocument(long documentId, String organId) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en.document.id FROM EdocNotification en where en.receiverId=:receiverId and en.document.id=:documentId");
            Query<Long> query = currentSession.createQuery(sql.toString(), Long.class);
            query.setParameter("receiverId", organId);
            query.setParameter("documentId", documentId);
            List<Long> result = query.list();
            return result != null && result.size() != 0;
        } catch (Exception e) {
            LOGGER.error("Error check allow document with document id " + documentId + " organId " + organId + " cause " + Arrays.toString(e.getStackTrace()));
            return false;
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
        }
    }

    public List<EdocNotification> getByOrganAndDocumentId(long documentId, String organDomain) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en FROM EdocNotification en where en.receiverId=:receiverId and en.document.id=:documentId");
            Query<EdocNotification> query = session.createQuery(sql.toString(), EdocNotification.class);
            query.setParameter("receiverId", organDomain);
            query.setParameter("documentId", documentId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Error get edoc notification by organ " + organDomain + " document id " + documentId + " cause " + e.getMessage());
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    public void setNotificationTaken(long documentId, String organId) throws SQLException {
        Session currentSession = openCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE EdocNotification en SET en.taken=:taken where en.receiverId=:receiverId and en.document.id=:documentId");
        Query query = currentSession.createQuery(sql.toString());
        query.setParameter("taken", true);
        query.setParameter("receiverId", organId);
        query.setParameter("documentId", documentId);
        query.executeUpdate();
    }

    @Override
    public List<String> getReceiverIdNotTaken(Date fromDate, Date toDate) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en.receiverId FROM EdocNotification en WHERE en.taken=:taken and " +
                    "(DATE(en.modifiedDate) >= DATE(:fromDate) and DATE(en.modifiedDate) <= DATE(:toDate)) GROUP BY en.receiverId");
            Query<String> query = session.createQuery(sql.toString(), String.class);
            query.setParameter("taken", false);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    public List<EdocDocument> getDocumentNotTakenByReceiverId(String receiverId) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en.document FROM EdocNotification en WHERE en.receiverId=:receiverId and en.taken=:taken");
            Query<EdocDocument> query = session.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("receiverId", receiverId);
            query.setParameter("taken", false);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EdocNotificationDaoImpl.class);
}
