package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.commonutil.XmlGregorianCalendarUtil;
import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDocumentDaoImpl;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.util.AppUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.executor.ExecutorServiceUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.mineutil.Mapper;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.resource.QueryString;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;

public class EdocDocumentService {
    private final EdocDocumentDaoImpl documentDaoImpl = new EdocDocumentDaoImpl();
    private final EdocDocumentDetailService documentDetailService = new EdocDocumentDetailService();
    private final EdocTraceHeaderListService traceHeaderListService = new EdocTraceHeaderListService();
    private final EdocAttachmentService attachmentService = new EdocAttachmentService();
    private final EdocNotificationService notificationService = new EdocNotificationService();
    private final Mapper mapper = new Mapper();
    private final Checker checker = new Checker();

    public EdocDocumentService() {

    }

    public boolean deleteDocument(long documentId) {
        return documentDaoImpl.removeDocument(documentId);
    }

    public EdocDocument getDocument(long documentId) {
        documentDaoImpl.openCurrentSession();
        EdocDocument result = documentDaoImpl.findById(documentId);
        documentDaoImpl.closeCurrentSession();
        return result;
    }

    public List<EdocDocument> findAll() {
        documentDaoImpl.openCurrentSession();
        List<EdocDocument> result = documentDaoImpl.findAll();
        documentDaoImpl.closeCurrentSession();
        return result;
    }

