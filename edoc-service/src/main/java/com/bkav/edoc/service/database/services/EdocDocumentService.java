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
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.InputStream;
import java.util.*;

public class EdocDocumentService {
    private final EdocDocumentDaoImpl documentDaoImpl = new EdocDocumentDaoImpl();
    private final Mapper mapper = new Mapper();
    private final Checker checker = new Checker();

    private final AttachmentGlobalUtil attUtil = new AttachmentGlobalUtil();

    public EdocDocumentService() {

    }

    public boolean deleteDocument(long documentId) {
        return documentDaoImpl.removeDocument(documentId);
    }

    public EdocDocument getDocument(long documentId) {
        return documentDaoImpl.getDocumentById(documentId);
    }

    public List<EdocDocument> findAll() {
        return documentDaoImpl.findAll();
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
            Query<Long> query = session.createQuery(queryDocument, Long.class);
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

    public List<EdocDocument> getDocumentList() {
        Session session = documentDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            Query<EdocDocument> query = session.createNativeQuery("{ call GetDocuments()}", EdocDocument.class);
            List<EdocDocument> documents = query.getResultList();
            System.out.println(documents.size());
            session.getTransaction().commit();
            return documents;
        } catch (Exception e) {
            session.getTransaction().rollback();
            return new ArrayList<>();
        } finally {
            documentDaoImpl.closeCurrentSession(session);
        }
    }

