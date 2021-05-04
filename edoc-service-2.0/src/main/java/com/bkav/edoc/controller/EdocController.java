package com.bkav.edoc.controller;

import com.bkav.edoc.payload.*;
import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.database.services.EdocAttachmentService;
import com.bkav.edoc.service.database.services.EdocTraceHeaderListService;
import com.bkav.edoc.service.database.services.EdocDocumentService;
import com.bkav.edoc.service.database.services.EdocTraceService;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.database.util.EdocTraceServiceUtil;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.kernel.util.Validator;
import com.bkav.edoc.service.mineutil.Mapper;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.vpcp.ServiceVPCP;
import com.bkav.edoc.service.xml.base.Content;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.base.util.UUidUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.builder.EdXmlBuilder;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import com.bkav.edoc.service.xml.status.builder.StatusXmlBuilder;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.bkav.edoc.service.xml.status.parser.StatusXmlParser;
import com.bkav.edoc.util.EdocServiceConstant;
import com.bkav.edoc.util.EdocUtil;
import com.bkav.edoc.util.MessageType;
import com.google.gson.Gson;
import com.vpcp.services.model.SendEdocResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/EdocService")
public class EdocController {
    private static final Gson gson = new Gson();
    private static final Checker CHECKER = new Checker();
    private final EdocDocumentService documentService = new EdocDocumentService();
    private final EdocTraceService traceService = new EdocTraceService();
    private final EdocTraceHeaderListService traceHeaderListService = new EdocTraceHeaderListService();
    private final EdocAttachmentService attachmentService = new EdocAttachmentService();
    private final AttachmentGlobalUtil attUtil = new AttachmentGlobalUtil();
    private final Mapper mapper = new Mapper();
    private static final Checker checker = new Checker();

    @Value("${edoc.edxml.file.location}")
    private String eDocPath;

