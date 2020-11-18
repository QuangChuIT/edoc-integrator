package com.bkav.edoc.web.vpcp;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.util.*;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import com.bkav.edoc.service.xml.status.Status;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.bkav.edoc.service.xml.status.parser.StatusXmlParser;
import com.vpcp.services.KnobstickServiceImp;
import com.vpcp.services.VnptProperties;
import com.vpcp.services.model.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceVPCP {
    private static ServiceVPCP INSTANCE;

    private final KnobstickServiceImp knobstickServiceImp;

    private static final VnptProperties vnptProperties;
    private static final String pStart = "---------";

    static public ServiceVPCP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceVPCP();
        }
        return INSTANCE;
    }

    public ServiceVPCP() {
        this.knobstickServiceImp = new KnobstickServiceImp(vnptProperties);
    }

    public GetChangeStatusResult updateStatus(String statusCode, String documentId) {
        JSONObject header = new JSONObject();
        header.put("status", statusCode);
        header.put("docid", documentId);
        // Update Status after received document
        GetChangeStatusResult GetChangeStatusResult = this.knobstickServiceImp.updateStatus(header.toString());
        if (GetChangeStatusResult != null) {
            LOGGER.info("---------------" + "status:" + GetChangeStatusResult.getStatus());
            LOGGER.info("---------------" + "Desc:" + GetChangeStatusResult.getErrorDesc());
            LOGGER.info("---------------" + "status:" + GetChangeStatusResult.getErrorCode());
        } else {
            LOGGER.error("Error when update status for documentId " + documentId);
        }
        return GetChangeStatusResult;
    }

    public void GetDocuments() {
        // Get list documents sent from VPCP
        LOGGER.info("Invoke GetDocuments from VPCP !!!!!");
        JSONObject getDocumentsHeader = new JSONObject();
        getDocumentsHeader.put("servicetype", "eDoc");
        getDocumentsHeader.put("messagetype", MessageType.edoc);
        GetReceivedEdocResult getReceivedEdocResult = this.knobstickServiceImp.getReceivedEdocList(getDocumentsHeader.toString());
        if (getReceivedEdocResult != null) {
            LOGGER.info(pStart + "status:" + getReceivedEdocResult.getStatus());
            LOGGER.info(pStart + "Desc:" + getReceivedEdocResult.getErrorDesc());
            LOGGER.info(pStart + "Size:" + getReceivedEdocResult.getKnobsticks().size());
            if (getReceivedEdocResult.getKnobsticks().size() > 0) {
                for (Knobstick item : getReceivedEdocResult.getKnobsticks()) {
                    try {
                        LOGGER.info("Prepare get document from VPCP with docId: " + item.getId());
                        JSONObject getDocumentHeader = new JSONObject();
                        String attachmentDir = PropsUtil.get("VPCP.attachment.dir");
                        getDocumentHeader.put("filePath", attachmentDir);
                        getDocumentHeader.put("docId", item.getId());
                        GetEdocResult getEdocResult = this.knobstickServiceImp.getEdoc(getDocumentHeader.toString());
                        JSONObject headerChangeStatus = new JSONObject();
                        headerChangeStatus.put("docid", item.getId());
                        if (getEdocResult != null) {
                            LOGGER.info(pStart + "status:" + getEdocResult.getStatus());
                            LOGGER.info(pStart + "Desc:" + getEdocResult.getErrorDesc());
                            LOGGER.info(pStart + "file:" + getEdocResult.getFilePath());
                            if (getEdocResult.getStatus().equals("OK")) {
                                // TODO insert to database
                                //parse data from edxml
                                File file = new File(getEdocResult.getFilePath());
                                InputStream inputStream = new FileInputStream(file);
                                Ed ed = EdXmlParser.getInstance().parse(inputStream);
                                LOGGER.info("Parser success document from file " + getEdocResult.getFilePath());
                                //Get message header
                                MessageHeader messageHeader = (MessageHeader) ed.getHeader().getMessageHeader();
                                //filter list organ of current organ on esb
                                List<Organization> thisOrganizations = filterOrgan(messageHeader.getToes());
                                messageHeader.setToes(thisOrganizations);
                                // create document
                                EdocDocument edocDocument = MapperUtil.modelToEdocDocument(messageHeader);
                                edocDocument.setReceivedExt(true);
                                edocDocument.setDocumentExtId(item.getId());
                                edocDocument = EdocDocumentServiceUtil.createDocument(edocDocument);
                                if (edocDocument == null) {
                                    continue;
                                }
                                // create document detail
                                EdocDocumentDetail documentDetail = MapperUtil.modelToDocumentDetail(messageHeader);
                                documentDetail.setDocument(edocDocument);
                                documentDetail = EdocDocumentServiceUtil.createDocumentDetail(documentDetail);
                                if (documentDetail == null) {
                                    EdocDocumentServiceUtil.deleteDocument(edocDocument.getDocumentId());
                                    continue;
                                }
                                // create trace header list
                                //Get trace header list
                                TraceHeaderList traceHeaderList = ed.getHeader().getTraceHeaderList();
                                String businessInfo = CommonUtil.getBusinessInfo(traceHeaderList);
                                EdocTraceHeaderList edocTraceHeaderList = EdocTraceHeaderListServiceUtil
                                        .addTraceHeaderList(traceHeaderList, businessInfo, edocDocument);
                                if (edocTraceHeaderList == null) {
                                    EdocDocumentServiceUtil.deleteDocument(edocDocument.getDocumentId());
                                    continue;
                                }
                                //Get attachment
                                List<Attachment> attachments = ed.getAttachments();
                                List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
                                Set<EdocAttachment> edocAttachments = EdocAttachmentServiceUtil.addAttachments(edocDocument, attachments
                                        , attachmentCacheEntries);
                                if (attachmentCacheEntries.size() == 0) {
                                    EdocDocumentServiceUtil.deleteDocument(edocDocument.getDocumentId());
                                    continue;
                                }
                                edocDocument.setAttachments(edocAttachments);
                                // Add notification
                                Set<EdocNotification> notifications = EdocNotificationServiceUtil.addNotifications(messageHeader.getToes(),
                                        messageHeader.getDueDate(), edocDocument);
                                if (notifications.size() == 0) {
                                    EdocDocumentServiceUtil.deleteDocument(edocDocument.getDocumentId());
                                    continue;
                                }
                                EdocDocumentServiceUtil.addDocumentToPendingCached(thisOrganizations, edocDocument.getDocumentId());
                                edocDocument.setNotifications(notifications);
                                LOGGER.info("Done save document vpcp from file " + getEdocResult.getFilePath() + " to database !!!!!!!!");
                                // TODO change status to vpcp
                                headerChangeStatus.put("status", "done");
                            } else {
                                headerChangeStatus.put("status", "fail");
                            }
                            GetChangeStatusResult getChangeStatusResult = this.knobstickServiceImp.updateStatus(headerChangeStatus.toString());
                            LOGGER.info("Confirm receiver for document --------------> " + item.getId());
                            if (getChangeStatusResult != null) {
                                LOGGER.info("Confirm status ---------------> " + getChangeStatusResult.getStatus());
                                LOGGER.info("Confirm error code  ---------------> " + getChangeStatusResult.getErrorCode());
                                LOGGER.info("Confirm error code  ---------------> " + getChangeStatusResult.getErrorCode());
                            } else {
                                LOGGER.error("Error confirm receiver for document of vpcp with id " + item.getId());
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error get list documents from VPCP " + e);
                    }
                }
            }
        } else {
            LOGGER.error("Get list document from vpcp null !!!!");
        }
    }

    public void GetDocumentsTest() throws IOException, ParserException {
        // Get list documents sent from VPCP
        String filePath = "/home/quangcv/backup-school/edoc-integrator/edoc-webapp/src/main/resources/e5f4e280-55b3-460f-be38-e57b80bc3b0d.edxml";
        //parse data from edxml
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Ed ed = EdXmlParser.getInstance().parse(inputStream);
        //Get message header
        MessageHeader messageHeader = (MessageHeader) ed.getHeader().getMessageHeader();
        //filter list organ of current organ on esb
        List<Organization> thisOrganizations = filterOrgan(messageHeader.getToes());
        messageHeader.setToes(thisOrganizations);
        // create document
        EdocDocument edocDocument = MapperUtil.modelToEdocDocument(messageHeader);
        edocDocument.setReceivedExt(true);
        edocDocument.setDocumentExtId("e5f4e280-55b3-460f-be38-e57b80bc3b0d");
        EdocDocumentServiceUtil.createDocument(edocDocument);
        // create document detail
        EdocDocumentDetail documentDetail = MapperUtil.modelToDocumentDetail(messageHeader);
        documentDetail.setDocument(edocDocument);
        EdocDocumentServiceUtil.createDocumentDetail(documentDetail);
        // create trace header list
        //Get trace header list
        TraceHeaderList traceHeaderList = ed.getHeader().getTraceHeaderList();
        String businessInfo = CommonUtil.getBusinessInfo(traceHeaderList);
        EdocTraceHeaderListServiceUtil.addTraceHeaderList(traceHeaderList, businessInfo, edocDocument);
        //Get attachment
        List<Attachment> attachments = ed.getAttachments();
        List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
        Set<EdocAttachment> edocAttachments = EdocAttachmentServiceUtil.addAttachments(edocDocument, attachments
                , attachmentCacheEntries);
        LOGGER.warn(attachmentCacheEntries);
        edocDocument.setAttachments(edocAttachments);
        // Add notification
        Set<EdocNotification> notifications = EdocNotificationServiceUtil.addNotifications(messageHeader.getToes(),
                messageHeader.getDueDate(), edocDocument);
        EdocDocumentServiceUtil.addDocumentToPendingCached(thisOrganizations, edocDocument.getDocumentId());
        edocDocument.setNotifications(notifications);
        // TODO change status to vpcp
    }

    public void getStatus() {
        // Get list status sent from VPCP
        LOGGER.info("Invoke GetStatus from VPCP !!!!!");
        JSONObject getListStatusHeader = new JSONObject();
        getListStatusHeader.put("servicetype", "eDoc");
        getListStatusHeader.put("messagetype", MessageType.status);
        GetReceivedEdocResult getReceivedEdocResult = this.knobstickServiceImp.getReceivedEdocList(getListStatusHeader.toString());
        if (getReceivedEdocResult != null) {
            LOGGER.info(pStart + "status:" + getReceivedEdocResult.getStatus());
            LOGGER.info(pStart + "Desc:" + getReceivedEdocResult.getErrorDesc());
            LOGGER.info(pStart + "Size:" + getReceivedEdocResult.getKnobsticks().size());
            if (getReceivedEdocResult.getKnobsticks().size() > 0) {
                for (Knobstick item : getReceivedEdocResult.getKnobsticks()) {
                    try {
                        LOGGER.info("Prepare get status from VPCP with docId: " + item.getId());
                        JSONObject getStatusHeader = new JSONObject();
                        String attachmentDir = PropsUtil.get("VPCP.attachment.dir");
                        getStatusHeader.put("filePath", attachmentDir);
                        getStatusHeader.put("docId", item.getId());
                        LOGGER.info("Prepare get detail status from vpcp with status id " + item.getId() + " !!!!!!!!!!!!!!!!!!!!!!");
                        GetEdocResult getEdocResult = this.knobstickServiceImp.getEdoc(getStatusHeader.toString());
                        String status = "";
                        if (getEdocResult != null) {
                            LOGGER.info(pStart + "status:" + getReceivedEdocResult.getStatus());
                            LOGGER.info(pStart + "Desc:" + getReceivedEdocResult.getErrorDesc());
                            LOGGER.info(pStart + "Size:" + getReceivedEdocResult.getKnobsticks().size());
                            if (getEdocResult.getStatus().equals("OK")) {
                                // TODO insert to database
                                //parse data from edxml
                                File file = new File(getEdocResult.getFilePath());
                                InputStream inputStream = new FileInputStream(file);
                                Status ed = StatusXmlParser.parse(inputStream);
                                LOGGER.info("Parser success status from file " + getEdocResult.getFilePath());
                                //Get message header
                                MessageStatus messageStatus = (MessageStatus) ed.getHeader().getMessageHeader();
                                LOGGER.info(messageStatus.toString());
                                boolean success = EdocTraceServiceUtil.addTrace(messageStatus);
                                if (success) {
                                    LOGGER.info("Done save status vpcp from file " + getEdocResult.getFilePath() + " to database !!!!!!!!");
                                    status = "done";
                                } else {
                                    status = "fail";
                                }
                            } else {
                                status = "fail";
                                LOGGER.error("Error when get document from vpcp with documentId : " + item.getId());
                            }
                            GetChangeStatusResult changeStatusResult = this.updateStatus(status, item.getId());
                            LOGGER.info("Done get status with id " + item.getId() + " from VPCP !!!!!!!!!!!!");
                            LOGGER.info("Change status result code ---------------> " + changeStatusResult.getStatus());
                            LOGGER.info("Change status result error code ---------------> " + changeStatusResult.getErrorCode());
                            LOGGER.info("Change status result error desc ---------------> " + changeStatusResult.getErrorDesc());
                        } else {
                            LOGGER.error("Get document result null for docId -------------------> " + item.getId());
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error when get list document from VPCP " + e);
                    }
                }
            }
        } else {
            LOGGER.error("Get list document from vpcp null !!!!");
        }
    }

    private List<Organization> filterOrgan(List<Organization> organizations) {
        String currentOrgan = com.bkav.edoc.web.util.PropsUtil.get("edoc.root.organDomain");
        return organizations.stream()
                .filter(organ -> organ.getOrganId().contains(currentOrgan))
                .collect(Collectors.toList());
    }

    static {
        String vpcpEndpoint = PropsUtil.get("VPCP.endpoint");
        String systemId = PropsUtil.get("VPCP.systemId");
        String token = PropsUtil.get("VPCP.token");
        String attachmentDir = PropsUtil.get("VPCP.attachment.dir");
        int maxConnection = GetterUtil.getInteger(PropsUtil.get("VPCP.maxConnection"));
        int retry = GetterUtil.getInteger(PropsUtil.get("VPCP.retry"));
        vnptProperties = new VnptProperties(vpcpEndpoint, systemId, token, attachmentDir, maxConnection, retry);
    }

    public static void main(String[] args) throws IOException, ParserException {
        ServiceVPCP.getInstance().GetDocumentsTest();
    }

    private final static Logger LOGGER = Logger.getLogger(com.bkav.edoc.service.vpcp.ServiceVPCP.class);
}
