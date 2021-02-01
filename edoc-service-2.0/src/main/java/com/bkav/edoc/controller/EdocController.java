package com.bkav.edoc.controller;

import com.bkav.edoc.payload.GetDocumentResp;
import com.bkav.edoc.payload.GetPendingDocIDsResp;
import com.bkav.edoc.payload.SendDocResp;
import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Report;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.base.util.UUidUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import com.bkav.edoc.util.EdocServiceConstant;
import com.bkav.edoc.util.ProcessRequestUtil;
import com.google.gson.Gson;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/EdocService")
public class EdocController {
    private static final Logger LOGGER = Logger.getLogger(EdocController.class);
    private static final Gson gson = new Gson();
    private static final Checker CHECKER = new Checker();
    private final AttachmentGlobalUtil attUtil = new AttachmentGlobalUtil();

    @Value("${edoc.edxml.file.location}")
    private String eDocPath;

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/sendDocument", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseBody
    public String sendDocument(HttpServletRequest request) throws IOException {
        LOGGER.info("----------------------- Send Document Invoke --------------------");
        Map<String, String> headers = ProcessRequestUtil.getHeaders(request);
        List<Error> errors = ProcessRequestUtil.validateHeader(headers);
        SendDocResp sendDocResp = new SendDocResp();
        if (errors.size() > 0) {
            sendDocResp.setDocId(0L);
            sendDocResp.setCode("9999");
            sendDocResp.setStatus("Error");
            LOGGER.warn("Send Document Header Request Error -----------------> " + errors);
        } else {
            try {
                String organId = headers.get(EdocServiceConstant.ORGAN_ID);
                InputStream inputStream = request.getInputStream();
                String docIdUUid = UUidUtils.generate();
                Calendar cal = Calendar.getInstance();
                String SEPARATOR = EdXmlConstant.SEPARATOR;
                if (inputStream == null) {
                    errors.add(new Error("EdocFileError", "File input stream must init !!!!"));
                    sendDocResp.setCode("9999");
                    sendDocResp.setStatus("Error");
                    sendDocResp.setDocId(0L);
                    sendDocResp.setErrors(errors);
                } else {
                    String dataPath = organId + SEPARATOR +
                            cal.get(Calendar.YEAR) + SEPARATOR +
                            (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                            cal.get(Calendar.DAY_OF_MONTH) + SEPARATOR +
                            docIdUUid + ".edxml";

                    String specPath = eDocPath +
                            (eDocPath.endsWith(SEPARATOR) ? "" : SEPARATOR) +
                            dataPath;
                    long size = attUtil.saveToFile(specPath, inputStream);
                    LOGGER.info("Save success input stream to file ---------> " + specPath);
                    File file = new File(specPath);
                    InputStream fileStream = new FileInputStream(file);
                    Ed ed = EdXmlParser.getInstance().parse(fileStream);
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
                            sendDocResp.setDocId(document.getDocumentId());
                            sendDocResp.setStatus("Success");
                            sendDocResp.setCode("0");
                            sendDocResp.setErrors(new ArrayList<>());
                        } else {
                            errors.add(new Error("E.SendDocument", "Error send document with docCode " + messageHeader.getCode().toString()));
                            LOGGER.error("Error save document with document code " + messageHeader.getCode());
                        }
                    } else {
                        sendDocResp.setErrors(errors);
                        sendDocResp.setCode("9999");
                        sendDocResp.setStatus("Error");
                        sendDocResp.setDocId(0L);
                    }
                }
            } catch (Exception e) {
                sendDocResp.setStatus("Error");
                sendDocResp.setCode("9999");
                errors.add(new Error("E.Server", e.getMessage()));
                sendDocResp.setDocId(0L);
                LOGGER.error("Error process request send edoc file cause " + e);
            }
        }
        return gson.toJson(sendDocResp);
    }

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/getPendingDocIds", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getPendingDocIDs(HttpServletRequest request) {
        LOGGER.info("----------------------- Get Pending Doc Ids Invoke --------------------");
        Map<String, String> headerMap = ProcessRequestUtil.getHeaders(request);
        GetPendingDocIDsResp getPendingDocIDsResp = new GetPendingDocIDsResp();
        List<Error> errors = ProcessRequestUtil.validateHeader(headerMap);
        List<Long> notifications = null;
        if (errors.size() > 0) {
            getPendingDocIDsResp.setErrors(errors);
            getPendingDocIDsResp.setCode("9999");
            getPendingDocIDsResp.setStatus("Error");
            LOGGER.warn("Get Pending Doc Ids Header Error -----------------> " + errors);
        } else {
            String organId = headerMap.get(EdocServiceConstant.ORGAN_ID);
            // TODO: Cache
            List obj = RedisUtil.getInstance().get(RedisKey.getKey(organId, RedisKey.GET_PENDING_KEY), List.class);
            if (obj != null && obj.size() > 0) {
                notifications = CommonUtil.convertToListLong(obj);
            } else {
                try {
                    notifications = EdocNotificationServiceUtil.getDocumentIdsByOrganId(organId);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }

            if (notifications == null) {
                notifications = new ArrayList<>();
            }
            LOGGER.info("Get Pending Doc Ids Success -----------------> " + notifications);
            getPendingDocIDsResp.setStatus("Success");
            getPendingDocIDsResp.setDocIDs(notifications);
            getPendingDocIDsResp.setCode("200");
            getPendingDocIDsResp.setErrors(new ArrayList<>());
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
        List<Long> notifications = null;
    }

}
