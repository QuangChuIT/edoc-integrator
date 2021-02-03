package com.bkav.edoc.controller;

import com.bkav.edoc.payload.*;
import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.database.services.EdocAttachmentService;
import com.bkav.edoc.service.database.services.EdocDocumentService;
import com.bkav.edoc.service.database.services.EdocTraceHeaderListService;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.database.util.EdocTraceServiceUtil;
import com.bkav.edoc.service.kernel.util.Validator;
import com.bkav.edoc.service.mineutil.Mapper;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.xml.base.Content;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.base.util.UUidUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.builder.EdXmlBuilder;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import com.bkav.edoc.service.xml.status.Status;
import com.bkav.edoc.service.xml.status.builder.StatusXmlBuilder;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.bkav.edoc.service.xml.status.parser.StatusXmlParser;
import com.bkav.edoc.util.EdocServiceConstant;
import com.bkav.edoc.util.MessageType;
import com.bkav.edoc.util.ProcessRequestUtil;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/EdocService")
public class EdocController {
    private static final Logger LOGGER = Logger.getLogger(EdocController.class);
    private static final Gson gson = new Gson();
    private static final Checker CHECKER = new Checker();
    private final EdocDocumentService documentService = new EdocDocumentService();
    private final EdocTraceHeaderListService traceHeaderListService = new EdocTraceHeaderListService();
    private final EdocAttachmentService attachmentService = new EdocAttachmentService();
    private final AttachmentGlobalUtil attUtil = new AttachmentGlobalUtil();
    private final Mapper mapper = new Mapper();

    @Value("${edoc.edxml.file.location}")
    private String eDocPath;