    @RequestMapping(value = "/sendDocument", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    public String sendDocument(HttpServletRequest request) {
        LOGGER.info("----------------------- Send Document Invoke --------------------");
        Map<String, String> headers = EdocUtil.getHeaders(request);
        List<Error> errors = new ArrayList<>();
        SendDocResp sendDocResp = new SendDocResp();
        try {
            String organId = headers.get(EdocServiceConstant.ORGAN_ID);
            String messageType = headers.get(EdocServiceConstant.MESSAGE_TYPE);
            if (Validator.isNullOrEmpty(messageType) || !EdocServiceConstant.MESSAGE_TYPES.contains(messageType)) {
                errors.add(new Error("BadRequest", "Bad Request"));
                sendDocResp.setStatus("Fail");
                sendDocResp.setCode("9999");
                sendDocResp.setErrors(errors);
                return gson.toJson(sendDocResp);
            }
            InputStream inputStream = request.getInputStream();
            String docIdUUid = UUidUtils.generate();
            Calendar cal = Calendar.getInstance();
            String SEPARATOR = EdXmlConstant.SEPARATOR;
            if (inputStream == null) {
                LOGGER.info("--------------- Not found edxml file in request !!!!!!!!!-----------------");
                errors.add(new Error("File Not Found", "Not found edxml file in request"));
                sendDocResp.setCode("9999");
                sendDocResp.setStatus("Error");
                sendDocResp.setDocId(0L);
                return gson.toJson(sendDocResp);
            }
            String dataPath;
            if (messageType.equals("EDOC")) {
                LOGGER.info("--------------- Send Document Invoke --------------------");
                dataPath = organId + SEPARATOR +
                        cal.get(Calendar.YEAR) + SEPARATOR +
                        (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                        cal.get(Calendar.DAY_OF_MONTH) + SEPARATOR +
                        EdocServiceConstant.SEND_DOCUMENT + docIdUUid + ".edxml";
            } else {
                LOGGER.info("--------------- Send Status Invoke --------------------");
                dataPath = organId + SEPARATOR +
                        cal.get(Calendar.YEAR) + SEPARATOR +
                        (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                        cal.get(Calendar.DAY_OF_MONTH) + SEPARATOR +
                        EdocServiceConstant.SEND_STATUS + docIdUUid + ".edxml";
            }
            String specPath = eDocPath +
                    (eDocPath.endsWith(SEPARATOR) ? "" : SEPARATOR) +
                    dataPath;
            long size = attUtil.saveToFile(specPath, inputStream);
            LOGGER.info("Save edoc file success with size " + size);
            File file = new File(specPath);
            InputStream fileInputStream = new FileInputStream(file);
            // process add edoc
            if (messageType.equals(MessageType.EDOC.name())) {
                Ed ed = EdXmlParser.getInstance().parse(fileInputStream);
                LOGGER.info("-------------------- Parser document success ------------------");
                //Get message header
                MessageHeader messageHeader = (MessageHeader) ed.getHeader().getMessageHeader();
                // create trace header list
                //Get trace header list
                TraceHeaderList traceHeaderList = ed.getHeader().getTraceHeaderList();
                //Get attachment
                List<Attachment> attachments = ed.getAttachments();
                // validate edxml file
                //check message
                Report reportMessageHeader = CHECKER.checkMessageHeader(messageHeader);
                LOGGER.info("-------- Check message header: " + reportMessageHeader.isIsSuccess());
                Report reportTraceHeader = CHECKER.checkTraceHeaderList(traceHeaderList);
                LOGGER.info("-------- Check trace header list: " + reportTraceHeader.isIsSuccess());
                if (!reportMessageHeader.isIsSuccess()) {
                    errors.addAll(reportMessageHeader.getErrorList().getErrors());
                }
                if (!reportTraceHeader.isIsSuccess()) {
                    errors.addAll(reportTraceHeader.getErrorList().getErrors());
                }
                // only check exist with new document
                if (EdocDocumentServiceUtil.checkNewDocument(traceHeaderList)) {
                    // check exist document
                    if (EdocDocumentServiceUtil.checkExistDocument(messageHeader.getDocumentId())) {
                        errors.add(new Error("ExistDoc", "Document is exist"));
                    }
                }
                if (errors.size() == 0) {
                    LOGGER.info("Start to save document to database !!!!!!!!!!!!!");
                    StringBuilder documentEsbId = new StringBuilder();
                    List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
                    List<Error> errorList = new ArrayList<>();
                    EdocDocument document = EdocDocumentServiceUtil.addDocument(messageHeader,
                            traceHeaderList, attachments, documentEsbId, attachmentCacheEntries, errorList);
                    if (document != null) {
                        LOGGER.info("Save document from  successfully from file  to database document id " + documentEsbId.toString());
                        sendDocResp.setCode("0");
                        sendDocResp.setStatus("Success");
                        sendDocResp.setDocId(document.getDocumentId());
                        EdocUtil.saveEdxmlFilePathToCache(document.getDocumentId(), dataPath);
                        List<Organization> toesVPCP = checker.checkSendToVPCP(messageHeader.getToes());
                        boolean sendVPCP = toesVPCP.size() > 0;
                        // not send to vpcp -> save to cache
                        if (!sendVPCP) {
                            LOGGER.info("Not send document to VPCP with document id " + documentEsbId);
                            LOGGER.info(messageHeader.getToes());
                            // save envelop file to cache
                            //saveEnvelopeFileCache(envelop, strDocumentId.toString());
                        } else {
                            LOGGER.info("---------------------- Send to VPCP ------------------------------- " + documentEsbId);
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
                                        document.setSendSuccess(false);
                                    } else {
                                        document.setSendSuccess(true);
                                    }
                                    document.setTransactionStatus(sendEdocResult.getErrorDesc());
                                } else {
                                    LOGGER.error("------------------------- Error send document to VPCP with document Id " + documentEsbId);
                                    document.setDocumentExtId("");
                                }
                                Date now = new Date();
                                document.setModifiedDate(now);
                                documentService.updateDocument(document);
                            }
                        }
                    } else {
                        errors.add(new Error("SendDocument", "Send document error docCode " + messageHeader.getCode().toString()));
                        errors.addAll(errorList);
                        LOGGER.error("Error save document with docCode " + messageHeader.getCode());
                        sendDocResp.setCode("9999");
                        sendDocResp.setStatus("Fail");
                        sendDocResp.setDocId(0L);
                    }
                } else {
                    sendDocResp.setCode("9999");
                    sendDocResp.setStatus("Fail");
                    sendDocResp.setDocId(0L);
                }
            } else {
                // Process add status
                MessageStatus status = StatusXmlParser.parse(fileInputStream);
                LOGGER.info("Parser success status from file " + specPath);
                Report report = CHECKER.checkMessageStatus(status);
                LOGGER.info("Check message status success !!!!");
                if (!report.isIsSuccess()) {
                    errors.addAll(report.getErrorList().getErrors());
                    sendDocResp.setStatus("Fail");
                    sendDocResp.setCode("9999");
                    sendDocResp.setDocId(0L);
                } else {
                    EdocTrace edocTrace = EdocTraceServiceUtil.addTrace(status, errors);
                    if (edocTrace != null) {
                        sendDocResp.setStatus("Success");
                        sendDocResp.setCode("0");
                        sendDocResp.setDocId(edocTrace.getTraceId());
                        EdocUtil.saveEdxmlFilePathToCache(edocTrace.getTraceId(), dataPath);
                    } else {
                        sendDocResp.setStatus("Error");
                        sendDocResp.setCode("0");
                        sendDocResp.setDocId(0L);
                    }
                }
            }
            sendDocResp.setErrors(errors);
        } catch (Exception e) {
            sendDocResp.setStatus("Error");
            sendDocResp.setCode("9999");
            errors.add(new Error("InternalServer", e.getMessage()));
            sendDocResp.setDocId(0L);
            LOGGER.error("Error process request send edoc file cause " + e);
        }
        return gson.toJson(sendDocResp);
    }

