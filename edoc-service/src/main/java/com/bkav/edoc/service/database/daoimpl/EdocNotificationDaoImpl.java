package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocNotificationDao;
import com.bkav.edoc.service.database.entity.EdocNotification;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.Arrays;
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
            Query query = currentSession.createQuery(sql.toString());
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
        Session currentSession = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT en.document.id FROM EdocNotification en where en.receiverId=:receiverId and en.document.id=:documentId");
        Query<Long> query = currentSession.createQuery(sql.toString());
        query.setParameter("receiverId", organId);
        query.setParameter("documentId", documentId);
        List<Long> result = query.list();
        return result != null && result.size() != 0;
    }

    public EdocNotification getByOrganAndDocumentId(long documentId, String organDomain) {
        Session session = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT en FROM EdocNotification en where en.receiverId=:receiverId and en.document.id=:documentId");
        Query query = session.createQuery(sql.toString());
        query.setParameter("receiverId", organDomain);
        query.setParameter("documentId", documentId);
        Object resultObj = query.getSingleResult();
        if (resultObj != null) {
            return (EdocNotification) resultObj;
        }
        return null;
    }

    public void setNotificationTaken(long documentId, String organId) throws SQLException {
        Session currentSession = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE EdocNotification en SET en.taken=:taken where en.receiverId=:receiverId and en.document.id=:documentId");
        Query query = currentSession.createQuery(sql.toString());
        query.setParameter("taken", true);
        query.setParameter("receiverId", organId);
        query.setParameter("documentId", documentId);
        query.executeUpdate();
    }

    private static final Logger LOGGER = Logger.getLogger(EdocNotificationDaoImpl.class);
}
