package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.cache.NotificationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDynamicContactDaoImpl;
import com.bkav.edoc.service.database.daoimpl.EdocNotificationDaoImpl;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.kernel.util.DateUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

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

    public void updateNotification(EdocNotification notification) {

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

    public List<EmailRequest> getEmailRequestScheduleSend(Date fromDate, Date toDate) {
        List<EmailRequest> emailRequests = new ArrayList<>();
        Session session = notificationDaoImpl.openCurrentSession();
        int count_organ = 0;
        try {
            List<String> receiverIds = notificationDaoImpl.getReceiverIdNotTaken(fromDate, toDate);
            for (String receiverId : receiverIds) {
                if (checkOrganToSendEmail(receiverId)) {
                    EmailRequest emailRequest = new EmailRequest();
                    emailRequest.setReceiverId(receiverId);
                    List<EdocDocument> documents = notificationDaoImpl.getDocumentNotTakenByReceiverId(receiverId);
                    emailRequest.setNumberOfDocument(documents.size());
                    emailRequest.setEdocDocument(documents);
                    emailRequests.add(emailRequest);
                    count_organ++;
                }
            }
            LOGGER.info("Total " + count_organ + " organ need to send warning email!!");
            return emailRequests;
        } catch (Exception e) {
            LOGGER.error(e);
            return emailRequests;
        } finally {
            notificationDaoImpl.closeCurrentSession(session);
        }
    }

    public List<TelegramMessage> getTelegramMessages(Date date) {
        List<TelegramMessage> telegramMessages = new ArrayList<>();
        try {
            List<EdocNotification> notifications = notificationDaoImpl.getEdocNotificationsNotTaken(date);
            for (EdocNotification notification : notifications) {
                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(notification.getReceiverId());
                if (contact.getReceiveNotify()) {
                    // check if document not taken after 30m to notification
                    Date createDate = notification.getModifiedDate();
                    int diffMin = DateUtil.getMinuteBetween(createDate, date);
                    LOGGER.info("------------------------- Modified Date " + createDate +
                            " ---------------------- " + diffMin + " ------- organ " + notification.getReceiverId());
                    if (diffMin >= 30) {
                        TelegramMessage telegramMessage = new TelegramMessage();
                        telegramMessage.setReceiverId(notification.getReceiverId());
                        telegramMessage.setReceiverName(contact.getName());
                        telegramMessage.setDocument(notification.getDocument());
                        telegramMessage.setCreateDate(createDate);
                        telegramMessages.add(telegramMessage);
                    }
                }
            }
            LOGGER.info("------------------------ telegram messages " + telegramMessages.size() + "---------------------------");
            /*List<String> notifications = notificationDaoImpl.getEdocNotificationsNotTaken(date);
            for (String notification : notifications) {
                if(checkOrganReceiveNotify(notification)) {
                    EmailRequest emailRequest = new EmailRequest();
                    emailRequest.setReceiverId(notification);
                    List<EdocDocument> documents = notificationDaoImpl.getDocumentNotTakenByReceiverId(notification);
                    emailRequest.setNumberOfDocument(documents.size());
                    emailRequest.setEdocDocument(documents);
                    emailRequests.add(emailRequest);
                    count_organ++;

             */
            /*int i = 0;
            List<EdocNotification> notifications = notificationDaoImpl.getEdocNotificationsNotTaken(date);
            for (EdocNotification notification : notifications) {
                // check if document not taken after 30m to notification
                if (checkOrganReceiveNotify(notification.getReceiverId())) {
                    Date createDate = notification.getModifiedDate();
                    int diffMin = DateUtil.getMinuteBetween(createDate, date);
                    if (diffMin >= 30) {
                        TelegramMessage telegramMessage = new TelegramMessage();
                        telegramMessage.setReceiverId(notification.getReceiverId());
                        telegramMessage.setDocument(notification.getDocument());
                        telegramMessage.setCreateDate(createDate);
                        telegramMessages.add(telegramMessage);
                        i++;
                    }
                }
            }
            System.out.println(i);*/
            return telegramMessages;
        } catch (Exception e) {
            LOGGER.error(e);
            return telegramMessages;
        }
    }


    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        cal.add(Calendar.HOUR, 9);
        Date yesterday = cal.getTime();
        System.out.println(new EdocNotificationService().getTelegramMessages(yesterday).size());
    }

    private boolean checkOrganToSendEmail(String organId) {
        boolean result = false;
        try {
            EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(organId);
            if (edocDynamicContact != null) {
                result = edocDynamicContact.getAgency();
            }
        } catch (Exception e) {
            LOGGER.error("Error check organ to stat cause " + e);
        }
        return result;
    }

    public boolean checkExistNotification(String organDomain, long documentId) {
        return notificationDaoImpl.checkExistNotification(organDomain, documentId);
    }

    private static final Logger LOGGER = Logger.getLogger(EdocNotificationService.class);

}
