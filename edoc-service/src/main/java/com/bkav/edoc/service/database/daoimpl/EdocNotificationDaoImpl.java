package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocNotificationDao;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.*;

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
            LOGGER.info("---------------Get Pending document has size: " + query.list().size());
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

    public List<EdocDocument> getDocumentByOrganId(String organId) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en.document FROM EdocNotification en where en.receiverId=:receiverId and en.taken=:taken");
            Query<EdocDocument> query = currentSession.createQuery(sql.toString(), EdocDocument.class);
            query.setParameter("receiverId", organId);
            query.setParameter("taken", false);
            LOGGER.info("---------------Get Pending document has size: " + query.list().size());
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
        sql.append("UPDATE EdocNotification en SET en.taken=:taken, en.modifiedDate=:modifiedDate where en.receiverId=:receiverId and en.document.id=:documentId");
        Query query = currentSession.createQuery(sql.toString());
        query.setParameter("taken", true);
        query.setParameter("modifiedDate", new Date());
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
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    @Override
    public List<EdocNotification> getEdocNotificationsNotTaken(Date date) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en FROM EdocNotification en WHERE en.taken=:taken and " +
                    "DATE(en.modifiedDate) = DATE(:date) and DATE(en.dateCreate) = DATE(:date)");
            Query<EdocNotification> query = session.createQuery(sql.toString(), EdocNotification.class);
            query.setParameter("taken", false);
            query.setParameter("date", date);
            List<EdocNotification> notifications = query.getResultList();
            if (notifications != null) {
                LOGGER.info("Get success list document not taken from date " + date + " with size " + notifications.size());
                return notifications;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            LOGGER.error("Error get edoc notification not taken cause " + e.getMessage());
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    public List<EdocNotification> getEdocNotifyNotTaken() {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT en FROM EdocNotification en WHERE en.taken=:taken");
            Query<EdocNotification> query = session.createQuery(sql.toString(), EdocNotification.class);
            query.setParameter("taken", false);
            List<EdocNotification> notifications = query.getResultList();
            if (notifications != null) {
                LOGGER.info("Get success list document not taken with size " + notifications.size());
                return notifications;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            LOGGER.error("Error get edoc notification not taken cause " + e.getMessage());
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
            List<EdocDocument> result = query.getResultList();
            if (result != null) {
                return result;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    public boolean checkExistNotification(String organDomain, long documentId) {
       Session session = openCurrentSession();
       try {
           StringBuilder sql = new StringBuilder();
           sql.append("Select en from EdocNotification en where en.receiverId = :organDomain and en.document.documentId =:documentId");
           Query<EdocNotification> query = session.createQuery(sql.toString(), EdocNotification.class);
           query.setParameter("organDomain", organDomain);
           query.setParameter("documentId", documentId);
           if (query.getResultList().size() > 0)
               return true;
       } catch (Exception e) {
           LOGGER.error(e);
       } finally {
           closeCurrentSession(session);
       }
       return false;
    }

    /*public List<EdocNotification> getEdocNotificationNotTaken(PaginationCriteria paginationCriteria) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("Select en from EdocNotification en, EdocDynamicContact ed where" +
                    " en.receiverId = ed.domain and ed.receiveNotify = 1 and en.taken = :taken order by :order");
            Query<EdocNotification> query = session.createQuery(sql.toString(), EdocNotification.class);
            query.setParameter("taken", false);
            query.setParameter("order", paginationCriteria.getOrderBy());
            int pageNumber = paginationCriteria.getPageNumber();
            int pageSize = paginationCriteria.getPageSize();
            query.setFirstResult(pageNumber);
            query.setMaxResults(pageSize);
            List<EdocNotification> edocNotification = query.getResultList();
            if (edocNotification.size() > 0)
                return edocNotification;
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return new ArrayList<>();
    }

    public Long countEdocNotificationNotTaken() {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("Select count(1) from EdocNotification en where en.taken = :taken");
            Query<Long> query = session.createQuery(sql.toString(), Long.class);
            query.setParameter("taken", false);
            return query.getSingleResult();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return 0L;
    }*/

    /*public static void main(String[] args) {
        System.out.println(new EdocNotificationDaoImpl().countEdocNotificationNotTaken());
    }*/

    private static final Logger LOGGER = Logger.getLogger(EdocNotificationDaoImpl.class);
}