    public static void main(String[] args) {
        List<EdocDocument> documents = new EdocDocumentService().getDocumentList();
        for (EdocDocument document : documents) {
            System.out.println(document.getDocumentDetail().getSignerFullName());
        }
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
            Query<EdocDocument> query = session.createQuery(queryDocument, EdocDocument.class);
            query.setParameter("organDomain", organId);
            int pageNumber = paginationCriteria.getPageNumber();
            int pageSize = paginationCriteria.getPageSize();
            query.setFirstResult(pageNumber);
            query.setMaxResults(pageSize);
            List<EdocDocument> documents = query.getResultList();
            if (documents.size() > 0) {
                for (EdocDocument document : documents) {
                    DocumentCacheEntry documentCacheEntry = MapperUtil.documentToCached(document);
                    if (documentCacheEntry != null) {
                        entries.add(documentCacheEntry);
                    }
                }
            }
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
                                    StringBuilder outDocumentId, List<AttachmentCacheEntry> edocAttachmentCacheEntries, List<Error> errors) {
        Session currentSession = documentDaoImpl.openCurrentSession();
        try {
            // output document id
            if (outDocumentId == null) {
                outDocumentId = new StringBuilder();
            }

            currentSession.beginTransaction();
            // prepare get document info from message header
            EdocDocument document = MapperUtil.modelToEdocDocument(messageHeader);
            currentSession.persist(document);

            long docId = document.getDocumentId();

            LOGGER.info("Save document successfully return DocumentId " + docId);
            outDocumentId.append(docId);

            // Add document to cache (using by get document)
            saveGetDocumentCache(docId, document.getFromOrganDomain(),
                    document.getSentDate());

            // get info of document detail
            EdocDocumentDetail documentDetail = MapperUtil.modelToDocumentDetail(messageHeader);
            documentDetail.setDocument(document);
            currentSession.persist(documentDetail);
            LOGGER.info("Save document detail successfully with document id " + docId);
            document.setDocumentDetail(documentDetail);

            // get business info
            String businessInfo = CommonUtil.getBusinessInfo(traces);

            EdocTraceHeaderList edocTraceHeaderList = new EdocTraceHeaderList();
            if (traces.getTraceHeaders().size() > 0) {
                edocTraceHeaderList.setBusinessDocReason(traces.getBusiness().getBusinessDocReason());
                int businessDocType = (int) traces.getBusiness().getBusinessDocType();
                EdocTraceHeaderList.BusinessDocType type = EdocTraceHeaderList.BusinessDocType.values()[businessDocType];
                edocTraceHeaderList.setBusinessDocType(type);
                edocTraceHeaderList.setPaper(traces.getBusiness().getPaper());
                edocTraceHeaderList.setBusinessInfo(businessInfo);
                // get staff info
                if (traces.getBusiness().getStaffInfo() != null) {
                    StaffInfo staffInfo = traces.getBusiness().getStaffInfo();
                    edocTraceHeaderList.setEmail(staffInfo.getEmail());
                    edocTraceHeaderList.setDepartment(staffInfo.getDepartment());
                    edocTraceHeaderList.setMobile(staffInfo.getMobile());
                    edocTraceHeaderList.setStaff(staffInfo.getStaff());
                }

                // save trace header list to database
                edocTraceHeaderList.setDocument(document);
                currentSession.persist(edocTraceHeaderList);
                LOGGER.info("Save trace header list successfully with document id " + docId);

                // get list trace header
                Set<EdocTraceHeader> edocTraceHeaders = new HashSet<>();
                for (TraceHeader trace : traces.getTraceHeaders()) {
                    EdocTraceHeader traceHeader = new EdocTraceHeader();
                    traceHeader.setOrganDomain(trace.getOrganId());
                    traceHeader.setTimeStamp(trace.getTimestamp());
                    traceHeader.setTraceHeaderList(edocTraceHeaderList);
                    currentSession.persist(traceHeader);
                    LOGGER.info("Save trace header successfully with document id " + docId);
                    edocTraceHeaders.add(traceHeader);
                }
                edocTraceHeaderList.setTraceHeaders(edocTraceHeaders);
            }

            // Insert vao bang Attachment
            String rootPath = attUtil.getAttachmentPath();
            Calendar cal = Calendar.getInstance();
            String SEPARATOR = EdXmlConstant.SEPARATOR;
            Set<EdocAttachment> edocAttachmentSet = new HashSet<>();
            String domain = document.getFromOrganDomain();
            for (int i = 0; i < attachments.size(); i++) {
                Attachment attachment = attachments.get(i);
                String dataPath = domain + SEPARATOR +
                        cal.get(Calendar.YEAR) + SEPARATOR +
                        (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                        cal.get(Calendar.DAY_OF_MONTH) + SEPARATOR +
                        docId + "_" + (i + 1);

                String specPath = rootPath +
                        (rootPath.endsWith(SEPARATOR) ? "" : SEPARATOR) +
                        dataPath;
                long size;
                InputStream is = attachment.getInputStream();
                size = attUtil.saveToFile(specPath, is);
                is.close();
                if (size > 0) {
                    String name = attachment.getName();
                    String type = attachment.getContentType();
                    String organDomain = document.getFromOrganDomain();
                    String toOrganDomain = document.getToOrganDomain();
                    EdocAttachment edocAttachment = new EdocAttachment();
                    edocAttachment.setOrganDomain(organDomain);
                    edocAttachment.setName(name);
                    edocAttachment.setType(type);
                    edocAttachment.setToOrganDomain(toOrganDomain);
                    edocAttachment.setCreateDate(new Date());
                    edocAttachment.setFullPath(dataPath);
                    edocAttachment.setSize(String.valueOf(size));
                    edocAttachment.setDocument(document);
                    currentSession.persist(edocAttachment);
                    LOGGER.info("Save attachment successfully with document id " + docId);
                    AttachmentCacheEntry attachmentCacheEntry = MapperUtil.modelToAttachmentCache(edocAttachment);
                    attachmentCacheEntry.setDocumentId(docId);
                    edocAttachmentCacheEntries.add(attachmentCacheEntry);
                    edocAttachmentSet.add(edocAttachment);
                } else {
                    LOGGER.info("Save attachment fail with document id " + docId + " document code " + document.getDocCode());
                    currentSession.getTransaction().rollback();
                    return null;
                }
            }
            document.setAttachments(edocAttachmentSet);

            List<Organization> toesVPCP = checker.checkSendToVPCP(messageHeader.getToes());
            List<Organization> toOrganizations = messageHeader.getToes();
            List<Organization> organToPending = new ArrayList<>();
            if (toesVPCP.size() > 0) {
                for (Organization organization : toOrganizations) {
                    if (!toesVPCP.contains(organization)) {
                        organToPending.add(organization);
                    }
                }
            } else {
                organToPending = toOrganizations;
            }
            Date dueDate = messageHeader.getDueDate();

            Set<EdocNotification> notifications = new HashSet<>();
            // Insert Notification
            for (Organization to : organToPending) {
                EdocNotification notification = new EdocNotification();
                Date currentDate = new Date();
                notification.setDateCreate(currentDate);
                notification.setModifiedDate(currentDate);
                notification.setSendNumber(0);
                notification.setDueDate(dueDate);
                notification.setReceiverId(to.getOrganId());
                notification.setDocument(document);
                notification.setTaken(false);
                currentSession.persist(notification);
                LOGGER.info("Save edoc notification successfully for document " + docId
                        + " and code " + document.getDocCode());
                notifications.add(notification);
            }
            // save pending document to cache
            savePendingDocumentCache(organToPending, docId);
            document.setNotifications(notifications);
            saveAllowGetDocumentCache(organToPending, docId);
            currentSession.getTransaction().commit();
            return document;
        } catch (Exception e) {
            LOGGER.error("Error add document to database cause " + Arrays.toString(new String[]{Arrays.toString(e.getStackTrace())}));
            Error error = new Error("M.SaveDocError", "Save document error cause "
                    + Arrays.toString(e.getStackTrace()) + " with document code " + messageHeader.getCode().getCodeNumber());
            errors.add(error);
            if (currentSession != null) {
                currentSession.getTransaction().rollback();
            }
            return null;
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
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

        Date promulgationDate = XmlGregorianCalendarUtil.convertToDate(promulgationDateStr, "dd/MM/yyyy");
        String toOrganDomain = CommonUtil.getToOrganDomain(tos);

        return documentDaoImpl.checkExistDocument(subject, codeNumber, codeNotation, promulgationDate, fromOrganDomain, toOrganDomain, attachmentNames);
    }

    public List<EdocDocument> selectForDailyCounter(Date date) {

        return documentDaoImpl.selectForDailyCounter(date);
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
        documentDaoImpl.saveOrUpdate(edocDocument);
    }

    /**
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
        List<DocumentCacheEntry> documentCacheEntries = new ArrayList<>();
        List<EdocDocument> documents = documentDaoImpl.getDocuments(organDomain, start, size);
        if (documents.size() > 0) {
            documentCacheEntries = ExecutorServiceUtil.getDocumentCacheEntries(documents);
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
            EdocDocument edocDocument = documentDaoImpl.findById(documentId);
            if (edocDocument != null) {
                documentCacheEntry = MapperUtil.modelToDocumentCached(edocDocument);
                MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.SEND_DOCUMENT_TIME_LIFE, documentCacheEntry);
            }
        }
        return documentCacheEntry;
    }

    public DocumentCacheEntry getDocByCodeAndDomain(String docCode, String organDomain) {
        EdocDocument edocDocument = documentDaoImpl.getDocumentByCodeDomain(docCode, organDomain);
        if (edocDocument != null) {
            return MapperUtil.modelToDocumentCached(edocDocument);
        }
        return null;
    }

    public MessageHeader getDocumentById(long docId) {

        EdocDocument document = this.getDocument(docId);

        if (document != null) {
            EdocDocumentDetail detail = document.getDocumentDetail();
            return mapper.modelToMessageHeader(document, detail);
        }
        return null;
    }

    public EdocDocument addDocument(EdocDocument edocDocument) {
        Session currSession = documentDaoImpl.openCurrentSession();
        try {
            currSession.beginTransaction();
            currSession.persist(edocDocument);
            currSession.getTransaction().commit();
            return edocDocument;
        } catch (Exception e) {
            LOGGER.error("Error save document to database cause " + Arrays.toString(e.getStackTrace()));
            if (currSession != null) {
                currSession.getTransaction().rollback();
            }
            return null;
        } finally {
            documentDaoImpl.closeCurrentSession(currSession);
        }
    }

    public void updateDocument(long documentId) {
        EdocDocument document = documentDaoImpl.findById(documentId);
        if (document != null) {
            document.setVisited(true);
            documentDaoImpl.update(document);
            // update to memcached
            String cacheKey = MemcachedKey.getKey(String.valueOf(document.getDocumentId()), MemcachedKey.DOCUMENT_KEY);
            DocumentCacheEntry documentCacheEntry = (DocumentCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
            if (documentCacheEntry != null) {
                documentCacheEntry.setVisited(document.getVisited());
                MemcachedUtil.getInstance().update(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, documentCacheEntry);
            }
        } else {
            LOGGER.warn("Update Document Not found document with id " + documentId);
        }
    }

    public void updateDraftToPublishDocument(long documentId) {
        Session session = documentDaoImpl.openCurrentSession();
        try {
            session.beginTransaction();
            EdocDocument document = documentDaoImpl.findById(documentId);
            document.setDraft(false);
            session.update(document);
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
            documentDaoImpl.closeCurrentSession(session);
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
            documentDaoImpl.closeCurrentSession(session);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EdocDocumentService.class);
}
