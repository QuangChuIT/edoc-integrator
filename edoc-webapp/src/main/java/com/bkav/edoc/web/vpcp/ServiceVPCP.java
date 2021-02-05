package com.bkav.edoc.web.vpcp;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocTraceServiceUtil;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.util.CommonUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import com.bkav.edoc.service.xml.status.Status;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.bkav.edoc.service.xml.status.parser.StatusXmlParser;
import com.bkav.edoc.web.util.TokenUtil;
import com.vpcp.services.AgencyServiceImp;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceVPCP {
    private static ServiceVPCP INSTANCE;

    private final KnobstickServiceImp knobstickServiceImp;
    private final AgencyServiceImp agencyService;
    private static final VnptProperties vnptProperties;
    private static final String pStart = "---------";

    static public ServiceVPCP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceVPCP();
        }
        return INSTANCE;
    }

    public ServiceVPCP() {
        this.agencyService = new AgencyServiceImp(vnptProperties);
        this.knobstickServiceImp = new KnobstickServiceImp(vnptProperties);
    }

    public void GetAgencies() {
        LOGGER.info("------------------------------ Get Agencies invoke --------------------------------------------------------");
        try {
            GetAgenciesResult getAgenciesResult = this.agencyService.getAgenciesList("{}");
            if (getAgenciesResult != null) {
                LOGGER.info("--------------------------------------------------- " + "status:" + getAgenciesResult.getStatus());
                LOGGER.info("--------------------------------------------------- " + "Desc:" + getAgenciesResult.getErrorDesc());
                LOGGER.info("--------------------------------------------------- " + "Size:" + getAgenciesResult.getAgencies().size());
                List<Agency> agencies = getAgenciesResult.getAgencies();
                int success = 0;
                int error = 0;
                int duplicate = 0;
                if (agencies.size() > 0) {
                    for (Agency agency : agencies) {
                        String domain = agency.getCode();
                        String name = agency.getName();
                        String mail = agency.getMail();
                        if ((domain == null || domain.equals("")) || (name == null || name.equals(""))) {
                            error++;
                        }
                        EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(domain);
                        if (edocDynamicContact != null) {
                            duplicate++;
                        } else {
                            EdocDynamicContact contact = new EdocDynamicContact();
                            contact.setEmail(mail);
                            contact.setDomain(domain);
                            contact.setName(name);
                            String token = TokenUtil.getRandomNumber(domain, name);
                            contact.setToken(token);
                            EdocDynamicContactServiceUtil.createContact(contact);
                            success++;
                        }
                    }
                    String message = "Successfully synchronized organization from VPCP success: " +
                            success + " error: " + error + " duplicate: " + duplicate + " !";
                    LOGGER.info(message);
                } else {
                    LOGGER.warn("Get agencies with size 0 not process !!!!!!");
                }
            } else {
                LOGGER.warn("Get agencies result null not process !!!!!!");
            }
        } catch (Exception e) {
            LOGGER.error("Error synchronized organization from VPCP cause " + Arrays.toString(e.getStackTrace()));
        }
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
                                // create trace header list
                                //Get trace header list
                                TraceHeaderList traceHeaderList = ed.getHeader().getTraceHeaderList();
                                //Get attachment
                                List<Attachment> attachments = ed.getAttachments();
                                //filter list organ of current organ on esb
                                List<Organization> thisOrganizations = filterOrgan(messageHeader.getToes());
                                messageHeader.setToes(thisOrganizations);
                                StringBuilder documentEsbId = new StringBuilder();
                                List<Error> errors = new ArrayList<>();
                                List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
                                EdocDocument document = EdocDocumentServiceUtil.addDocument(messageHeader,
                                        traceHeaderList, attachments, documentEsbId, attachmentCacheEntries, errors);
                                if (document != null) {
                                    LOGGER.info("Save document from vpcp successfully from file " + getEdocResult.getFilePath() + " to database !!!!!!!!");
                                    document.setDocumentExtId(item.getId());
                                    document.setReceivedExt(true);
                                    EdocDocumentServiceUtil.updateDocument(document);
                                    LOGGER.info("Update document from vpcp successfully for document " + documentEsbId.toString());
                                    // TODO change status to vpcp
                                    headerChangeStatus.put("status", "done");
                                } else {
                                    LOGGER.error("Error save document from vpcp  with document code " + messageHeader.getCode());
                                    headerChangeStatus.put("status", "fail");
                                }
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
        //Get attachment
        List<Attachment> attachments = ed.getAttachments();
        // create trace header list
        //Get trace header list
        TraceHeaderList traceHeaderList = ed.getHeader().getTraceHeaderList();
        String businessInfo = CommonUtil.getBusinessInfo(traceHeaderList);
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
                                List<Error> errors = new ArrayList<>();
                                EdocTrace edocTrace = EdocTraceServiceUtil.addTrace(messageStatus, errors);
                                if (edocTrace != null) {
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
        List<String> organs = new ArrayList<>();
        List<Organization> result = new ArrayList<>();
        organizations.forEach(o -> organs.add(o.getOrganId()));
        List<EdocDynamicContact> dynamicContacts = EdocDynamicContactServiceUtil.getContactsByMultipleDomains(organs);
        if (dynamicContacts.size() > 0) {
            List<EdocDynamicContact> agencies = dynamicContacts.stream().filter(EdocDynamicContact::getAgency).collect(Collectors.toList());
            if (agencies.size() > 0) {
                List<String> organDomains = agencies.stream().map(EdocDynamicContact::getDomain).collect(Collectors.toList());
                for (Organization domain : organizations) {
                    if (organDomains.contains(domain.getOrganId())) {
                        result.add(domain);
                    }
                }
            }
        }
        return result;
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
