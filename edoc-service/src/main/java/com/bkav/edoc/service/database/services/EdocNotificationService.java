package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.cache.NotificationCacheEntry;
import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDynamicContactDaoImpl;
import com.bkav.edoc.service.database.daoimpl.EdocNotificationDaoImpl;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.*;

public class EdocNotificationService {
    private final EdocNotificationDaoImpl notificationDaoImpl = new EdocNotificationDaoImpl();
    private final EdocDynamicContactDaoImpl edocDynamicContactDao = new EdocDynamicContactDaoImpl();

    public void addNotification(EdocNotification edocNotification) {
        Session currentSession = notificationDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            currentSession.save(edocNotification);
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
        } finally {
            notificationDaoImpl.closeCurrentSession(currentSession);
        }
    }

    /**
     * get document id by domain
     *
     * @param organId
     * @return
     */
    public List<Long> getDocumentIdsByOrganId(String organId) {
        return notificationDaoImpl.getDocumentIdsByOrganId(organId);
    }

    /**
     * check allow of this domain with document
     *
     * @param documentId
     * @param organId
     * @return
     */
    public boolean checkAllowWithDocument(long documentId, String organId) {

        return notificationDaoImpl.checkAllowWithDocument(documentId, organId);
    }

    public List<EdocNotification> findAll() {
        return notificationDaoImpl.findAll();
    }

    public void removePendingDocumentId(String domain, long documentId) {
        try {
            //notificationDaoImpl.setNotificationTaken(documentId, domain);
            List<EdocNotification> edocNotifications = notificationDaoImpl.getByOrganAndDocumentId(documentId, domain);
            if (edocNotifications.size() > 0) {
                for (EdocNotification edocNotification : edocNotifications) {
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
                }

            } else {
                LOGGER.error("M.RemovePending. Not found edoc_notification to remove pending by document " + documentId + " receiver id " + domain);
            }
        } catch (Exception e) {
            LOGGER.error("Error remove document pending for organ domain " + domain
                    + " and document " + documentId + " cause " + Arrays.toString(e.getStackTrace()));
        }
    }

    public List<EmailRequest> getEmailRequestScheduleSend() {
        List<EmailRequest> emailRequests = new ArrayList<>();
        Session session = notificationDaoImpl.openCurrentSession();
        try {
            List<String> receiverIds = notificationDaoImpl.getReceiverIdNotTaken();
            for (String receiverId: receiverIds) {
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setReceiverId(receiverId);
                List<EdocDocument> documents = notificationDaoImpl.getDocumentNotTakenByReceiverId(receiverId);
                //emailRequest.setNumberOfDocument(documents.size());
                emailRequest.setEdocDocument(documents);
                emailRequests.add(emailRequest);
            }
            //System.out.println(emailRequests.size());
            return emailRequests;
        } catch (Exception e) {
            LOGGER.error(e);
            return emailRequests;
        } finally {
            notificationDaoImpl.closeCurrentSession(session);
        }
    }

    public static void main(String[] args) {
        EdocNotificationService edocNotificationService = new EdocNotificationService();
//        edocNotificationService.removePendingDocumentId("000.01.32.H53", 285);
        edocNotificationService.getEmailRequestScheduleSend();
    }

    private static final Logger LOGGER = Logger.getLogger(EdocNotificationService.class);

}
