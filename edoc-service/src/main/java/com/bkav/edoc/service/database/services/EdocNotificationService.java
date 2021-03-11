package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.cache.NotificationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDynamicContactDaoImpl;
import com.bkav.edoc.service.database.daoimpl.EdocNotificationDaoImpl;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.kernel.util.DateUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import com.bkav.edoc.service.util.CommonUtil;
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
     * remove pending document
     *
     * @param domain
     * @param documentId
     */
    public void removePendingDocId(String domain, long documentId) {
        LOGGER.info("------------------------ Prepare remove pending document with document id " + documentId + " : " + domain + " ------------------------");
        // remove in cache
        removePendingDocumentIdInCache(domain, documentId);
        // remove in db
        this.removePendingDocumentId(domain, documentId);
    }

    /**
     * remove pending document in cache
     *
     * @param domain
     * @param documentId
     */
    private void removePendingDocumentIdInCache(String domain, long documentId) {
        List obj = RedisUtil.getInstance().get(RedisKey.getKey(domain,
                RedisKey.GET_PENDING_KEY), List.class);

        if (obj != null) {
            List<Long> oldDocumentIds = CommonUtil.convertToListLong(obj);
            LOGGER.info("--------------------- Before remove document in cache list of document in cache" + oldDocumentIds);
            oldDocumentIds.remove(documentId);
            LOGGER.info("--------------------- Remove pending document in cache for document " + documentId + " organ domain " + domain + " " + oldDocumentIds);
            RedisUtil.getInstance().set(RedisKey.getKey(domain,
                    RedisKey.GET_PENDING_KEY), oldDocumentIds);
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
                    LOGGER.info("Update notification set taken success for notification with document id " + documentId + " : " + edocNotification.getReceiverId());
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

    public List<TelegramMessage> getTelegramMessages() {
        List<TelegramMessage> telegramMessages = new ArrayList<>();
        try {
            //List<EdocNotification> notifications = notificationDaoImpl.getEdocNotifyNotTaken(date);
            List<EdocNotification> notifications = notificationDaoImpl.getEdocNotifyNotTaken();
            for (EdocNotification notification : notifications) {
                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(notification.getReceiverId());
                if (contact != null) {
                    if (contact.getReceiveNotify()) {
                        // check if document not taken after 30m to notification
                        Date createDate = notification.getModifiedDate();
                        Date now = new Date();
                        int diffMin = DateUtil.getMinuteBetween(createDate, now);
                        if (diffMin >= 30) {
                            LOGGER.info("------------------------- Modified Date " + createDate +
                                    " ------------------ organ " + notification.getReceiverId());

                            TelegramMessage telegramMessage = new TelegramMessage();
                            telegramMessage.setReceiverId(notification.getReceiverId());
                            telegramMessage.setReceiverName(contact.getName());
                            telegramMessage.setDocument(notification.getDocument());
                            telegramMessage.setCreateDate(createDate);
                            telegramMessages.add(telegramMessage);
                        }
                    }
                }
            }
            LOGGER.info("------------------------ telegram messages " + telegramMessages.size() + "---------------------------");
            return telegramMessages;
        } catch (Exception e) {
            LOGGER.error(e);
            return telegramMessages;
        }
    }

    public Map<String, Object> getAllDocumentNotTaken(PaginationCriteria paginationCriteria) {
        int totalRecords = 0;
        List<EdocDocument> documents = new ArrayList<>();
        Session session = notificationDaoImpl.openCurrentSession();
        Map<String, Object> map = null;
        LOGGER.info("Get all documents not taken invoke !!!!!!!!!!!!!!!");
        try {
            StoredProcedureQuery storedProcedureQuery = session.createStoredProcedureQuery("GetAllDocumentsNotTaken", EdocDocument.class);
            storedProcedureQuery.registerStoredProcedureParameter("orderBy", String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("keyword", String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("pageIdx", Integer.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("pageSize", Integer.class, ParameterMode.IN);
            //storedProcedureQuery.registerStoredProcedureParameter("fromDate", java.sql.Date.class, ParameterMode.IN);
            //storedProcedureQuery.registerStoredProcedureParameter("toDate", java.sql.Date.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter("totalRecords", Integer.class, ParameterMode.OUT);
            storedProcedureQuery.setParameter("orderBy", paginationCriteria.getOrderBy());
            storedProcedureQuery.setParameter("keyword", paginationCriteria.getSearch());
            storedProcedureQuery.setParameter("pageIdx", paginationCriteria.getPageNumber());
            storedProcedureQuery.setParameter("pageSize", paginationCriteria.getPageSize());

            /*if(fromDate == null || toDate == null){
                java.sql.Date date = null;
                storedProcedureQuery.setParameter("fromDate", date);
                storedProcedureQuery.setParameter("toDate", date);
            } else {
                storedProcedureQuery.setParameter("fromDate", fromDate);
                storedProcedureQuery.setParameter("toDate", toDate);
            }*/
            totalRecords = (Integer) storedProcedureQuery.getOutputParameterValue("totalRecords");

            List list = storedProcedureQuery.getResultList();

            if (list != null && list.size() > 0) {
                for (Object object : list) {
                    EdocDocument document = (EdocDocument) object;
                    documents.add(document);
                }
                map = new HashMap<>();
                LOGGER.info("Has " + documents.size() + " not taken !!!!!!!!!!!");
                map.put("documents", documents);
                map.put("totalDocuments", totalRecords);
                return map;
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("Error get contacts cause " + e.getMessage());
            return map;
        } finally {
            notificationDaoImpl.closeCurrentSession(session);
        }
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -20);
        Date yesterday = cal.getTime();
        Date today = new Date();
        System.out.println(new EdocNotificationService().getTelegramMessages().size());
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
