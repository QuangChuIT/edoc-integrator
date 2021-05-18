package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.commonutil.XmlGregorianCalendarUtil;
import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDocumentDaoImpl;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.util.AppUtil;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.executor.ExecutorServiceUtil;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.mineutil.Mapper;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.resource.QueryString;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    public int countDocumentsFilter(PaginationCriteria paginationCriteria, String organId, String mode, String toOrgan, String fromOrgan, String docCode) {
        Session session = documentDaoImpl.openCurrentSession();
        String condition = "and";
        if (organId != null)
            if (organId.equals(PropsUtil.get("edoc.domain.vpubnd.0"))) condition = "or";
        int result = 0;
        try {
            session.beginTransaction();
            String queryDocument = "";
            switch (mode) {
                case EdXmlConstant.INBOX_MODE:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_INBOX_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.OUTBOX:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_OUTBOX_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.INBOX_NOT_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_INBOX_NOT_RECEIVER_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.INBOX_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_INBOX_RECEIVER_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.DRAFT:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_DRAFT_TMP, paginationCriteria, condition);
                    break;
            }
            Query query = session.createNativeQuery(queryDocument);
            query.setParameter("organDomain", organId);
            if (organId != null) {
                if (organId.equals(PropsUtil.get("edoc.domain.vpubnd.0"))) {
                    query.setParameter("organDomain1", GetterUtil.getString(PropsUtil.get("edoc.domain.vpubnd.1")));
                } else {
                    query.setParameter("organDomain1", null);
                }
            } else {
                query.setParameter("organDomain1", null);
            }
            query.setParameter("toOrgan", toOrgan);
            query.setParameter("fromOrgan", fromOrgan);
            query.setParameter("docCode", docCode);
            BigInteger count = (BigInteger) query.getSingleResult();
            result = count.intValue();
        } catch (Exception e) {
            LOGGER.error("Error count documents filter " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    public List<DocumentCacheEntry> getDocumentsFilter(PaginationCriteria paginationCriteria, String organId,
                                                       String mode, String toOrgan, String fromOrgan, String docCode) {
        List<DocumentCacheEntry> entries = new ArrayList<>();
        Session session = documentDaoImpl.openCurrentSession();
        String condition = "and";
        if (organId != null)
            if (organId.equals(PropsUtil.get("edoc.domain.vpubnd.0"))) condition = "or";
        try {
            String queryDocument = "";
            switch (mode) {
                case EdXmlConstant.INBOX_MODE:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_INBOX_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.OUTBOX:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_OUTBOX_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.INBOX_NOT_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_INBOX_NOT_RECEIVER_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.INBOX_RECEIVED:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_INBOX_RECEIVER_TMP, paginationCriteria, condition);
                    break;
                case EdXmlConstant.DRAFT:
                    queryDocument = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_DRAFT_TMP, paginationCriteria, condition);
                    break;
            }
            Query<EdocDocument> query = session.createNativeQuery(queryDocument, EdocDocument.class);
            query.setParameter("organDomain", organId);
            if (organId != null) {
                if (organId.equals(PropsUtil.get("edoc.domain.vpubnd.0"))) {
                    query.setParameter("organDomain1", GetterUtil.getString(PropsUtil.get("edoc.domain.vpubnd.1")));
                } else {
                    query.setParameter("organDomain1", null);
                }
            } else {
                query.setParameter("organDomain1", null);
            }
            query.setParameter("toOrgan", toOrgan);
            query.setParameter("fromOrgan", fromOrgan);
            query.setParameter("docCode", docCode);
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


    public List<DocumentCacheEntry> getDocumentNotTaken(PaginationCriteria paginationCriteria) {
        List<DocumentCacheEntry> entries = new ArrayList<>();
        Session session = documentDaoImpl.openCurrentSession();

        try {
            String queryDocumentNotTaken = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_NOT_TAKEN_TMP, paginationCriteria, null);
            Query<EdocNotification> query = session.createNativeQuery(queryDocumentNotTaken, EdocNotification.class);
            int pageNumber = paginationCriteria.getPageNumber();
            int pageSize = paginationCriteria.getPageSize();
            query.setFirstResult(pageNumber);
            query.setMaxResults(pageSize);
            List<EdocNotification> notifications = query.getResultList();
            if (notifications.size() > 0) {
                for (EdocNotification notification : notifications) {
                    EdocDocument edocDocument = notification.getDocument();
                    edocDocument.setToOrganDomain(notification.getReceiverId());
                    DocumentCacheEntry documentCacheEntry = MapperUtil.documentToCached(edocDocument);
                    entries.add(documentCacheEntry);
                }
            }

            /*List<EdocNotification> edocNotifications = edocNotificationDao.getEdocNotificationNotTaken(paginationCriteria);
            for (EdocNotification edocNotification: edocNotifications) {
                //EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(edocNotification.getReceiverId());
                //if (contact.getReceiveNotify()) {
                    EdocDocument edocDocument = edocNotification.getDocument();
                    edocDocument.setToOrganDomain(edocNotification.getReceiverId());
                    DocumentCacheEntry documentCacheEntry = MapperUtil.documentToCached(edocDocument);
                    entries.add(documentCacheEntry);
                //}
            }*/
        } catch (Exception e) {
            LOGGER.error("Error get documents not taken " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null)
                session.close();
        }
        return entries;
    }

    public int countDocumentsNotTaken(PaginationCriteria paginationCriteria) {
        Session session = documentDaoImpl.openCurrentSession();
        int result = 0;
        try {
            session.beginTransaction();
            String queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_NOT_TAKEN_TMP, paginationCriteria, null);
            Query query = session.createNativeQuery(queryDocument);
            BigInteger count = (BigInteger) query.getSingleResult();
            result = count.intValue();
        } catch (Exception e) {
            LOGGER.error("Error count documents not taken " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    public List<EdocDocument> getAllDocumentNotSendVPCP() {
        return documentDaoImpl.getDocumentNotSentToVPCP();
    }

    public List<DocumentCacheEntry> getDocumentNotSendVPCP(PaginationCriteria paginationCriteria) {
        List<DocumentCacheEntry> entries = new ArrayList<>();
        Session session = documentDaoImpl.openCurrentSession();
        try {
            String queryDocumentNotTaken = AppUtil.buildPaginatedQuery(QueryString.BASE_QUERY_DOCUMENT_NOT_SEND_VPCP, paginationCriteria, null);
            Query<EdocDocument> query = session.createNativeQuery(queryDocumentNotTaken, EdocDocument.class);
            int pageNumber = paginationCriteria.getPageNumber();
            int pageSize = paginationCriteria.getPageSize();
            query.setFirstResult(pageNumber);
            query.setMaxResults(pageSize);
            List<EdocDocument> documents = query.getResultList();
            if (documents.size() > 0) {
                documents.forEach(document -> {
                    DocumentCacheEntry documentCacheEntry = MapperUtil.documentToCached(document);
                    if (documentCacheEntry != null) {
                        entries.add(documentCacheEntry);
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error("Error get documents not taken " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null)
                session.close();
        }
        return entries;
    }

    public int countDocumentNotendVPCP(PaginationCriteria paginationCriteria) {
        Session session = documentDaoImpl.openCurrentSession();
        int result = 0;
        try {
            session.beginTransaction();
            String queryDocument = AppUtil.buildPaginatedQuery(QueryString.QUERY_COUNT_DOCUMENT_NOT_SEND_VPCP, paginationCriteria, null);
            Query query = session.createNativeQuery(queryDocument);
            BigInteger count = (BigInteger) query.getSingleResult();
            result = count.intValue();
        } catch (Exception e) {
            LOGGER.error("Error count documents not taken " + Arrays.toString(e.getStackTrace()));
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
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
            String docCode = document.getDocCode();

            LOGGER.info("Save document successfully return DocumentId " + docId + " docCode " + docCode);
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
                // fix loi tu vpcp gui thieu the business
                if (traces.getBusiness() == null) {
                    edocTraceHeaderList.setBusinessDocReason("New document");
                    int businessDocType = 0;
                    EdocTraceHeaderList.BusinessDocType type = EdocTraceHeaderList.BusinessDocType.values()[businessDocType];
                    edocTraceHeaderList.setBusinessDocType(type);
                    edocTraceHeaderList.setPaper(0);
                } else {
                    edocTraceHeaderList.setBusinessDocReason(traces.getBusiness().getBusinessDocReason());
                    int businessDocType = (int) traces.getBusiness().getBusinessDocType();
                    EdocTraceHeaderList.BusinessDocType type = EdocTraceHeaderList.BusinessDocType.values()[businessDocType];
                    edocTraceHeaderList.setBusinessDocType(type);
                    edocTraceHeaderList.setPaper(traces.getBusiness().getPaper());
                }
                edocTraceHeaderList.setBusinessInfo(businessInfo);
                // get staff info
                if (traces.getBusiness() != null && traces.getBusiness().getStaffInfo() != null) {
                    StaffInfo staffInfo = traces.getBusiness().getStaffInfo();
                    edocTraceHeaderList.setEmail(staffInfo.getEmail());
                    edocTraceHeaderList.setDepartment(staffInfo.getDepartment());
                    edocTraceHeaderList.setMobile(staffInfo.getMobile());
                    edocTraceHeaderList.setStaff(staffInfo.getStaff());
                } else {
                    edocTraceHeaderList.setEmail("");
                    edocTraceHeaderList.setDepartment("");
                    edocTraceHeaderList.setMobile("");
                    edocTraceHeaderList.setStaff("");
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
            List<Organization> toes = messageHeader.getToes();
            LOGGER.info("-------------- List to organization ---------- "
                    + toes.toString() + "with document code " + docCode + " and form organ "
                    + messageHeader.getFrom().toString() + " document id " + document.getDocumentId());
            List<Organization> toesVPCP = checker.checkSendToVPCP(toes);
            LOGGER.info("------------ Bkav List Organ send to VPCP --------- " +
                    toesVPCP.toString() + "with document code " + docCode + " and form organ "
                    + messageHeader.getFrom().toString() + " document id " + document.getDocumentId());
            List<Organization> organToPending = new ArrayList<>();
            if (toesVPCP.size() > 0) {
                List<String> organDomains = toesVPCP.stream().map(Organization::getOrganId).collect(Collectors.toList());
                for (Organization organization : toes) {
                    if (!organDomains.contains(organization.getOrganId())) {
                        organToPending.add(organization);
                    }
                }
            } else {
                organToPending = toes;
            }
            LOGGER.info("------------ Bkav List Organ save to notification table --------- " +
                    organToPending.toString() + "with document code " + docCode + " and form organ " +
                    messageHeader.getFrom().toString() + " document id " + document.getDocumentId());
            Date dueDate = messageHeader.getDueDate();

            Set<EdocNotification> notifications = new HashSet<>();
            // Insert Notification
            // get check organization
            boolean isTayNinh = GetterUtil.getBoolean(PropsUtil.get("edoc.check.organ.is.tayninh"), false);
            // get turn on vnpt request with group organ domain
            boolean turnOn = GetterUtil.getBoolean(PropsUtil.get("edoc.turn.on.vnpt.request"), false);
            int countOrgan = 0;
            LOGGER.info("---------- Bkav Prepare insert notification with size " +
                    organToPending.size() + " to database --------" + " document id " + document.getDocumentId());
            for (Organization to : organToPending) {
                LOGGER.info("------------------ Insert into notification with document id " + docId + ", receiver id " + to.getOrganId());
                EdocNotification notification = new EdocNotification();
                Date currentDate = new Date();
                notification.setDateCreate(currentDate);
                notification.setModifiedDate(currentDate);
                notification.setSendNumber(0);
                notification.setDueDate(dueDate);
                if (isTayNinh) {
                    if (turnOn) {
                        // tay ninh
                        if (to.getOrganId().charAt(10) == 'A') {
                            notification.setReceiverId(PropsUtil.get("edoc.domain.A.parent"));
                        } else {
                            notification.setReceiverId(to.getOrganId());
                        }
                    } else {
                        notification.setReceiverId(to.getOrganId());
                    }
                } else {
                    // lamdong
                    if (turnOn) {
                        EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(to.getOrganId());
                        if (contact.getIntegratorCenter()) {
                            if (countOrgan == 0) {
                                notification.setReceiverId(PropsUtil.get("edoc.domain.A.parent"));
                                countOrgan++;
                            } else {
                                continue;
                            }
                        } else {
                            notification.setReceiverId(to.getOrganId());
                        }
                        /*String[] subDomain = to.getOrganId().split("\\.");
                        String childDomain = subDomain[2] + "." + subDomain[3];
                        List<String> listParentDomain = Arrays.asList(PropsUtil.get("edoc.integrator.center.lamdong").split("\\|"));
                        boolean hasParent = listParentDomain.stream().anyMatch(s -> s.contains(childDomain) || s.contains(to.getOrganId()));
                        if (hasParent && !to.getOrganId().equals(PropsUtil.get("edoc.exclude.center.lamdong"))) {
                            if (countOrgan == 0) {
                                notification.setReceiverId(PropsUtil.get("edoc.domain.A.parent"));
                                countOrgan++;
                            } else {
                                continue;
                            }
                        } else {
                            notification.setReceiverId(to.getOrganId());
                        }*/
                    } else {
                        notification.setReceiverId(to.getOrganId());
                    }
                }
                notification.setDocument(document);
                notification.setTaken(false);
                currentSession.persist(notification);
                LOGGER.info("Save edoc notification successfully for document " + docId
                        + " and code " + document.getDocCode());
                notifications.add(notification);
            }
            LOGGER.info("---------- Bkav List notification before save to database size "
                    + notifications.size() + "with document code " + docCode
                    + " and form organ " + messageHeader.getFrom().toString() + " document id " + document.getDocumentId());
            // save pending document to cache
            savePendingDocumentCache(organToPending, docId);
            document.setNotifications(notifications);
            saveAllowGetDocumentCache(organToPending, docId);
            currentSession.getTransaction().commit();
            return document;
        } catch (Exception e) {
            LOGGER.error("Error add document to database because " + e);
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
     * @param subject             - subject document
     * @param codeNumber          - code number of document
     * @param codeNotation        - code notation of document
     * @param promulgationDateStr - Promulgation Date String
     * @param fromOrganDomain     - From Organ Domain
     * @param tos                 - List To Organization
     * @param attachmentNames     - List Attachment Name
     * @return boolean
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
        return (check != null);
    }

    public boolean checkExistDocumentByDocCode(String fromOrgan, String toOrgan, String docCode) {
        return documentDaoImpl.checkExistDocumentByDocCode(fromOrgan, toOrgan, docCode);
    }

    public boolean checkNewDocument(TraceHeaderList traceHeaderList) {
        // get business doc type
        long businessDocType = traceHeaderList.getBusiness().getBusinessDocType();
        // with new document, business doc type = 0
        return businessDocType == 0;
    }

    public EdocDocument getDocumentByCode(String docCode) {
        return documentDaoImpl.getDocumentByDocCode(docCode);
    }

    public void updateDocument(EdocDocument edocDocument) {
        documentDaoImpl.saveOrUpdate(edocDocument);
    }

    /**
     * @param documentId      - document id
     * @param fromOrganDomain - From Organ Domain
     * @param sentDate        - Sent Date
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
     * @param tos   - List To Organ
     * @param docId Document Id
     */
    public void savePendingDocumentCache(List<Organization> tos, long docId) {
        for (Organization to : tos) {
            String organId = to.getOrganId();
            if (organId.charAt(10) == 'A') {
                organId = PropsUtil.get("edoc.domain.A.parent");
            }
            // TODO: Cache
            List obj = RedisUtil.getInstance().get(RedisKey.getKey(organId, RedisKey.GET_PENDING_KEY), List.class);
            // if data in cache not exist, create new
            if (obj == null) {
                List<Long> documentIds = new ArrayList<>();
                documentIds.add(docId);
                RedisUtil.getInstance().set(RedisKey.getKey(to.getOrganId(), RedisKey.GET_PENDING_KEY), documentIds);
                LOGGER.info("List document pending null create new and save to cached with size " + documentIds.size() + " save to domain " + to.getOrganId());
            } else {
                // add document id to old list in cache
                List<Long> oldDocumentIds;
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
            // check if send to group config
            if (organId.charAt(10) == 'A') {
                organId = PropsUtil.get("edoc.domain.A.parent");
            }
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
     * @param tos   List To Organ
     * @param docId DocumentId
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
            currSession.save(edocDocument);
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

    public int countReceivedExtDocument(String fromDate, String toDate, boolean received_ext, String organDomain) {
        return documentDaoImpl.countReceivedExtDoc(fromDate, toDate, received_ext, organDomain);
    }

    public List<Long> getDocCodeByOrganDomain(String fromDate, String toDate, String organDomain) {
        return documentDaoImpl.getDocCodeByOrganDomain(fromDate, toDate, organDomain);
    }

    public List<EdocDocument> getDocumentByDate(Date date) {
        return documentDaoImpl.getDocumentByDate(date);
    }

    public List<EdocDocument> getDocumentsByDocCode(String docCode) {
        return documentDaoImpl.getDocumentsByDocCode(docCode);
    }

    public void getDailyCounterDocument(Date fromDate, Date toDate) {
        List<Date> dateList = documentDaoImpl.getDateInRange(fromDate, toDate);
        dateList.forEach(date -> {
            _counterDate = date;
            Map<String, EdocDailyCounter> dailyCounterMap = new HashMap<>();
            LOGGER.info("Starting counter document in date: " + date);
            List<String> docCodes = documentDaoImpl.getDocCodeByCounterDate(date);
            docCodes.forEach(docCode -> {
                List<EdocDocument> documents = documentDaoImpl.getDocumentsByDocCode(docCode);
                AtomicReference<String> fromOrgan = new AtomicReference<>("");
                documents.forEach(document -> {
                    fromOrgan.set(document.getFromOrganDomain());

                    String toOrgans = document.getToOrganDomain();
                    List<String> toOrganList = Arrays.asList(toOrgans.split("#"));
                    toOrganList.stream().filter(this::checkAgencyOrgan)
                            .forEach(toOrgan -> countReceived(toOrgan, dailyCounterMap));
                });
                if (checkAgencyOrgan(fromOrgan.get()))
                    countSent(fromOrgan.get(), dailyCounterMap);
            });
            System.out.println(new Gson().toJson(dailyCounterMap));
            //submitDatabase(dailyCounterMap);
        });
    }

    private void submitDatabase(Map<String, EdocDailyCounter> dailyCounterMap) {
        for (Map.Entry<String, EdocDailyCounter> entry : dailyCounterMap.entrySet()) {
            EdocDailyCounter dailyCounter = entry.getValue();
            EdocDailyCounterServiceUtil.createDailyCounter(dailyCounter);
        }
    }

    private boolean checkAgencyOrgan(String organDomain) {
        boolean result = false;
        try {
            EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(organDomain);
            if (edocDynamicContact != null) {
                result = edocDynamicContact.getAgency();
            }
        } catch (Exception e) {
            LOGGER.error("Error check organ to stat cause " + e);
        }
        return result;
    }

    private void countSent(String organDomain, Map<String, EdocDailyCounter> dailyCounterMap) {
        EdocDailyCounter dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int sent = dailyCounter.getSent() + 1;
            dailyCounter.setSent(sent);
        } else {
            dailyCounter = new EdocDailyCounter();
            dailyCounter.setSent(1);
            dailyCounter.setDateTime(_counterDate);
            dailyCounter.setReceived(0);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    private void countReceived(String organDomain, Map<String, EdocDailyCounter> dailyCounterMap) {
        EdocDailyCounter dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int received = dailyCounter.getReceived() + 1;
            dailyCounter.setReceived(received);
        } else {
            dailyCounter = new EdocDailyCounter();
            dailyCounter.setSent(0);
            dailyCounter.setDateTime(_counterDate);
            dailyCounter.setReceived(1);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    public List<String> getDocCodeByCounterDate(Date _counterDate) {
        return documentDaoImpl.getDocCodeByCounterDate(_counterDate);
    }

    public boolean testCheck(String domain) {

        String[] subDomain = domain.split("\\.");
        String childDomain = subDomain[2] + "." + subDomain[3];
        List<String> listParentDomain = Arrays.asList(PropsUtil.get("edoc.integrator.center.lamdong").split("\\|"));
        listParentDomain.forEach(str -> {
            System.out.println(str);
        });
        return listParentDomain.stream().anyMatch(s -> s.contains(childDomain));
        /*for (String s: listParentDomain) {
            if (s.trim().contains(childDomain))
                return true;
        }*/
    }

    public static void main(String[] args) {
        /*String yesterday = "2021-01-26";
        java.sql.Date yes = java.sql.Date.valueOf(yesterday);
        String now = "2021-01-28";
        java.sql.Date no = java.sql.Date.valueOf(now);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        Date date = cal.getTime();

        EdocDocumentService edocDocumentService = new EdocDocumentService();
        //edocDocumentService.getDailyCounterDocument(yes, no);
        System.out.println(edocDocumentService.getDocCodeByCounterDate(date));*/

        EdocDocumentService edocDocumentService = new EdocDocumentService();
        String domain = "000.01.77.H36";
        if (edocDocumentService.testCheck(domain)) {
            System.out.println("True!!");
        } else {
            System.out.println("False");
        }
    }


    private Date _counterDate;
    private static final Logger LOGGER = Logger.getLogger(EdocDocumentService.class);
}
