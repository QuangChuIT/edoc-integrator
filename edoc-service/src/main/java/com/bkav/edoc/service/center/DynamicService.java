package com.bkav.edoc.service.center;

import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.commonutil.XmlChecker;
import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.database.services.*;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.mineutil.*;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.util.ResponseUtil;
import com.bkav.edoc.service.vpcp.ServiceVPCP;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.vpcp.services.model.SendEdocResult;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class DynamicService extends AbstractMediator implements ManagedLifecycle {

    private final EdocDocumentService documentService = new EdocDocumentService();
    private final EdocNotificationService notificationService = new EdocNotificationService();
    private final EdocAttachmentService attachmentService = new EdocAttachmentService();
    private final EdocTraceHeaderListService traceHeaderListService = new EdocTraceHeaderListService();
    private final EdocDynamicContactService edocDynamicContactService = new EdocDynamicContactService();
    private final EdocTraceService traceService = new EdocTraceService();
    private final ArchiveMime archiveMime = new ArchiveMime();
    private final Mapper mapper = new Mapper();

    public boolean mediate(MessageContext messageContext) {
        LOGGER.info("--------------- eDoc mediator invoker by class mediator ---------------");

        org.apache.axis2.context.MessageContext inMessageContext = ((Axis2MessageContext) messageContext).getAxis2MessageContext();

        SynapseLog synLog = getLog(messageContext);

        if (synLog.isTraceOrDebugEnabled()) {
            synLog.traceOrDebug("Start : Log mediator");

            if (synLog.isTraceTraceEnabled()) {
                synLog.traceTrace("Message : " + messageContext.getEnvelope());
            }
        }

        String soapAction = inMessageContext.getSoapAction();

        LOGGER.info("--------------- Soap action  --------------- " + soapAction);

        Map<String, Object> map = new HashMap<>();

        SOAPEnvelope responseEnvelope;

        try {
            Document document = xmlUtil.convertToDocument(messageContext.getEnvelope());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Request message ----------------------> " + messageContext.getEnvelope().toString());
            }

            switch (soapAction) {
                case EdXmlConstant.SEND_DOCUMENT_ACTION:
                    LOGGER.info("-------------------------- Send document ------------------------");
                    LOGGER.info(messageContext.getEnvelope().toString());
                    map = sendDocument(document, inMessageContext);
                    break;
                case EdXmlConstant.GET_PENDING_DOCUMENT_ACTION:
                    LOGGER.info("------------------------ Get Pending documents -------------------");
                    map = getPendingDocumentIds(document);
                    break;
                case EdXmlConstant.GET_DOCUMENT_ACTION:
                    LOGGER.info("------------------------ Get documents -------------------");
                    map = getDocument(document);
                    break;
                case EdXmlConstant.UPDATE_TRACE_ACTION:
                    map = updateTraces(document);
                    break;
                case EdXmlConstant.GET_TRACE_ACTION:
                    map = getTraces(document);
                    break;
                case EdXmlConstant.CONFIRM_RECEIVED_REQUEST:
                    map = confirmReceived(document);
                    break;
                case EdXmlConstant.CHECK_PERMISSION:
                    map = checkPermission(document);
                    break;
                case EdXmlConstant.GET_CONTACT:
                    map = getOrganizations(document);
                    break;
                default:
                    LOGGER.error("Soap action not define !!!!!!!");
            }
        } catch (Exception e) {
            LOGGER.error("Error when process eDoc mediator !!!!!!!!!!!!!!!!!!! " + Arrays.toString(e.getStackTrace()));
        }
        responseEnvelope = ResponseUtil.buildResultEnvelope(inMessageContext, map, soapAction);
        try {
            inMessageContext.setDoingSwA(true);
            inMessageContext.setDoingMTOM(true);
            inMessageContext.setEnvelope(responseEnvelope);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        return true;
    }

    private Map<String, Object> getOrganizations(Document doc) {
        Map<String, Object> map = new HashMap<>();

        Document bodyChildDocument;

        List<EdocDynamicContact> contacts;

        List<Error> errorList = new ArrayList<>();

        Report report;
        CheckPermission checkPermission;
        try {
            checkPermission = extractMime.getCheckPermissionFromRq(doc, EdXmlConstant.GET_CONTACT);
            report = checker.checkPermission(checkPermission);
            if (!report.isIsSuccess()) {
                bodyChildDocument = xmlUtil.convertEntityToDocument(Report.class, report);

                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                return map;
            }
            updateReceivedNotify(report, checkPermission);
            contacts = edocDynamicContactService.getAllDynamicContact();
            if (contacts == null) {
                contacts = new ArrayList<>();
            }
            GetOrganizationsResponse response = ResponseUtil.createGetOrganizationsResponse(contacts);

            bodyChildDocument = xmlUtil.convertEntityToDocument(GetOrganizationsResponse.class, response);

            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

        } catch (Exception e) {
            LOGGER.error("Error when get organizations " + e);
            errorList.add(new Error("M.GetOrganizations", "Error when process get organization " + e.getMessage()));

            report = new Report(false, new ErrorList(errorList));

            bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);

            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        }

        return map;
    }

    private Map<String, Object> checkPermission(Document doc) {

        Map<String, Object> map = new HashMap<>();

        Document bodyChildDocument;

        Report report;

        List<Error> errorList = new ArrayList<>();


        CheckPermission checkPermission;

        try {
            checkPermission = extractMime.getCheckPermission(doc);

            report = checker.checkPermission(checkPermission);

            bodyChildDocument = xmlUtil.convertEntityToDocument(Report.class, report);

            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        } catch (Exception e) {
            LOGGER.error("Error when check permission " + e);
            errorList.add(new Error("M.CheckPermission", "Error when process check permission " + e.getMessage()));

            report = new Report(false, new ErrorList(errorList));

            bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);

            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        }

        return map;
    }

    private Map<String, Object> getTraces(Document envelop) {

        Map<String, Object> map = new HashMap<>();

        Report report;

        Document responseDocument = null;

        List<Error> errorList = new ArrayList<>();

        List<EdocTrace> traces;

        Document bodyChildDocument;
        CheckPermission checkPermission;
        try {
            checkPermission = extractMime.getCheckPermissionFromRq(envelop, EdXmlConstant.GET_TRACE);
            report = checker.checkPermission(checkPermission);
            if (!report.isIsSuccess()) {
                bodyChildDocument = xmlUtil.convertEntityToDocument(
                        Report.class, report);

                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                return map;
            }
            updateReceivedNotify(report, checkPermission);
            String organId = extractMime.getOrganId(envelop, EdXmlConstant.GET_TRACE);
            Date fromDate = extractMime.getTimeStamp(envelop, EdXmlConstant.GET_TRACE);
            if (organId == null || organId.isEmpty()) {
                errorList.add(new Error("M.OrganId", "OrganId is required."));

                report = new Report(false, new ErrorList(errorList));

                bodyChildDocument = xmlUtil.convertEntityToDocument(
                        Report.class, report);

                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

                return map;
            }
            LOGGER.info("Get trace for " + organId + " from date " + fromDate);
            // get trace
            traces = traceService.getEdocTracesByOrganId(organId, fromDate);
            LOGGER.info("Number traces get " + traces.size());

            List<MessageStatus> statuses = mapper.traceInfoToStatusEntity(traces);
            GetTraceResponse response = new ResponseUtil().createGetTraceResponse(statuses);
            // disable traces after get traces
            if (fromDate == null) {
                traceService.disableEdocTrace(traces);
            }
            try {
                responseDocument = xmlUtil.convertEntityToDocument(GetTraceResponse.class, response);
            } catch (Exception ex) {
                LOGGER.error("Error build response for get traces !!!!! " + ex.getMessage());
            }

            map.put(StringPool.CHILD_BODY_KEY, responseDocument);

        } catch (Exception e) {
            LOGGER.error("Error get traces " + e);
            errorList.add(new Error("M.GetTraces", "Error when process get traces " + e.getMessage()));

            report = new Report(false, new ErrorList(errorList));

            bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);

            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        }
        return map;
    }

    private Map<String, Object> confirmReceived(Document envelop) {
        LOGGER.info("Confirm Received Invoker at " + SIMPLE_DATE_FORMAT.format(new Date()));
        Map<String, Object> map = new HashMap<>();

        Report report;

        List<Error> errorList = new ArrayList<>();

        Document bodyChildDocument;
        CheckPermission checkPermission;
        try {
            String organId = extractMime.getOrganId(envelop, EdXmlConstant.CONFIRM_RECEIVED_REQUEST);
            checkPermission = extractMime.getCheckPermissionFromRq(envelop, EdXmlConstant.CONFIRM_RECEIVED_REQUEST);
            report = checker.checkPermission(checkPermission);
            if (!report.isIsSuccess()) {
                bodyChildDocument = xmlUtil.convertEntityToDocument(
                        Report.class, report);
                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                return map;
            }
            updateReceivedNotify(report, checkPermission);
            if (organId == null || organId.isEmpty()) {
                errorList.add(new Error("M.OrganId", "OrganId is required."));
            }

            long documentId = xmlUtil.getDocumentId(envelop);
            if (documentId == 0) {
                errorList.add(new Error("M.DocumentId", "DocumentId is required."));
            }

            if (errorList.isEmpty()) {
                LOGGER.info("--------------------------------- Confirm Receiver Invoke by "
                        + organId + " : " + documentId + " ----------------------------------");
                // remove pending document
                notificationService.removePendingDocId(organId, documentId);
                LOGGER.info("-------------- Remove pending success for document with id " + documentId + ": " + organId);
                LOGGER.info("Building trace received and  to database for organ " + organId);
                // TODO auto add trace received(01) for this document
                EdocDocument document = documentService.getDocument(documentId);
                if (document != null) {
                    MessageStatus messageStatus = createConfirmTrace(document, organId, true);
                    traceService.updateTrace(messageStatus, errorList);
                } else {
                    errorList.add(new Error("M.ConfirmReceived", "Not found document with document id " + documentId));
                }

               /* // TODO if document of VPCP send request confirm done or fail
                EdocDocument document = documentService.getDocument(documentId);
                if (document.isReceivedExt()) {
                    String extDocumentId = document.getDocumentExtId();
                    if (extDocumentId != null) {
                        GetChangeStatusResult statusResult = ServiceVPCP.getInstance().confirmReceived(extDocumentId, "done");
                        LOGGER.info("Confirm Receiver to VPCP with docId " + document.getDocumentId() + " result code " + statusResult.getStatus());
                    } else {
                        LOGGER.error("Found document to confirm to VPCP but docId VPCP docId VPCP null !!!!!!!!!");
                    }
                }*/
            }
        } catch (Exception e) {
            LOGGER.error("Error confirm received " + e);
            errorList.add(new Error("M.ConfirmReceived", "Error process confirm received " + Arrays.toString(e.getStackTrace())));
        }

        if (!errorList.isEmpty()) {
            report = new Report(false, new ErrorList(errorList));
        } else {
            report = new Report(true, new ErrorList(errorList));
        }

        bodyChildDocument = xmlUtil.convertEntityToDocument(
                Report.class, report);
        map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        return map;
    }

    private Map<String, Object> updateTraces(Document envelop) {
        Map<String, Object> map = new HashMap<>();

        Report report;

        MessageStatus status;

        List<Error> errorList = new ArrayList<>();

        Document bodyChildDocument;

        try {
            // Extract MessageHeader
            status = extractMime.getStatus(envelop);

            SignatureEdoc signature = extractMime.getSignature(envelop);
            LOGGER.info(signature);
            report = checker.checkSignature(signature);

            if (!report.isIsSuccess()) {
                bodyChildDocument = xmlUtil.convertEntityToDocument(
                        Report.class, report);

                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                return map;
            }
            CheckPermission checkPermission = new CheckPermission();
            checkPermission.setToken(signature.getKeyInfo().getToken());
            checkPermission.setOrganId(signature.getKeyInfo().getOrganId());
            updateReceivedNotify(report, checkPermission);
            List<Error> errors = new ArrayList<>();
            // update trace
            LOGGER.info(status.toString());
            if (GetterUtil.getInteger(status.getStatusCode()) != 5) {
                boolean isExists = traceService.exists(status.getFrom().getOrganId(),
                        status.getResponseFor().getOrganId(),
                        status.getResponseFor().getCode(), GetterUtil.getInteger(status.getStatusCode(), 1));
                if (isExists) {
                    LOGGER.warn("Exist trace on esb with code " + status.getResponseFor().getCode() + " from_organ_domain "
                            + status.getFrom().getOrganId() + " to_organ_domain "
                            + status.getResponseFor().getOrganId() + " status code " + status.getResponseFor().getCode());
                    errorList.add(new Error("M.UpdateTraces", "Exist trace on esb with code "
                            + status.getResponseFor().getCode() + " from_organ_domain "
                            + status.getFrom().getOrganId() + " to_organ_domain "
                            + status.getResponseFor().getOrganId() + " status code " + status.getResponseFor().getCode()));

                    report = new Report(false, new ErrorList(errorList));

                    bodyChildDocument = xmlUtil.convertEntityToDocument(
                            Report.class, report);
                    map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                    return map;
                }
            }

            EdocTrace trace = traceService.updateTrace(status, errors);
            if (errors.size() > 0) {
                report = new Report(false, new ErrorList(errors));
            } else {
                ResponseFor responseFor = checker.checkSendToVPCP(status.getResponseFor());
                if (responseFor != null) {
                    LOGGER.info("Send status to vpcp from organ " +
                            status.getFrom().getOrganId() + "to organ " +
                            status.getResponseFor().getOrganId() + " with code " + status.getResponseFor().getCode());
                    String edxmlDocumentId = responseFor.getDocumentId();
                    boolean existDocument = documentService.checkExistDocument(edxmlDocumentId);
                    if (existDocument) {
                        String edocEdXMLFilePath = PropsUtil.get("VPCP.attachment.dir");
                        SendEdocResult sendEdocResult = ServiceVPCP.getInstance().sendStatus(status, edocEdXMLFilePath);
                        if (sendEdocResult != null) {
                            LOGGER.info("-------------------- Send status to VPCP status " + sendEdocResult.getStatus());
                            LOGGER.info("-------------------- Send status to VPCP Desc: " + sendEdocResult.getErrorDesc());
                            LOGGER.info("-------------------- Send status to VPCP DocID: " + sendEdocResult.getDocID());
                        } else {
                            errorList.add(new Error("M.UpdateTrace", "Error when send trace to VPCP"));
                        }
                    } else {
                        errorList.add(new Error("M.UpdateTrace", "Document not exist on ESB, not send trace to VPCP"));
                    }
                }
                if (errorList.size() > 0) {
                    report = new Report(false, new ErrorList(errorList));
                } else {
                    report = new Report(true, new ErrorList(errorList));
                }
            }
            bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);

            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        } catch (Exception e) {
            LOGGER.error("Error update traces cause " + Arrays.toString(e.getStackTrace()));
            errorList.add(new Error("M.UpdateTraces", "Error process get update " + Arrays.toString(e.getStackTrace())));

            report = new Report(false, new ErrorList(errorList));

            bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);
            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        }
        return map;
    }


    private Map<String, Object> getDocument(Document doc) {

        Map<String, Object> map = new HashMap<>();
        Report report;
        List<Error> errorList = new ArrayList<>();
        long documentId = 0;
        String organId;
        CheckPermission checkPermission;
        try {
            documentId = xmlUtil.getDocumentId(doc);
            organId = extractMime.getOrganId(doc, EdXmlConstant.GET_DOCUMENT);
            checkPermission = extractMime.getCheckPermissionFromRq(doc, EdXmlConstant.GET_DOCUMENT);
            report = checker.checkPermission(checkPermission);
            if (!report.isIsSuccess()) {
                Document bodyChildDocument = xmlUtil.convertEntityToDocument(
                        Report.class, report);

                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                return map;
            }
            updateReceivedNotify(report, checkPermission);
            // check document id and organ id
            if (documentId > 0L && organId != null && !organId.isEmpty()) {
                // Check permission with document
                boolean acceptToDocument;

                // TODO: Cache
                Boolean allowObj;
                allowObj = RedisUtil.getInstance().get(RedisKey.getKey(organId
                        + documentId, RedisKey.CHECK_ALLOW_KEY), Boolean.class);
                if (allowObj != null) {
                    acceptToDocument = allowObj;
                } else {
                    acceptToDocument = notificationService.checkAllowWithDocument(documentId, organId);

                    // add to cache
                    RedisUtil.getInstance().set(RedisKey.getKey(organId
                            + documentId, RedisKey.CHECK_ALLOW_KEY), acceptToDocument);
                }

                if (!acceptToDocument) {
                    errorList.add(new Error(
                            "M.GetDocumentCheckAllow",
                            "Not find document pending in cache, database docId " + documentId + " organId " + organId));

                    report = new Report(false, new ErrorList(errorList));

                    Document bodyChildDocument = xmlUtil
                            .convertEntityToDocument(Report.class, report);

                    map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

                    return map;
                }

                List<Attachment> attachmentsByEntity = attachmentService.getAttachmentsByDocumentId(documentId);
                // get saved doc in cache
                String savedDocStr = RedisUtil.getInstance().get(RedisKey
                        .getKey(String.valueOf(documentId), RedisKey.GET_ENVELOP_FILE), String.class);
                Document savedDoc = null;
                if (savedDocStr != null) {
                    LOGGER.info("Found edxml of document with id " + documentId + " in cache !!!!!!!!!!!!!!!");
                    savedDoc = xmlUtil.getDocumentFromFile(new ByteArrayInputStream(savedDocStr.getBytes(StandardCharsets.UTF_8)));
                }
                if (savedDoc != null) {
                    LOGGER.info("Create mime from edxml file of document with id " + documentId + " in cache !!!!!!!!!!!!!!!");
                    map = archiveMime.createMime(savedDoc, attachmentsByEntity);
                } else {
                    // get info in db
                    MessageHeader messageHeader = documentService.getDocumentById(documentId);
                    LOGGER.info("Get message header success for document id " + documentId);
                    // TODO: Get Trace for edXML Message in here
                    TraceHeaderList traceHeaderList = traceHeaderListService.getTraceHeaderListByDocId(documentId);
                    LOGGER.info("Get trace header list success for document id " + documentId);

                    if (messageHeader != null && traceHeaderList != null) {
                        // parse business info
                        mapper.parseBusinessInfo(traceHeaderList);
                        Ed ed = new Ed(new Header(messageHeader, traceHeaderList));
                        LOGGER.info("Initial Ed success for document id " + documentId + " !!!!!!!!!!!!!!!!!!!!!!!!");
                        map = archiveMime.createMime(ed, attachmentsByEntity);
                        LOGGER.info("Create ed success for get document " + documentId);
                    } else {
                        errorList.add(new Error("M.GetDocument",
                                "Get trace header list or get message header from database null !!!!!!!!!!!!!!!!!!"));
                        report = new Report(false, new ErrorList(errorList));
                        Document bodyChildDocument = xmlUtil.convertEntityToDocument(
                                Report.class, report);
                        map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                    }
                }
            } else {
                errorList.add(new Error("M.GetDocument", "OrganId and documentId required!"));
                report = new Report(false, new ErrorList(errorList));
                Document bodyChildDocument = xmlUtil.convertEntityToDocument(
                        Report.class, report);
                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
            }
        } catch (Exception ex) {
            LOGGER.error("Error get document  with documentId " + documentId + " cause " + Arrays.toString(ex.getStackTrace()));
            errorList.add(new Error("M.GetDocument",
                    "Error process get document " + Arrays.toString(ex.getStackTrace()) + " with documentId " + documentId));

            report = new Report(false, new ErrorList(errorList));

            Document bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);
            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
        }

        return map;
    }

    public Map<String, Object> sendDocument(Document envelop, org.apache.axis2.context.MessageContext messageContext) {

        Map<String, Object> map = new HashMap<>();

        List<Error> errorList = new ArrayList<>();

        List<Attachment> attachmentsEntity;

        MessageHeader messageHeader;

        Document bodyChildDocument;

        TraceHeaderList traceHeaderList;

        SignatureEdoc signature;

        StringBuilder strDocumentId = new StringBuilder();
        Report report = xmlChecker.checkXmlTag(envelop);
        if (report.isIsSuccess()) {
            try {
                // Extract MessageHeader
                messageHeader = extractMime.getMessageHeader(envelop);

                // Extract TraceHeaderList
                traceHeaderList = extractMime.getTraceHeaderList(envelop);

                // Extract signature
                signature = extractMime.getSignature(envelop);

                //check signature
                report = checker.checkSignature(signature);

                if (!report.isIsSuccess()) {
                    bodyChildDocument = xmlUtil.convertEntityToDocument(
                            Report.class, report);

                    map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

                    return map;
                }
                //check message
                report = checker.checkMessageHeader(messageHeader);

                if (!report.isIsSuccess()) {
                    bodyChildDocument = xmlUtil.convertEntityToDocument(
                            Report.class, report);

                    map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

                    return map;
                }

                // check trace header list
                report = checker.checkTraceHeaderList(traceHeaderList);

                if (!report.isIsSuccess()) {
                    bodyChildDocument = xmlUtil.convertEntityToDocument(
                            Report.class, report);

                    map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

                    return map;
                }

                // Get attachment from context
                Map<String, Object> attachments = attachmentUtil
                        .GetAttachmentDocsByContext(messageContext);

                // Check Attachment attachment
                report = attachmentUtil.checkAllowAttachment(envelop,
                        attachments);
                if (!report.isIsSuccess()) {

                    bodyChildDocument = xmlUtil.convertEntityToDocument(Report.class, report);

                    map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

                    return map;
                }

                attachmentsEntity = attachmentUtil.getAttachments(envelop,
                        attachments);

                // only check exist with new document
                if (documentService.checkNewDocument(traceHeaderList)) {
                    // check exist document
                    if (documentService.checkExistDocument(messageHeader.getDocumentId())) {

                        errorList.add(new Error("M.Exist", "Document is exist on ESB !!!!"));
                        report = new Report(false, new ErrorList(errorList));

                        bodyChildDocument = xmlUtil.convertEntityToDocument(
                                Report.class, report);
                        map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

                        return map;
                    }
                }
                List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();

                // add document, document detail, notification, attachment, trace header list
                EdocDocument document = documentService.addDocument(messageHeader, traceHeaderList,
                        attachmentsEntity, strDocumentId, attachmentCacheEntries, errorList);
                if (document == null) {
                    report = new Report(false, new ErrorList(errorList));

                    bodyChildDocument = xmlUtil.convertEntityToDocument(Report.class, report);

                    map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                    return map;
                }
                List<Organization> toesVPCP = checker.checkSendToVPCP(messageHeader.getToes());
                boolean sendVPCP = toesVPCP.size() > 0;
                // not send to vpcp -> save to cache
                if (!sendVPCP) {
                    LOGGER.info("Not send document to VPCP with document id " + strDocumentId);
                    LOGGER.info(messageHeader.getToes());
                    // save envelop file to cache
                    saveEnvelopeFileCache(envelop, strDocumentId.toString());
                } else {
                    LOGGER.info("------------------------- Send to VPCP ------------------------------- " + strDocumentId);
                    // Send to vpcp
                    if (attachmentCacheEntries.size() > 0) {
                        messageHeader.setToes(toesVPCP);
                        document.setSendExt(true);
                        SendEdocResult sendEdocResult = ServiceVPCP.getInstance().sendDocument(messageHeader, traceHeaderList, attachmentCacheEntries);
                        if (sendEdocResult != null) {
                            LOGGER.info("-------------------- Send to VPCP status " + sendEdocResult.getStatus());
                            LOGGER.info("-------------------- Send to VPCP Desc: " + sendEdocResult.getErrorDesc());
                            LOGGER.info("-------------------- Send to VPCP DocID: " + sendEdocResult.getDocID());
                            document.setDocumentExtId(sendEdocResult.getDocID());
                            if (sendEdocResult.getStatus().equals("FAIL") || sendEdocResult.getStatus() == null) {
                                toesVPCP.forEach(to -> {
                                    LOGGER.info("----- Create fail trace for document id " + document.getDocumentId() + " ------ " + to.getOrganId());
                                    MessageStatus messageStatusVPCP = createConfirmTrace(document, to.getOrganId(), false);
                                    traceService.updateTrace(messageStatusVPCP, errorList);
                                });
                            } else {
                                toesVPCP.forEach(to -> {
                                    LOGGER.info("----- Create trace for document id " + document.getDocumentId() + " ------ " + to.getOrganId());
                                    MessageStatus messageStatusVPCP = createConfirmTrace(document, to.getOrganId(), true);
                                    traceService.updateTrace(messageStatusVPCP, errorList);
                                });
                            }
                            if (sendEdocResult.getStatus() != null) {
                                document.setSendSuccess(!sendEdocResult.getStatus().equals("FAIL"));
                                document.setTransactionStatus(sendEdocResult.getErrorDesc());
                            } else {
                                document.setSendSuccess(false);
                                document.setTransactionStatus("");
                            }
                        } else {
                            LOGGER.error("------------------------- Error send document to VPCP with document Id " + strDocumentId);
                            document.setDocumentExtId("");
                        }
                        Date now = new Date();
                        document.setModifiedDate(now);
                        documentService.updateDocument(document);
                    }
                }

                bodyChildDocument = xmlUtil.convertEntityToDocument(Report.class,
                        report);

                Document docIdResponseElm = xmlUtil.getSendResponseDocId(strDocumentId
                        .toString());

                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
                map.put(StringPool.SEND_DOCUMENT_RESPONSE_ID_KEY, docIdResponseElm);
            } catch (Exception e) {
                LOGGER.error("Error send document " + Arrays.toString(e.getStackTrace()));
                errorList.add(new Error("M.SendDocument", "Error send document to esb error trace" + Arrays.toString(e.getStackTrace())));

                report = new Report(false, new ErrorList(errorList));

                bodyChildDocument = xmlUtil.convertEntityToDocument(Report.class, report);

                map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);
            }
        }
        return map;
    }

    public Map<String, Object> getPendingDocumentIds(Document doc) {

        Map<String, Object> map = new HashMap<>();

        Document responseDocument = null;

        List<Long> notifications = null;
        Report report;
        CheckPermission checkPermission = extractMime.getCheckPermissionFromRq(doc, EdXmlConstant.GET_PENDING_DOCUMENT);
        String organId = extractMime.getOrganId(doc, EdXmlConstant.GET_PENDING_DOCUMENT);

        report = checker.checkPermission(checkPermission);

        if (!report.isIsSuccess()) {
            LOGGER.warn("--------------- Get pending permission " + report.isIsSuccess());
            Document bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);

            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

            return map;
        }
        updateReceivedNotify(report, checkPermission);
        if (organId == null || organId.isEmpty()) {
            List<Error> errorList = new ArrayList<>();
            errorList.add(new Error("M.OrganId", "OrganId is required."));
            report = new Report(false, new ErrorList(errorList));
            Document bodyChildDocument = xmlUtil.convertEntityToDocument(
                    Report.class, report);
            map.put(StringPool.CHILD_BODY_KEY, bodyChildDocument);

            return map;
        }

        // TODO: Cache
        List obj = RedisUtil.getInstance().get(RedisKey.getKey(organId, RedisKey.GET_PENDING_KEY), List.class);
        if (obj != null && obj.size() > 0) {
            notifications = CommonUtil.convertToListLong(obj);
        } else {
            try {
                LOGGER.info("---------------Start getDocument with organ: " + organId);
                notifications = notificationService.getDocumentIdsByOrganId(organId);
            } catch (Exception e) {
                log.error(e);
            }
        }

        if (notifications == null) {
            notifications = new ArrayList<>();
        }

        GetPendingDocumentIDResponse response = new ResponseUtil()
                .createGetPendingDocumentIDResponse(notifications);

        try {
            responseDocument = xmlUtil.convertEntityToDocument(
                    GetPendingDocumentIDResponse.class, response);

        } catch (Exception ex) {
            log.error(ex);
        }

        map.put(StringPool.CHILD_BODY_KEY, responseDocument);
        return map;
    }

    private void updateReceivedNotify(Report report, CheckPermission checkPermission) {
        boolean isEnable = GetterUtil.getBoolean(PropsUtil.get("edoc.update.received.notification"), false);
        if (isEnable) {
            LOGGER.info("Running update received notification for organization request to edoc !!!!");
            if (report.isIsSuccess()) {
                String cacheKey = MemcachedKey.getKey(checkPermission.getOrganId(), MemcachedKey.ORGAN_RECEIVED_REPORT);
                Boolean isReceivedNotification = (Boolean) MemcachedUtil.getInstance().read(cacheKey);
                if (isReceivedNotification != null) {
                    if (!isReceivedNotification) {
                        LOGGER.info("Prepare update received notification for dynamic contact " + checkPermission.getOrganId() + " !!!!!");
                        EdocDynamicContact edocDynamicContact = edocDynamicContactService.getDynamicContactByDomain(checkPermission.getOrganId());
                        edocDynamicContact.setReceiveNotify(true);
                        edocDynamicContactService.updateContact(edocDynamicContact);
                        LOGGER.info("Update success received notification for organization " + checkPermission.getOrganId());
                        MemcachedUtil.getInstance().update(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, true);
                    }
                } else {
                    EdocDynamicContact dynamicContact = edocDynamicContactService.getDynamicContactByDomain(checkPermission.getOrganId());
                    if (!dynamicContact.getReceiveNotify()) {
                        LOGGER.info("Prepare update received notification for dynamic contact " + checkPermission.getOrganId() + " !!!!!");
                        dynamicContact.setReceiveNotify(true);
                        edocDynamicContactService.updateContact(dynamicContact);
                        LOGGER.info("Update success received notification for organization " + checkPermission.getOrganId());
                        MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, true);
                    }
                }
            }
        }

    }

    /**
     * save envelop file to cache
     *
     * @param document      - Document Id
     * @param strDocumentId - Document Id String
     */
    private void saveEnvelopeFileCache(Document document, String strDocumentId) {
        try {
            // read document by string
            DOMSource domSource = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            // save envelop file to cache
            RedisUtil.getInstance().set(RedisKey.getKey(strDocumentId, RedisKey.GET_ENVELOP_FILE), writer.toString());
        } catch (Exception e) {
            log.error(e);
        }
    }

    private MessageStatus createConfirmTrace(EdocDocument document, String organId, boolean status) {
        LOGGER.info("---------------- Start create confirm trace with document id " + document.getDocumentId() + " ------------ " + organId);
        MessageStatus messageStatus = new MessageStatus();
        if (status) {
            messageStatus.setDescription("Da nhan van ban ve he thong");
            messageStatus.setStatusCode("1");
        } else {
            messageStatus.setDescription("Gui that bai");
            messageStatus.setStatusCode("999");
        }
        messageStatus.setTimestamp(new Date());

        String fromOrgan = document.getFromOrganDomain();
        String docCode = document.getDocCode();
        Date promulgationDate = document.getPromulgationDate();
        String edXMLDocumentId = fromOrgan + "," + DateUtils.format(document.getPromulgationDate(), DateUtils.DEFAULT_DATE_FORMAT) + "," + docCode;
        EdocDynamicContact toContact = edocDynamicContactService.getDynamicContactByDomain(organId);

        ResponseFor responseFor = new ResponseFor();
        responseFor.setCode(docCode);
        responseFor.setDocumentId(edXMLDocumentId);
        responseFor.setOrganId(fromOrgan);
        responseFor.setPromulgationDate(promulgationDate);

        Organization organization = new Organization();
        organization.setEmail(toContact.getEmail());
        organization.setFax(toContact.getFax());
        organization.setOrganAdd(toContact.getAddress());
        organization.setOrganId(organId);
        organization.setOrganizationInCharge(toContact.getInCharge());
        organization.setOrganName(toContact.getName());
        organization.setTelephone(toContact.getTelephone());
        organization.setWebsite(toContact.getWebsite());

        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setDepartment(toContact.getName());
        staffInfo.setDepartmentId("");
        staffInfo.setEmail(toContact.getEmail());
        staffInfo.setStaff("EdocAdapter");
        staffInfo.setMobile("");
        staffInfo.setStaffId("");

        messageStatus.setStaffInfo(staffInfo);
        messageStatus.setFrom(organization);
        messageStatus.setResponseFor(responseFor);

        LOGGER.info("-------------------- Create confirm trace success for document id " + document.getDocumentId() + " -------------- " + organId);
        return messageStatus;
    }

    /*private static final Logger LOGGER = Logger.getLogger(DynamicService.class);*/
    private static final Logger LOGGER = Logger.getLogger(DynamicService.class);

    @Override
    public void init(SynapseEnvironment synapseEnvironment) {
        LOGGER.info("Dynamic Service Mediator Init !!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void destroy() {

    }

    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final XmlChecker xmlChecker = new XmlChecker();
    private static final ExtractMime extractMime = new ExtractMime();
    private static final Checker checker = new Checker();
    private static final AttachmentUtil attachmentUtil = new AttachmentUtil();
    private static final XmlUtil xmlUtil = new XmlUtil();
}
