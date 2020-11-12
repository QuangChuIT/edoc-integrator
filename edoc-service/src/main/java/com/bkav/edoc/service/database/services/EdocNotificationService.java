package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.cache.NotificationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocNotificationDaoImpl;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.xml.base.header.Organization;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.*;

public class EdocNotificationService {
    private final EdocNotificationDaoImpl notificationDaoImpl = new EdocNotificationDaoImpl();

    /**
     * Add notifications
     *
     * @param toOrgans
     * @param duaDate
     * @param document
     * @return
     */
    public Set<EdocNotification> addNotifications(List<Organization> toOrgans, Date duaDate, EdocDocument document) {
        Session currentSession = notificationDaoImpl.openCurrentSession();
        Set<EdocNotification> notifications = new HashSet<>();
        try {
            currentSession.beginTransaction();
            // Insert Notification
            for (Organization to : toOrgans) {
                EdocNotification notification = new EdocNotification();
                Date currentDate = new Date();
                notification.setDateCreate(currentDate);
                notification.setModifiedDate(currentDate);
                notification.setSendNumber(0);
                notification.setDueDate(duaDate);
                notification.setReceiverId(to.getOrganId());
                notification.setDocument(document);
                notification.setTaken(false);
                notificationDaoImpl.persist(notification);
                LOGGER.info("Save edoc notification success for document " + document.getDocumentId() + " and code " + document.getDocCode());
                notifications.add(notification);
            }
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error save notification for document id " +
                    document.getDocumentId() + " doc code " + document.getDocCode() + " cause " + Arrays.toString(e.getStackTrace()));
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
        } finally {
            notificationDaoImpl.closeCurrentSession();
        }
        return notifications;
    }

    public void addNotification(EdocNotification edocNotification) {
        Session currentSession = notificationDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            notificationDaoImpl.persist(edocNotification);
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
        } finally {
            notificationDaoImpl.closeCurrentSession();
        }
    }

    /**
     * get document id by domain
     *
     * @param organId
     * @return
     */
    public List<Long> getDocumentIdsByOrganId(String organId) {
        List<Long> notificationIds = notificationDaoImpl.getDocumentIdsByOrganId(organId);
        return notificationIds;
    }

    /**
     * check allow of this domain with document
     *
     * @param documentId
     * @param organId
     * @return
     */
    public boolean checkAllowWithDocument(long documentId, String organId) {
        notificationDaoImpl.openCurrentSession();

        boolean checkAllow = notificationDaoImpl.checkAllowWithDocument(documentId, organId);

        notificationDaoImpl.closeCurrentSession();
        return checkAllow;
    }

    public List<EdocNotification> findAll() {
        notificationDaoImpl.openCurrentSession();
        List<EdocNotification> result = notificationDaoImpl.findAll();
        notificationDaoImpl.closeCurrentSession();
        return result;
    }

    public void removePendingDocumentId(String domain, long documentId) {
        Session currentSession = notificationDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            //notificationDaoImpl.setNotificationTaken(documentId, domain);
            EdocNotification edocNotification = notificationDaoImpl.getByOrganAndDocumentId(documentId, domain);
            edocNotification.setTaken(true);
            edocNotification.setModifiedDate(new Date());
            notificationDaoImpl.saveOrUpdate(edocNotification);
            String cacheKey = MemcachedKey.getKey(String.valueOf(documentId), MemcachedKey.DOCUMENT_KEY);
            DocumentCacheEntry documentCacheUpdate = (DocumentCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
            if (documentCacheUpdate != null) {
                List<NotificationCacheEntry> notificationCacheEntries = documentCacheUpdate.getNotifications();
                if (notificationCacheEntries.size() > 0) {
                    //check has old notification
                    notificationCacheEntries.removeIf(entry -> entry.getNotificationId().equals(edocNotification.getNotificationId()));
                }
                NotificationCacheEntry notificationCacheEntry = MapperUtil.modelToNotificationCache(edocNotification);
                notificationCacheEntries.add(notificationCacheEntry);
                documentCacheUpdate.setNotifications(notificationCacheEntries);
                MemcachedUtil.getInstance().update(cacheKey, MemcachedKey.SEND_DOCUMENT_TIME_LIFE, documentCacheUpdate);
            }
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            currentSession.getTransaction().rollback();
        } finally {
            notificationDaoImpl.closeCurrentSession();
        }
    }

    public EdocNotification getByOrganAndDocumentId(long documentId, String organId) {
        notificationDaoImpl.openCurrentSession();
        EdocNotification notification = notificationDaoImpl.getByOrganAndDocumentId(documentId, organId);
        notificationDaoImpl.closeCurrentSession();
        return notification;
    }

    public static void main(String[] args) {
        EdocNotificationService edocNotificationService = new EdocNotificationService();
        edocNotificationService.removePendingDocumentId("000.01.32.H53", 285);
    }

    private static final Logger LOGGER = Logger.getLogger(EdocNotificationService.class);
}