    public int countDocumentsFilter(PaginationCriteria paginationCriteria, String organId, String mode) {
        Session session = documentDaoImpl.openCurrentSession();
        int result = 0;
        try {
            session.beginTransaction();
            String queryDocument = "";
            switch (mode) {
                case EdXmlConstant.INBOX_MODE:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_INBOX_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.OUTBOX:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_OUTBOX_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.INBOX_NOT_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_INBOX_NOT_RECEIVER_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.INBOX_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_INBOX_RECEIVER_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.DRAFT:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_DRAFT_TMP, paginationCriteria);
                    break;
            }
            Query<Long> query = session.createQuery(queryDocument);
            query.setParameter("organDomain", organId);
            Long count = query.uniqueResult();
            result = Math.toIntExact(count);
        } catch (Exception e) {
            LOGGER.error("Error count documents filter " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    public List<DocumentCacheEntry> getDocumentsFilter(PaginationCriteria paginationCriteria, String organId, String mode) {
        List<DocumentCacheEntry> entries = new ArrayList<>();
        Session session = documentDaoImpl.openCurrentSession();
        try {
            String queryDocument = "";
            switch (mode) {
                case EdXmlConstant.INBOX_MODE:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_INBOX_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.OUTBOX:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_OUTBOX_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.INBOX_NOT_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_INBOX_NOT_RECEIVER_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.INBOX_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_INBOX_RECEIVER_TMP, paginationCriteria);
                    break;
                case EdXmlConstant.DRAFT:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_DRAFT_TMP, paginationCriteria);
                    break;
            }
            Query<EdocDocument> query = session.createQuery(queryDocument);
            query.setParameter("organDomain", organId);
            int pageNumber = paginationCriteria.getPageNumber();
            int pageSize = paginationCriteria.getPageSize();
            query.setFirstResult(pageNumber);
            query.setMaxResults(pageSize);
            List<EdocDocument> documents = query.getResultList();
            if (documents.size() > 0) {
                for (EdocDocument document : documents) {
                    DocumentCacheEntry documentCacheEntry = MapperUtil.modelToDocumentCached(document);
                    entries.add(documentCacheEntry);
                }
            }
            documentDaoImpl.closeCurrentSession();
        } catch (Exception e) {
            LOGGER.error("Error get documents filter " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entries;
    }

    public EdocDocument addDocument(MessageHeader messageHeader, TraceHeaderList traces, List<Attachment> attachments,
                                    StringBuilder outDocumentId, List<AttachmentCacheEntry> edocAttachmentCacheEntries, List<Error> errors) throws Exception {
        // output document id
        if (outDocumentId == null) {
            outDocumentId = new StringBuilder();
        }
        // add eDoc document
        EdocDocument edocDocument = addEdocDocument(messageHeader);
        if (edocDocument == null) {
            errors.add(new Error("M.SaveDocError",
                    "Error save document to database with document code " +
                            messageHeader.getCode().getCodeNumber() + "/" + messageHeader.getCode().getCodeNotation()));
            return null;
        }
        long docId = edocDocument.getDocumentId();
        outDocumentId.append(docId);

        // Add document to cache (using by get document)
        saveGetDocumentCache(docId, edocDocument.getFromOrganDomain(),
                edocDocument.getSentDate());

        // Insert document detail
        EdocDocumentDetail documentDetail = documentDetailService.addDocumentDetail(messageHeader, edocDocument);
        if (documentDetail == null) {
            errors.add(new Error("M.SaveDocError", "Error save document detail for document id " + docId));
            deleteDocument(docId);
            return null;
        }
        edocDocument.setDocumentDetail(documentDetail);
        // get business info
        String businessInfo = CommonUtil.getBusinessInfo(traces);

        // Insert Trace Header List
        EdocTraceHeaderList traceHeaderList = traceHeaderListService.addTraceHeaderList(traces, businessInfo, edocDocument);
        if (traceHeaderList == null) {
            errors.add(new Error("M.SaveDocError", "Error save trace header list for document id " + docId));
            deleteDocument(docId);
            return null;
        }
        edocDocument.setTraceHeaderList(traceHeaderList);
        // Insert vao bang Attachment
        Set<EdocAttachment> edocAttachments = attachmentService.addAttachments(edocDocument, attachments, edocAttachmentCacheEntries);
        int attachmentSizeInput = attachments.size();
        int attachmentSizeSave = edocAttachments.size();
        if (attachmentSizeInput != attachmentSizeSave) {
            errors.add(new Error("M.SaveDocError", "Error save attachments for document with id" + docId));
            deleteDocument(docId);
            return null;
        }
        edocDocument.setAttachments(edocAttachments);

        List<Organization> toesVPCP = checker.checkSendToVPCP(messageHeader.getToes());
        boolean sendVPCP = toesVPCP.size() > 0;
        List<Organization> toOrganizations = messageHeader.getToes();
        Date dueDate = messageHeader.getDueDate();
        List<Organization> organToPending;
        if (sendVPCP) {
            toOrganizations.removeAll(toesVPCP);
        }
        organToPending = toOrganizations;
        // add notifications
        Set<EdocNotification> notifications = notificationService.addNotifications(organToPending, dueDate, edocDocument);
        if (notifications.size() == 0) {
            deleteDocument(docId);
            LOGGER.error("Error when set document notification to database with document " + docId);
            errors.add(new Error("M.SaveDocError", "Error save notifications for document with id" + docId));
            return null;
        }
        // save pending document to cache
        savePendingDocumentCache(organToPending, docId);
        edocDocument.setNotifications(notifications);
        saveAllowGetDocumentCache(organToPending, docId);
        return edocDocument;
    }

    /**
     * Add eDoc document, save to database
     *
     * @param messageHeader
     * @return
     * @throws Exception
     */
    public EdocDocument addEdocDocument(MessageHeader messageHeader) throws Exception {
        Session currentSession = documentDaoImpl.openCurrentSession();
        try {
            currentSession.beginTransaction();
            // get info of eDoc document
            EdocDocument document = MapperUtil.modelToEdocDocument(messageHeader);
            documentDaoImpl.persist(document);
            currentSession.getTransaction().commit();
            return document;
        } catch (Exception e) {
            LOGGER.error("Error when save document for organ domain " + messageHeader.getFrom().getOrganId());
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
            return null;
        } finally {
            documentDaoImpl.closeCurrentSession();
        }
    }

    /**
     * check document is exist
     *
     * @param subject
     * @param codeNumber
     * @param codeNotation
     * @param promulgationDateStr
     * @param fromOrganDomain
     * @param tos
     * @param attachmentNames
     * @return
     */
    public boolean checkExistDocument(String subject, String codeNumber, String codeNotation,
                                      String promulgationDateStr, String fromOrganDomain, List<Organization> tos, List<String> attachmentNames) {
        documentDaoImpl.openCurrentSession();

        Date promulgationDate = XmlGregorianCalendarUtil.convertToDate(promulgationDateStr, "dd/MM/yyyy");
        String toOrganDomain = CommonUtil.getToOrganDomain(tos);

        boolean check = documentDaoImpl.checkExistDocument(subject, codeNumber, codeNotation, promulgationDate, fromOrganDomain, toOrganDomain, attachmentNames);

        documentDaoImpl.closeCurrentSession();
        return check;
    }

    public List<EdocDocument> selectForDailyCounter(Date date) {
        documentDaoImpl.openCurrentSession();
        List<EdocDocument> documents = documentDaoImpl.selectForDailyCounter(date);
        documentDaoImpl.closeCurrentSession();
        return documents;
    }

    public boolean checkExistDocument(String edXmlDocumentId) {
        EdocDocument check = documentDaoImpl.checkExistDocument(edXmlDocumentId);
        return check != null;
    }

    public boolean checkNewDocument(TraceHeaderList traceHeaderList) {
        // get business doc type
        long businessDocType = traceHeaderList.getBusiness().getBusinessDocType();
        // with new document, business doc type = 0
        return businessDocType == 0;
    }

    public void updateDocument(EdocDocument edocDocument) {
        Session session = documentDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            documentDaoImpl.saveOrUpdate(edocDocument);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error when update document with id " + edocDocument.getDocumentId() + " cause " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            documentDaoImpl.closeCurrentSession();
        }
    }

    /**
     * save document to cache for get document
     *
     * @param documentId
     * @param fromOrganDomain
     * @param sentDate
     */
    public void saveGetDocumentCache(long documentId, String fromOrganDomain, Date sentDate) {
        Map<String, Object> cacheThis = new HashMap<>();
        cacheThis.put("sentDate", sentDate);
        cacheThis.put("fromDomain", fromOrganDomain);

        RedisUtil.getInstance().set(RedisKey.getKey(String.valueOf(documentId),
                RedisKey.GET_DOCUMENT_KEY), cacheThis);
    }

    /**
     * save pending document for to domain -> cache
     *
     * @param tos
     * @param docId
     */
    public void savePendingDocumentCache(List<Organization> tos, long docId) {
        for (Organization to : tos) {
            // TODO: Cache
            List obj = RedisUtil.getInstance().get(RedisKey.getKey(to.getOrganId(), RedisKey.GET_PENDING_KEY), List.class);
            // if data in cache not exist, create new
            if (obj == null) {

                List<Long> documentIds = new ArrayList<>();
                documentIds.add(docId);
                RedisUtil.getInstance().set(RedisKey.getKey(to.getOrganId(), RedisKey.GET_PENDING_KEY), documentIds);
                LOGGER.info("List document pending null create new and save to cached with size " + documentIds.size() + " save to domain " + to.getOrganId());
            } else {
                // add document id to old list in cache
                List<Long> oldDocumentIds = null;
                oldDocumentIds = (List<Long>) obj;
                LOGGER.info("List document pending in cache  for organ domain " + to.getOrganId() + " with size " + oldDocumentIds.size());
                oldDocumentIds.add(docId);
                RedisUtil.getInstance().set(RedisKey.getKey(to.getOrganId(), RedisKey.GET_PENDING_KEY), oldDocumentIds);
                LOGGER.info("Save document pending in cache  for organ domain " + to.getOrganId() + " with size " + oldDocumentIds.size());

            }
        }
    }

    private void saveAllowGetDocumentCache(List<Organization> toOrgans, long documentId) {
        for (Organization to : toOrgans) {
            String organId = to.getOrganId();
            Boolean allowObj = RedisUtil.getInstance().get(RedisKey.getKey(organId
                    + documentId, RedisKey.CHECK_ALLOW_KEY), Boolean.class);
            if (allowObj == null) {
                // add to cache
                RedisUtil.getInstance().set(RedisKey.getKey(organId
                        + documentId, RedisKey.CHECK_ALLOW_KEY), true);
            }
        }
    }

    /**
     * save pending document for to domain -> cache
     *
     * @param tos
     * @param docId
     */
    public void savePendingDocumentCacheFromWeb(List<String> tos, long docId) {
        for (String to : tos) {
            // TODO: Cache
            savePendingDocumentCached(to, docId);
        }
    }

    public void savePendingDocumentCached(String toOrgan, long docId) {
        List obj = RedisUtil.getInstance().get(RedisKey.getKey(toOrgan, RedisKey.GET_PENDING_KEY), List.class);
        // if data in cache not exist, create new
        if (obj == null) {
            List<Long> documentIds = new ArrayList<>();
            documentIds.add(docId);
            RedisUtil.getInstance().set(RedisKey.getKey(toOrgan, RedisKey.GET_PENDING_KEY), documentIds);
        } else {
            // add document id to old list in cache
            List<Long> oldDocumentIds = null;
            oldDocumentIds = (List<Long>) obj;

            oldDocumentIds.add(docId);
            RedisUtil.getInstance().set(RedisKey.getKey(toOrgan, RedisKey.GET_PENDING_KEY), oldDocumentIds);
        }
    }

    public List<DocumentCacheEntry> getDocuments(String organDomain, int start, int size) {
        documentDaoImpl.openCurrentSession();
        List<DocumentCacheEntry> documentCacheEntries = new ArrayList<>();
        List<EdocDocument> documents = documentDaoImpl.getDocuments(organDomain, start, size);
        if (documents.size() > 0) {
            documentCacheEntries = ExecutorServiceUtil.getDocumentCacheEntries(documents);
        }
        documentDaoImpl.closeCurrentSession();
        return documentCacheEntries;
    }

    public List<DocumentCacheEntry> findByOrganIdAndMode(String organDomain, String mode, int start, int size) {
        List<DocumentCacheEntry> documentCacheEntries;
        String prefix = organDomain + "_" + mode;
        String cacheKey = RedisKey.getKey(prefix, RedisKey.GET_LIST_DOCUMENT_KEY);
        RedisUtil.getInstance().delete(cacheKey);
        documentCacheEntries = (List<DocumentCacheEntry>) RedisUtil.getInstance().get(cacheKey, Object.class);
        if (documentCacheEntries == null || documentCacheEntries.size() == 0) {
            documentCacheEntries = new ArrayList<>();
            documentDaoImpl.openCurrentSession();
            List<EdocDocument> documents = documentDaoImpl.findByOranDomain(organDomain, mode, start, size);
            if (documents.size() > 0) {
                /*for (EdocDocument document : documents) {
                    DocumentCacheEntry documentCacheEntry = MapperUtil.modelToDocumentCached(document);
                    documentCacheEntries.add(documentCacheEntry);
                }*/
                documentCacheEntries = ExecutorServiceUtil.getDocumentCacheEntries(documents);
                RedisUtil.getInstance().set(cacheKey, documentCacheEntries);
            }
            documentDaoImpl.closeCurrentSession();
        }
        return documentCacheEntries;
    }

    public DocumentCacheEntry getDocById(long documentId) {
        DocumentCacheEntry documentCacheEntry;
        String cacheKey = MemcachedKey.getKey(String.valueOf(documentId), MemcachedKey.DOCUMENT_KEY);
        MemcachedUtil.getInstance().delete(cacheKey);
        documentCacheEntry = (DocumentCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
        if (documentCacheEntry == null) {
            LOGGER.info("Found document with id " + documentId + " in cache !!!!!!");
            documentDaoImpl.openCurrentSession();
            EdocDocument edocDocument = documentDaoImpl.findById(documentId);
            if (edocDocument != null) {
                documentCacheEntry = MapperUtil.modelToDocumentCached(edocDocument);
                MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.SEND_DOCUMENT_TIME_LIFE, documentCacheEntry);
            }
            documentDaoImpl.closeCurrentSession();
        }
        return documentCacheEntry;
    }

    public MessageHeader getDocumentById(long docId) {

        EdocDocument document = this.getDocument(docId);

        EdocDocumentDetail detail = document.getDocumentDetail();

        return mapper.modelToMessageHeader(document, detail);
    }

    //KienNDc-InsertDocument
    public EdocDocument addDocument(EdocDocument edocDocument) {
        Session currSession = documentDaoImpl.openCurrentSession();
        try {
            currSession.beginTransaction();
            documentDaoImpl.persist(edocDocument);
            currSession.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (currSession != null) {
                currSession.getTransaction().rollback();
            }
            return null;
        } finally {
            documentDaoImpl.closeCurrentSession();
        }
        return edocDocument;
    }

    public void updateDocument(long documentId) {
        Session session = documentDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            EdocDocument document = documentDaoImpl.findById(documentId);
            document.setVisited(true);
            documentDaoImpl.update(document);
            session.getTransaction().commit();
            // update to memcached
            String cacheKey = MemcachedKey.getKey(String.valueOf(document.getDocumentId()), MemcachedKey.DOCUMENT_KEY);
            DocumentCacheEntry documentCacheEntry = (DocumentCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
            if (documentCacheEntry != null) {
                documentCacheEntry.setVisited(document.getVisited());
                MemcachedUtil.getInstance().update(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, documentCacheEntry);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            documentDaoImpl.closeCurrentSession();
        }
    }

    public void updateDraftToPublishDocument(long documentId) {
        Session session = documentDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            EdocDocument document = documentDaoImpl.findById(documentId);
            document.setDraft(false);
            documentDaoImpl.update(document);
            session.getTransaction().commit();
            // update to memcached
            String cacheKey = MemcachedKey.getKey(String.valueOf(document.getDocumentId()), MemcachedKey.DOCUMENT_KEY);
            DocumentCacheEntry documentCacheEntry = (DocumentCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
            if (documentCacheEntry != null) {
                documentCacheEntry.setVisited(document.getVisited());
                MemcachedUtil.getInstance().update(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, documentCacheEntry);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            documentDaoImpl.closeCurrentSession();
        }
    }

    public void deleteDraftDocument(long documentDraftId) {
        Session session = documentDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            EdocDocument document = documentDaoImpl.findById(documentDraftId);
            session.delete(document);
            session.getTransaction().commit();
            // update document in memcached
            String cacheKey = MemcachedKey.getKey(String.valueOf(document.getDocumentId()), MemcachedKey.DOCUMENT_KEY);
            DocumentCacheEntry documentCacheEntry = (DocumentCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
            if (documentCacheEntry != null) {
                MemcachedUtil.getInstance().delete(cacheKey);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            documentDaoImpl.closeCurrentSession();
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EdocDocumentService.class);
}