    @RequestMapping(value = "/getPendingDocIds", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getPendingDocIDs(HttpServletRequest request) {
        LOGGER.info("----------------------- Get Pending Doc Ids Invoke --------------------");
        Map<String, String> headerMap = EdocUtil.getHeaders(request);
        GetPendingDocResp getPendingDocResp = new GetPendingDocResp();
        List<Error> errors = new ArrayList<>();
        List<GetPendingResult> getPendingResults = new ArrayList<>();
        List<Long> notifications;
        String organId = headerMap.get(EdocServiceConstant.ORGAN_ID);
        LOGGER.info("OrganID request -------------------------------" + organId);
        try {
            String messageType = headerMap.get(EdocServiceConstant.MESSAGE_TYPE);
            if (!Validator.isNullOrEmpty(messageType) && (messageType.equals("EDOC") || messageType.equals("STATUS"))) {
                if (messageType.equals("EDOC")) {
                    List obj = RedisUtil.getInstance().get(RedisKey.getKey(organId, RedisKey.GET_PENDING_KEY), List.class);
                    if (obj != null && obj.size() > 0) {
                        notifications = CommonUtil.convertToListLong(obj);
                        LOGGER.info("------------------ Get pending document with organ " + organId + " with size " + notifications + " in cache!!!!!!!!!!!");
                        notifications.forEach(notification -> {
                            DocumentCacheEntry documentCacheEntry = EdocDocumentServiceUtil.getDocumentById(notification);
                            if (GetterUtil.getBoolean(PropsUtil.get("edoc.check.organ.is.tayninh"), false)) {
                                OrganizationCacheEntry toOrgan = documentCacheEntry.getToOrgan().get(0);
                                String toOrganId = toOrgan.getDomain();
                                GetPendingResult pendingResult = new GetPendingResult();
                                pendingResult.setDocId(documentCacheEntry.getDocumentId());
                                pendingResult.setOrganId(toOrganId);
                                getPendingResults.add(pendingResult);
                            } else {
                                List<OrganizationCacheEntry> listToOrgan = documentCacheEntry.getToOrgan();
                                List<String> listParentDomain = Arrays.asList(PropsUtil.get("edoc.integrator.center.lamdong").split("\\|"));
                                listToOrgan.forEach(toOrgan -> {
                                    GetPendingResult pendingResult = new GetPendingResult();
                                    if (GetterUtil.getBoolean(PropsUtil.get("edoc.turn.on.vnpt.request"), false)) {
                                        String[] subDomain = toOrgan.getDomain().split("\\.");
                                        String childDomain = subDomain[2] + "." + subDomain[3];
                                        if (listParentDomain.stream().anyMatch(s -> s.contains(childDomain))) {
                                            pendingResult.setDocId(documentCacheEntry.getDocumentId());
                                            pendingResult.setOrganId(toOrgan.getDomain());
                                            getPendingResults.add(pendingResult);
                                        }
                                    } else {
                                        pendingResult.setDocId(documentCacheEntry.getDocumentId());
                                        pendingResult.setOrganId(toOrgan.getDomain());
                                        getPendingResults.add(pendingResult);
                                    }
                                });
                            }
                        });
                    } else {
                        List<EdocNotification> edocNotifications = EdocNotificationServiceUtil.getNotificationsByOrganId(organId);
                        LOGGER.info("---------------- Get pending document with organ " + organId + " with size " + edocNotifications.size());
                        edocNotifications.forEach(notification -> {
                            if (GetterUtil.getBoolean(PropsUtil.get("edoc.check.organ.is.tayninh"), false)) {
                                GetPendingResult result = new GetPendingResult();
                                result.setDocId(notification.getDocument().getDocumentId());
                                result.setOrganId(notification.getDocument().getToOrganDomain());
                                getPendingResults.add(result);
                            } else {
                                List<String> toOrganList = Arrays.asList(notification.getDocument().getToOrganDomain().split("#"));
                                List<String> listParentDomain = Arrays.asList(PropsUtil.get("edoc.integrator.center.lamdong").split("\\|"));
                                toOrganList.forEach(toOrgan -> {
                                    GetPendingResult result = new GetPendingResult();
                                    if (GetterUtil.getBoolean(PropsUtil.get("edoc.turn.on.vnpt.request"), false)) {
                                        String[] subDomain = toOrgan.split("\\.");
                                        String childDomain = subDomain[2] + "." + subDomain[3];
                                        if (listParentDomain.stream().anyMatch(s -> s.contains(childDomain))) {
                                            result.setDocId(notification.getDocument().getDocumentId());
                                            result.setOrganId(toOrgan);
                                            getPendingResults.add(result);
                                            LOGGER.info("-------- Get document pending by VNPT request with organ: " + toOrgan);
                                        }
                                    } else {
                                        result.setDocId(notification.getDocument().getDocumentId());
                                        result.setOrganId(toOrgan);
                                        getPendingResults.add(result);
                                    }
                                });
                            }
                        });
                        //notifications = EdocNotificationServiceUtil.getDocumentIdsByOrganId(organId);
                    }
                    LOGGER.info("Get Pending Doc Ids Success: " + new Gson().toJson(getPendingResults));
                } else {
                    List<EdocTrace> traces = EdocTraceServiceUtil.getEdocTracesByOrganId(organId, null);
                    //notifications = traces.stream().map(EdocTrace::getTraceId).collect(Collectors.toList());
                    traces.forEach(trace -> {
                        GetPendingResult result = new GetPendingResult();
                        result.setDocId(trace.getTraceId());
                        result.setOrganId(trace.getToOrganDomain());
                        getPendingResults.add(result);
                        LOGGER.info("------------- Get trace success for organ " + trace.getToOrganDomain() + " and code " + trace.getCode());
                    });
                    LOGGER.info("--------------- Get Pending trace success with size: " + getPendingResults.size());
                }
                getPendingDocResp.setStatus("Success");
                getPendingDocResp.setPendingResult(getPendingResults);
                getPendingDocResp.setCode("0");
            } else {
                errors.add(new Error("MessageType", "UnSupport Message Type"));
                getPendingDocResp.setStatus("Error");
                getPendingDocResp.setPendingResult(new ArrayList<>());
                getPendingDocResp.setCode("9999");
            }
            getPendingDocResp.setErrors(errors);
        } catch (Exception e) {
            LOGGER.error("Get pending doc ids for organ " + organId + " cause " + e.getMessage());
            errors.add(new Error("GetPending", e.getMessage()));
            getPendingDocResp.setStatus("Error");
            getPendingDocResp.setCode("9999");
            getPendingDocResp.setErrors(errors);
        }
        return gson.toJson(getPendingDocResp);
    }

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/getDocument", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getDocument(HttpServletRequest request) {
        LOGGER.info("----------------------- Get Document Invoke --------------------");
        Map<String, String> headerMap = EdocUtil.getHeaders(request);
        GetDocumentResp getDocumentResp = new GetDocumentResp();
        List<Error> errors = EdocUtil.validateHeader(headerMap);
        if (errors.size() > 0) {
            getDocumentResp.setErrors(errors);
            getDocumentResp.setCode("9999");
            getDocumentResp.setStatus("Error");
            LOGGER.warn("Get Document Error " + errors);
        } else {
            String docIdValue = headerMap.get(EdocServiceConstant.DOC_ID);
            String messageType = headerMap.get(EdocServiceConstant.MESSAGE_TYPE);
            if (Validator.isNullOrEmpty(docIdValue)) {
                errors.add(new Error("DocID", "Invalid DocID"));
                getDocumentResp.setErrors(errors);
                getDocumentResp.setCode("9999");
                getDocumentResp.setStatus("Error");
            } else {
                long docId = Long.parseLong(docIdValue);
                if (!Validator.isNullOrEmpty(messageType) && (messageType.equals("EDOC") || messageType.equals("STATUS"))) {
                    String edXmlFilePath = RedisUtil.getInstance()
                            .get(RedisKey.getKey(String.valueOf(docId), RedisKey.GET_DOCUMENT_EDXML_KEY), String.class);
                    if (edXmlFilePath != null && !edXmlFilePath.equals("")) {
                        String specPath = eDocPath +
                                (eDocPath.endsWith(EdXmlConstant.SEPARATOR) ? "" : EdXmlConstant.SEPARATOR) + edXmlFilePath;
                        File file = new File(specPath);
                        if (file.exists()) {
                            byte[] encode = new byte[0];
                            try {
                                encode = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
                                String data = new String(encode, StandardCharsets.UTF_8);
                                getDocumentResp.setData(data);
                                getDocumentResp.setStatus("Success");
                                getDocumentResp.setCode("0");
                            } catch (IOException e) {
                                errors.add(new Error("Exception", e.getMessage()));
                                getDocumentResp.setErrors(errors);
                                getDocumentResp.setCode("9999");
                                getDocumentResp.setStatus("Error");
                            }

                        } else {
                            getDocumentResp = buildGetDocumentResp(docId, messageType, errors);
                        }
                    } else {
                        getDocumentResp = buildGetDocumentResp(docId, messageType, errors);
                    }
                }
            }
        }
        return gson.toJson(getDocumentResp);
    }