    @RequestMapping(value = "/sendDocument", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    public String sendDocument(HttpServletRequest request) {
        LOGGER.info("----------------------- Send Document Invoke --------------------");
        Map<String, String> headers = ProcessRequestUtil.getHeaders(request);
        List<Error> errors = new ArrayList<>();
        SendDocResp sendDocResp = new SendDocResp();
        try {
            String organId = headers.get(EdocServiceConstant.ORGAN_ID);
            String messageType = headers.get(EdocServiceConstant.MESSAGE_TYPE);
            InputStream inputStream = request.getInputStream();
            String docIdUUid = UUidUtils.generate();
            Calendar cal = Calendar.getInstance();
            String SEPARATOR = EdXmlConstant.SEPARATOR;
            if (inputStream == null) {
                errors.add(new Error("EdocFileError", "File Doc input stream must init !!!!"));
                sendDocResp.setCode("9999");
                sendDocResp.setStatus("Error");
                sendDocResp.setDocId(0L);
            } else if (!Validator.isNullOrEmpty(messageType) && (messageType.equals("EDOC") || messageType.equals("STATUS"))) {
                String dataPath;
                if (messageType.equals("EDOC")) {
                    dataPath = organId + SEPARATOR +
                            cal.get(Calendar.YEAR) + SEPARATOR +
                            (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                            cal.get(Calendar.DAY_OF_MONTH) + SEPARATOR +
                            EdocServiceConstant.SEND_DOCUMENT + docIdUUid + ".edxml";
                } else {
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
                    LOGGER.info("-------------------- Parser success document --------------------");
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
                    Report reportTraceHeader = CHECKER.checkTraceHeaderList(traceHeaderList);
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
                            errors.add(new Error("M.Exist", "Document is exist on ESB !!!!"));
                        }
                    }
                    if (errors.size() == 0) {
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
                        } else {
                            errors.add(new Error("E.SendDocument", "Error send document with docCode " + messageHeader.getCode().toString()));
                            errors.addAll(errorList);
                            LOGGER.error("Error save document with document code " + messageHeader.getCode());
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
                    Status status = StatusXmlParser.parse(fileInputStream);
                    LOGGER.info("Parser success status from file " + specPath);
                    //Get message header
                    MessageStatus messageStatus = (MessageStatus) status.getHeader().getMessageHeader();
                    EdocTrace edocTrace = EdocTraceServiceUtil.addTrace(messageStatus, errors);
                    if (edocTrace != null) {
                        sendDocResp.setStatus("Success");
                        sendDocResp.setErrors(errors);
                        sendDocResp.setCode("0");
                        sendDocResp.setDocId(edocTrace.getTraceId());
                    } else {
                        sendDocResp.setStatus("Error");
                        sendDocResp.setErrors(errors);
                        sendDocResp.setCode("0");
                    }
                }
            } else {
                errors.add(new Error("M.MessageType", "UnSupport Message Type !!!"));
            }
            sendDocResp.setErrors(errors);
        } catch (Exception e) {
            sendDocResp.setStatus("Error");
            sendDocResp.setCode("9999");
            errors.add(new Error("E.Server", e.getMessage()));
            sendDocResp.setDocId(0L);
            LOGGER.error("Error process request send edoc file cause " + e);
        }
        return gson.toJson(sendDocResp);
    }

    @RequestMapping(value = "/getPendingDocIds", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getPendingDocIDs(HttpServletRequest request) {
        LOGGER.info("----------------------- Get Pending Doc Ids Invoke --------------------");
        Map<String, String> headerMap = ProcessRequestUtil.getHeaders(request);
        GetPendingDocIDsResp getPendingDocIDsResp = new GetPendingDocIDsResp();
        List<Error> errors = new ArrayList<>();
        List<Long> notifications;
        String organId = headerMap.get(EdocServiceConstant.ORGAN_ID);
        try {
            String messageType = headerMap.get(EdocServiceConstant.MESSAGE_TYPE);
            if (!Validator.isNullOrEmpty(messageType) && (messageType.equals("EDOC") || messageType.equals("STATUS"))) {
                if (messageType.equals("EDOC")) {
                    List obj = RedisUtil.getInstance().get(RedisKey.getKey(organId, RedisKey.GET_PENDING_KEY), List.class);
                    if (obj != null && obj.size() > 0) {
                        notifications = CommonUtil.convertToListLong(obj);
                    } else {
                        notifications = EdocNotificationServiceUtil.getDocumentIdsByOrganId(organId);
                    }
                    if (notifications == null) {
                        notifications = new ArrayList<>();
                    }
                    LOGGER.info("Get Pending Doc Ids Success -----------------> " + notifications);
                } else {
                    List<EdocTrace> traces = EdocTraceServiceUtil.getEdocTracesByOrganId(organId);
                    notifications = traces.stream().map(EdocTrace::getTraceId).collect(Collectors.toList());
                    LOGGER.info("Get Pending Trace Ids Success -----------------> " + notifications);
                }
                getPendingDocIDsResp.setStatus("Success");
                getPendingDocIDsResp.setDocIDs(notifications);
                getPendingDocIDsResp.setCode("0");
            } else {
                errors.add(new Error("M.MessageType", "UnSupport Message Type !!!"));
                getPendingDocIDsResp.setStatus("Error");
                getPendingDocIDsResp.setDocIDs(new ArrayList<>());
                getPendingDocIDsResp.setCode("9999");
            }
            getPendingDocIDsResp.setErrors(errors);
        } catch (Exception e) {
            LOGGER.error("Error get pending doc ids for organ " + organId + " cause " + e.getMessage());
            errors.add(new Error("M.GetPending", e.getMessage()));
            getPendingDocIDsResp.setStatus("Error");
            getPendingDocIDsResp.setCode("9999");
            getPendingDocIDsResp.setErrors(errors);
        }
        return gson.toJson(getPendingDocIDsResp);
    }

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/getDocument", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getDocument(HttpServletRequest request) {
        LOGGER.info("----------------------- Get Document Invoke --------------------");
        Map<String, String> headerMap = ProcessRequestUtil.getHeaders(request);
        GetDocumentResp getDocumentResp = new GetDocumentResp();
        List<Error> errors = ProcessRequestUtil.validateHeader(headerMap);
        if (errors.size() > 0) {
            getDocumentResp.setErrors(errors);
            getDocumentResp.setCode("9999");
            getDocumentResp.setStatus("Error");
            LOGGER.warn("Get Document Error -----------------> " + errors);
        } else {
            try {
                String docIdValue = headerMap.get(EdocServiceConstant.DOC_ID);
                String messageType = headerMap.get(EdocServiceConstant.MESSAGE_TYPE);
                if (Validator.isNullOrEmpty(docIdValue)) {
                    errors.add(new Error("R.DocID", "DocId must init !!!!"));
                    getDocumentResp.setErrors(errors);
                    getDocumentResp.setCode("9999");
                    getDocumentResp.setStatus("Error");
                } else {
                    long docId = Long.parseLong(docIdValue);
                    if (!Validator.isNullOrEmpty(messageType) && (messageType.equals("EDOC") || messageType.equals("STATUS"))) {
                        if (messageType.equals("EDOC")) {
                            MessageHeader messageHeader = documentService.getDocumentById(docId);
                            LOGGER.info("Get message header success for document id " + docId);
                            TraceHeaderList traceHeaderList = traceHeaderListService.getTraceHeaderListByDocId(docId);
                            LOGGER.info("Get trace header list success for document id " + docId);
                            List<Attachment> attachmentsByEntity = attachmentService.getAttachmentsByDocumentId(docId);
                            LOGGER.info("Get list attachment success for document id " + docId);
                            if (messageHeader != null && traceHeaderList != null && attachmentsByEntity.size() > 0) {
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
                                    getDocumentResp.setFilePath(fileName);
                                } else {
                                    errors.add(new Error("M.GetDocument", "Get document error !"));
                                    getDocumentResp.setErrors(errors);
                                    getDocumentResp.setCode("9999");
                                    getDocumentResp.setStatus("Error");
                                }
                            } else {
                                errors.add(new Error("M.GetDocument", "Get document from database error !"));
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
                            Header header = new Header(messageStatus);
                            Status status = new Status(header);
                            Content content = StatusXmlBuilder.build(status, eDocPath);
                            if ((content != null ? content.getContent() : null) != null) {
                                byte[] encode = Base64.encodeBase64(FileUtils.readFileToByteArray(content.getContent()));
                                String data = new String(encode, StandardCharsets.UTF_8);
                                getDocumentResp.setData(data);
                                getDocumentResp.setStatus("Success");
                                getDocumentResp.setCode("0");
                                getDocumentResp.setFilePath(content.getContent().getPath());
                            } else {
                                errors.add(new Error("M.GetDocument", "Get document error !"));
                                getDocumentResp.setErrors(errors);
                                getDocumentResp.setCode("9999");
                                getDocumentResp.setStatus("Error");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
        return gson.toJson(getDocumentResp);
    }

    @RequestMapping(value = "/confirmReceived", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String confirmReceived(HttpServletRequest request) {
        LOGGER.info("----------------------- Confirm Received Invoke --------------------");
        Map<String, String> headerMap = ProcessRequestUtil.getHeaders(request);
        ConfirmReceivedResp confirmReceivedResp = new ConfirmReceivedResp();
        List<Error> errors = new ArrayList<>();
        String organId = headerMap.get(EdocServiceConstant.ORGAN_ID);
        try {
            String docIdValue = headerMap.get(EdocServiceConstant.DOC_ID);
            if (Validator.isNullOrEmpty(docIdValue)) {
                errors.add(new Error("M.DocID", "Document Id must init !!!"));
                confirmReceivedResp.setCode("9999");
                confirmReceivedResp.setErrors(errors);
                confirmReceivedResp.setStatus("Fail");
            } else {
                long docId = Long.parseLong(docIdValue);
                EdocNotificationServiceUtil.removePendingDocId(organId, docId);
                confirmReceivedResp.setStatus("Success");
                confirmReceivedResp.setErrors(new ArrayList<>());
                confirmReceivedResp.setCode("0");
            }
        } catch (Exception e) {
            errors.add(new Error("M.ConfirmReceived", e.getMessage()));
            confirmReceivedResp.setCode("9999");
            confirmReceivedResp.setErrors(errors);
            confirmReceivedResp.setStatus("Fail");
        }
        return gson.toJson(confirmReceivedResp);
    }

    @RequestMapping(value = "/getOrganizations", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getOrganizations(HttpServletRequest request) {
        LOGGER.info("----------------------- Get Organizations Invoke --------------------");
        GetOrganizationResp organizationResp = new GetOrganizationResp();
        List<Error> errors = new ArrayList<>();
        try {
            List<EdocDynamicContact> contacts = EdocDynamicContactServiceUtil.getAllDynamicContacts();
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
            errors.add(new Error("M.ConfirmReceived", e.getMessage()));
            organizationResp.setCode("9999");
            organizationResp.setErrors(errors);
            organizationResp.setStatus("Fail");
        }
        return gson.toJson(organizationResp);
    }
}