    @RequestMapping(value = "/confirmReceived", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String confirmReceived(HttpServletRequest request) {
        LOGGER.info("----------------------- Confirm Received Invoke --------------------");
        Map<String, String> headerMap = EdocUtil.getHeaders(request);
        ConfirmReceivedResp confirmReceivedResp = new ConfirmReceivedResp();
        List<Error> errors = new ArrayList<>();
        String organId = headerMap.get(EdocServiceConstant.ORGAN_ID);
        try {
            String docIdValue = headerMap.get(EdocServiceConstant.DOC_ID);
            LOGGER.info("------------ Confirm Receiver for document with id " + docIdValue);
            if (Validator.isNullOrEmpty(docIdValue)) {
                errors.add(new Error("DocID", "Invalid DocID"));
                confirmReceivedResp.setCode("9999");
                confirmReceivedResp.setErrors(errors);
                confirmReceivedResp.setStatus("Fail");
            } else {
                long docId = Long.parseLong(docIdValue);
                EdocNotificationServiceUtil.removePendingDocId(organId, docId);
                List<EdocTrace> traces = null;
                if (organId.equals(PropsUtil.get("edoc.domain.A.parent"))) {
                    traces = EdocTraceServiceUtil.getEdocTracesByTraceId(docId);
                } else {
                    traces = EdocTraceServiceUtil.getEdocTracesByOrganId(organId, null);
                }
                if (traces.size() > 0) {
                    traceService.disableEdocTrace(traces);
                }
                confirmReceivedResp.setStatus("Success");
                confirmReceivedResp.setErrors(new ArrayList<>());
                confirmReceivedResp.setCode("0");
                LOGGER.info("-----------Confirm Receiver success for document with id " + docIdValue);
            }
        } catch (Exception e) {
            LOGGER.error("-------------- Confirm Receiver for document with organ domain " + organId + " cause " + e.toString());
            errors.add(new Error("ConfirmReceived", e.getMessage()));
            confirmReceivedResp.setCode("9999");
            confirmReceivedResp.setErrors(errors);
            confirmReceivedResp.setStatus("Fail");
        }
        return gson.toJson(confirmReceivedResp);
    }

    @RequestMapping(value = "/getOrganizations", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getOrganizations(HttpServletRequest request,
                                   @RequestParam(value = "organId") @Nullable String organId) {
        LOGGER.info("----------------------- Get Organizations Invoke --------------------");
        GetOrganizationResp organizationResp = new GetOrganizationResp();
        List<Error> errors = new ArrayList<>();
        try {
            List<EdocDynamicContact> contacts = EdocDynamicContactServiceUtil.getAllChildOrgan(organId);
            List<Organization> organizations = new ArrayList<>();
            contacts.forEach(contact -> {
                Organization organization = new Organization(contact.getDomain(), contact.getInCharge(), contact.getName(),
                        contact.getAddress(), contact.getEmail(), contact.getTelephone(), contact.getFax(), contact.getWebsite());
                organizations.add(organization);
            });
            organizationResp.setOrganizations(organizations);
            organizationResp.setCode("0");
            organizationResp.setErrors(new ArrayList<>());
            organizationResp.setStatus("Success");
        } catch (Exception e) {
            errors.add(new Error("GetOrganizations", e.getMessage()));
            organizationResp.setCode("9999");
            organizationResp.setErrors(errors);
            organizationResp.setStatus("Fail");
        }
        return gson.toJson(organizationResp);
    }

    @RequestMapping(value = "/checkExistDocument", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public CheckExistDocumentResp checkExistDocument(HttpServletRequest request, @RequestBody CheckExistDocumentRequest ob) {
        Map<String, String> headerMap = EdocUtil.getHeaders(request);
        CheckExistDocumentResp checkExistDocumentResp = new CheckExistDocumentResp();
        List<Error> errors = new ArrayList<>();
        String organId = headerMap.get(EdocServiceConstant.ORGAN_ID);
        LOGGER.info("--------------- Check exist document with organ: " + organId);
        try {
            boolean isExistDocument = EdocDocumentServiceUtil.checkExistDocumentByDocCode(ob.getFromOrgan(), ob.getToOrgan(), ob.getDocCode());
            if (isExistDocument) {
                checkExistDocumentResp.setResult(true);
            } else {
                checkExistDocumentResp.setResult(false);
            }
            checkExistDocumentResp.setStatus("Success");
            checkExistDocumentResp.setCode("200");
            checkExistDocumentResp.setErrors(new ArrayList<>());
        } catch (Exception e) {
            LOGGER.error("--------------- Error to check exist document cause: " + e.getMessage());
            errors.add(new Error("CheckExistDocumentException", e.getMessage()));
            checkExistDocumentResp.setErrors(errors);
            checkExistDocumentResp.setCode("9999");
            checkExistDocumentResp.setStatus("Error");
        }
        return checkExistDocumentResp;
    }

    private GetDocumentResp buildGetDocumentResp(long docId, String messageType, List<Error> errors) {
        GetDocumentResp getDocumentResp = new GetDocumentResp();
        try {
            if (messageType.equals("EDOC")) {
                MessageHeader messageHeader = documentService.getDocumentById(docId);
                LOGGER.info("Get message header success for document id " + docId + ": " + messageHeader.toString());
                TraceHeaderList traceHeaderList = traceHeaderListService.getTraceHeaderListByDocId(docId);
                LOGGER.info("Get trace header list success for document id " + docId + ": " + traceHeaderList.toString());
                List<Attachment> attachmentsByEntity = attachmentService.getAttachmentsByDocumentId(docId);
                LOGGER.info("Get list attachment success for document id " + docId + " with size " + attachmentsByEntity.size());
                if (messageHeader != null && traceHeaderList != null && attachmentsByEntity.size() > 0) {
                    LOGGER.info("Start parse business info for document id " + docId);
                    mapper.parseBusinessInfo(traceHeaderList);
                    Ed ed = new Ed(new Header(messageHeader, traceHeaderList), attachmentsByEntity);
                    LOGGER.info("Initial Ed success for document id " + docId + " !!!!!!!!!!!!!!!!!!!!!!!!");
                    String fileName = "GetDocument_" + docId;
                    Content content = EdXmlBuilder.build(ed, fileName, eDocPath);
                    if ((content != null ? content.getContent() : null) != null) {
                        byte[] encode = Base64.encodeBase64(FileUtils.readFileToByteArray(content.getContent()));
                        String data = new String(encode, StandardCharsets.UTF_8);
                        getDocumentResp.setData(data);
                        getDocumentResp.setStatus("Success");
                        getDocumentResp.setCode("0");
                    } else {
                        errors.add(new Error("GetDocument", "Get Document Error"));
                        getDocumentResp.setErrors(errors);
                        getDocumentResp.setCode("9999");
                        getDocumentResp.setStatus("Error");
                    }
                } else {
                    errors.add(new Error("GetDocument", "Get Document From Database Error"));
                    getDocumentResp.setErrors(errors);
                    getDocumentResp.setCode("9999");
                    getDocumentResp.setStatus("Error");
                }
            } else {
                EdocTrace edocTrace = EdocTraceServiceUtil.getEdocTrace(docId);
                List<EdocTrace> traces = new ArrayList<>();
                traces.add(edocTrace);
                List<MessageStatus> messageStatuses = mapper.traceInfoToStatusEntity(traces);
                MessageStatus messageStatus = messageStatuses.get(0);
                Content content = StatusXmlBuilder.build(messageStatus, eDocPath);
                if ((content != null ? content.getContent() : null) != null) {
                    byte[] encode = Base64.encodeBase64(FileUtils.readFileToByteArray(content.getContent()));
                    String data = new String(encode, StandardCharsets.UTF_8);
                    getDocumentResp.setData(data);
                    getDocumentResp.setStatus("Success");
                    getDocumentResp.setCode("0");
                } else {
                    errors.add(new Error("GetDocument", "Get Status Error"));
                    getDocumentResp.setErrors(errors);
                    getDocumentResp.setCode("9999");
                    getDocumentResp.setStatus("Error");
                }
            }
        } catch (Exception e) {
            errors.add(new Error("GetDocumentException", e.getMessage()));
            getDocumentResp.setErrors(errors);
            getDocumentResp.setCode("9999");
            getDocumentResp.setStatus("Error");
        }
        return getDocumentResp;
    }

    private static final Logger LOGGER = Logger.getLogger(EdocController.class);
}
