package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.entity.pagination.DataTableResult;
import com.bkav.edoc.service.database.entity.pagination.DatatableRequest;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.util.*;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.OAuth2Constants;
import com.bkav.edoc.web.auth.CookieUtil;
import com.bkav.edoc.web.payload.DocumentRequest;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.scheduler.bean.EmailSenderBean;
import com.bkav.edoc.web.scheduler.bean.SendMessageToTelegramBean;
import com.bkav.edoc.web.util.CommonUtils;
import com.bkav.edoc.web.util.MessageSourceUtil;
import com.bkav.edoc.web.util.ValidateUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class DocumentRestController {

    private final MessageSourceUtil messageSourceUtil;

    private final SendMessageToTelegramBean sendMessageToTelegramBean;

    private final EmailSenderBean sendEmailBean;

    private final ValidateUtil validateUtil;

    public DocumentRestController(MessageSourceUtil messageSourceUtil, ValidateUtil validateUtil, SendMessageToTelegramBean sendMessageToTelegramBean, EmailSenderBean sendEmailBean) {
        this.messageSourceUtil = messageSourceUtil;
        this.validateUtil = validateUtil;
        this.sendMessageToTelegramBean = sendMessageToTelegramBean;
        this.sendEmailBean = sendEmailBean;
    }

    @RequestMapping(value = "/document/{documentId}", //
            method = RequestMethod.GET, //
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<DocumentCacheEntry> getDocument(@PathVariable("documentId") String documentId) {
        try {
            long docId = Long.parseLong(documentId);
            DocumentCacheEntry document = EdocDocumentServiceUtil.getDocumentById(docId);
            if (document != null) {
                EdocDocumentServiceUtil.updateDocument(docId);
                return new ResponseEntity<>(document, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/document/-/draft/delete/{documentDraftId}", //
            method = RequestMethod.POST, //
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<Response> deleteDraftDocument(@PathVariable("documentDraftId") String documentDraftId) {
        try {
            long docDraftId = Long.parseLong(documentDraftId);
            EdocDocumentServiceUtil.deleteDraftDocument(docDraftId);
            Response response = new Response(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        Response response = new Response(500);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/van-ban/-/document/create", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> createDocument(@RequestBody DocumentRequest documentRequest) {
        LOGGER.error("-------------- Create document invoke -----------------------");
        List<String> errors = new ArrayList<>();
        try {
            String message = "";
            int code = 200;
            if (documentRequest != null) {
                errors = validateUtil.validate(documentRequest);
                if (errors.size() == 0) {
                    List<String> toOrganDomains = documentRequest.getToOrganDomain();
                    boolean draft = documentRequest.getDraft();
                    List<EdocAttachment> attachments = new ArrayList<>();
                    List<Long> attachmentIds = documentRequest.getAttachmentIds();
                    List<EdocAttachment> attachmentsClone = new ArrayList<>();
                    for (Long attachmentId : attachmentIds) {
                        EdocAttachment edocAttachment = EdocAttachmentServiceUtil.findById(attachmentId);
                        attachments.add(edocAttachment);
                        EdocAttachment cloneAttachment = (EdocAttachment) edocAttachment.clone();
                        attachmentsClone.add(cloneAttachment);
                    }
                    for (int i = 0; i < toOrganDomains.size(); i++) {
                        String toOrgan = toOrganDomains.get(i);
                        String subject = documentRequest.getSubject();
                        int priority = documentRequest.getPriority();
                        int docType = documentRequest.getDocumentType();
                        String codeNumber = documentRequest.getCodeNumber();
                        String codeNation = documentRequest.getCodeNation();
                        String staffName = documentRequest.getStaffName();
                        String docCode = codeNumber + "/" + codeNation;
                        String fromOrgan = documentRequest.getFromOrgan();
                        int promulgationAmount = documentRequest.getPromulgationAmount();
                        int pageAmount = documentRequest.getPageAmount();
                        String promulgationDate = documentRequest.getPromulgationDate();
                        String promulgationPlace = documentRequest.getPromulgationPlace();
                        String content = documentRequest.getContent();
                        String dueDate = documentRequest.getDueDate();
                        String signerFullName = documentRequest.getSignerFullName();
                        String signerPosition = documentRequest.getSignerPosition();
                        String sphereOfPromulgation = documentRequest.getSphereOfPromulgation();
                        Date promulgationDateVal = DateUtils.parse(promulgationDate);
                        // Create EdocDocument
                        EdocDocument newDocument = new EdocDocument();
                        EdocDocumentType edocDocumentType = EdocTypeServiceUtil.findById(docType);
                        newDocument.setEdXMLDocId(String.valueOf(53234));
                        newDocument.setSubject(subject);
                        newDocument.setCodeNumber(codeNumber);
                        newDocument.setDocCode(docCode);
                        newDocument.setCodeNotation(codeNation);
                        newDocument.setPromulgationDate(promulgationDateVal);
                        newDocument.setPromulgationPlace(promulgationPlace);
                        newDocument.setDocumentType(docType);
                        newDocument.setDocumentTypeName(edocDocumentType.getValue());
                        newDocument.setDocumentTypeDetail(-1);
                        newDocument.setPriority(priority);
                        newDocument.setToOrganDomain(toOrgan);
                        newDocument.setFromOrganDomain(fromOrgan);
                        newDocument.setSentDate(new Date());
                        Date currentDate = new Date();
                        newDocument.setDraft(draft);
                        newDocument.setCreateDate(currentDate);
                        newDocument.setModifiedDate(currentDate);
                        newDocument.setVisible(true);
                        newDocument.setSendExt(false);
                        newDocument.setVisited(false);
                        EdocDocumentServiceUtil.createDocument(newDocument);
                        // Create EdocDocumentDetail
                        EdocDocumentDetail documentDetail = new EdocDocumentDetail();
                        documentDetail.setContent(content);
                        documentDetail.setSignerCompetence("");
                        documentDetail.setSignerPosition(signerPosition);
                        if (!dueDate.equals("")) {
                            Date duaDateVal = DateUtils.parse(dueDate);
                            documentDetail.setDueDate(duaDateVal);
                        } else {
                            documentDetail.setDueDate(null);
                        }
                        documentDetail.setToPlaces("");
                        documentDetail.setSphereOfPromulgation(sphereOfPromulgation);
                        documentDetail.setTyperNotation("");
                        documentDetail.setPageAmount(pageAmount);
                        documentDetail.setSignerFullName(signerFullName);
                        documentDetail.setResponseFor("");
                        documentDetail.setAppendixes("");
                        documentDetail.setPromulgationAmount(promulgationAmount);
                        documentDetail.setSteeringType(EdocDocumentDetail.SteeringType.STEER);
                        documentDetail.setDocument(newDocument);
                        EdocDocumentServiceUtil.createDocumentDetail(documentDetail);
                        newDocument.setDocumentDetail(documentDetail);
                        // create trace header list
                        EdocTraceHeaderList edocTraceHeaderList = new EdocTraceHeaderList();
                        String newDocumentStr = messageSourceUtil.getMessage("edoc.new.document", null);
                        edocTraceHeaderList.setBusinessDocReason(newDocumentStr);
                        EdocTraceHeaderList.BusinessDocType type = EdocTraceHeaderList.BusinessDocType.values()[0];
                        edocTraceHeaderList.setBusinessDocType(type);
                        edocTraceHeaderList.setPaper(0);
                        edocTraceHeaderList.setBusinessInfo(null);
                        // get staff info
                        edocTraceHeaderList.setEmail("");
                        edocTraceHeaderList.setDepartment("");
                        edocTraceHeaderList.setMobile("");
                        edocTraceHeaderList.setStaff(staffName);
                        // save to database
                        edocTraceHeaderList.setDocument(newDocument);
                        EdocTraceHeaderListServiceUtil.addEdocTraceHeaderList(edocTraceHeaderList);
                        newDocument.setTraceHeaderList(edocTraceHeaderList);
                        //create trace header for trace header list
                        EdocTraceHeader edocTraceHeader = new EdocTraceHeader();
                        edocTraceHeader.setOrganDomain(fromOrgan);
                        edocTraceHeader.setTimeStamp(new Date());
                        edocTraceHeader.setTraceHeaderList(edocTraceHeaderList);
                        EdocTraceHeaderListServiceUtil.addTraceHeader(edocTraceHeader);
                        // add attachment
                        if (i == 0) {
                            for (EdocAttachment attachment : attachments) {
                                attachment.setDocument(newDocument);
                                attachment.setToOrganDomain(toOrgan);
                                attachment.setOrganDomain(fromOrgan);
                                attachment.setCreateDate(new Date());
                                EdocAttachmentServiceUtil.updateAttachment(attachment);

                            }
                        } else {
                            for (EdocAttachment cloneEdocAttachment : attachmentsClone) {
                                cloneEdocAttachment.setDocument(newDocument);
                                cloneEdocAttachment.setToOrganDomain(toOrgan);
                                cloneEdocAttachment.setOrganDomain(fromOrgan);
                                cloneEdocAttachment.setCreateDate(new Date());
                                EdocAttachmentServiceUtil.addAttachment(cloneEdocAttachment);
                            }
                        }
                        // add to Notification
                        EdocNotification notification = new EdocNotification();
                        notification.setDateCreate(new Date());
                        notification.setModifiedDate(new Date());
                        notification.setSendNumber(0);
                        notification.setDueDate(documentDetail.getDueDate());
                        notification.setReceiverId(toOrgan);
                        notification.setDocument(newDocument);
                        notification.setTaken(false);
                        EdocNotificationServiceUtil.addNotification(notification);
                        EdocDocumentServiceUtil.addDocumentToPending(toOrganDomains, newDocument.getDocumentId());
                    }
                    message = draft ? messageSourceUtil.getMessage("edoc.message.create.draft.success", null)
                            : messageSourceUtil.getMessage("edoc.message.create.document.success", null);
                } else {
                    LOGGER.error("Error create document error list " + errors.toString());
                    code = 400;
                    message = messageSourceUtil.getMessage("edoc.message.error.create.document", null);
                }
            }
            Response response = new Response(code, errors, message);
            return new ResponseEntity<>(response, HttpStatus.valueOf(code));
        } catch (Exception e) {
            LOGGER.error("Error create document via web cause ", e);
            errors.add(messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            Response response = new Response(500,
                    errors, messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/documents/{mode}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getDocuments(@PathVariable("mode") String mode, HttpServletRequest request) {
        //String toOrgan = null, fromOrgan = "000.00.33.H36", docCode = null;
        String toOrgan = null, fromOrgan = null, docCode = null;
        String organDomain = CookieUtil.getValue(request, OAuth2Constants.ORGANIZATION);
        DatatableRequest<DocumentCacheEntry> datatableRequest = new DatatableRequest<>(request);
        PaginationCriteria pagination = datatableRequest.getPaginationRequest();
        int totalCount = EdocDocumentServiceUtil.countDocumentsFilter(pagination, organDomain, mode, toOrgan, fromOrgan, docCode);
        List<DocumentCacheEntry> entries = EdocDocumentServiceUtil.getDocumentsFilter(pagination, organDomain, mode, toOrgan, fromOrgan, docCode);
        DataTableResult<DocumentCacheEntry> dataTableResult = new DataTableResult<>();
        dataTableResult.setDraw(datatableRequest.getDraw());
        dataTableResult.setListOfDataObjects(entries);
        dataTableResult = new CommonUtils<DocumentCacheEntry>().getDataTableResult(dataTableResult, entries, totalCount, datatableRequest);
        return new Gson().toJson(dataTableResult);
    }

    @DeleteMapping(value = "/document/delete/{documentId}")
    public HttpStatus deleteDocument(@PathVariable("documentId") Long documentId) {
        if (documentId == null) {
            return HttpStatus.BAD_REQUEST;
        } else {
            boolean deleteResult = EdocDocumentServiceUtil.deleteDocument(documentId);
            if (deleteResult) {
                return HttpStatus.OK;
            } else {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    @RequestMapping(value = "/documents/-/not/taken",method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getAllDocumentNotTaken(HttpServletRequest request) {
        DatatableRequest<DocumentCacheEntry> datatableRequest = new DatatableRequest<>(request);
        PaginationCriteria pagination = datatableRequest.getPaginationRequest();
        int totalCount = EdocDocumentServiceUtil.countDocumentsNotTaken(pagination);
        List<DocumentCacheEntry> entries = EdocDocumentServiceUtil.getDocumentsNotTaken(pagination);
        DataTableResult<DocumentCacheEntry> dataTableResult = new DataTableResult<>();
        dataTableResult.setDraw(datatableRequest.getDraw());
        dataTableResult.setListOfDataObjects(entries);
        dataTableResult = new CommonUtils<DocumentCacheEntry>().getDataTableResult(dataTableResult, entries, totalCount, datatableRequest);
        return new Gson().toJson(dataTableResult);

        /*try {
            List<DocumentCacheEntry> documents = EdocDocumentServiceUtil.getAlldocumentNotTaken();
            if (documents != null) {
                return new ResponseEntity<>(documents, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);*/
    }

    /*@RequestMapping(value = "/edoc/search/advance", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getDocumentBySearchAdvance(HttpServletRequest request,
                                             @RequestParam(value = "fromOrgan", required = false) String fromOrgan,
                                             @RequestParam(value = "toOrgan", required = false) String toOrgan,
                                             @RequestParam(value = "docCode", required = false) String docCode) {

        return null;
    }*/

    @RequestMapping(value = "/send/telegram")
    public HttpStatus sendNotTakenToTelegram() {
        try {
            sendMessageToTelegramBean.runScheduleSendMessageToTelegram();
            return HttpStatus.OK;
        } catch (Exception e) {
            LOGGER.error(e);
            return HttpStatus.BAD_REQUEST;
        }
    }

    @RequestMapping(value = "/send/email")
    public HttpStatus sendNotTakenToEmail() {
        try {
            sendEmailBean.runScheduleSendEmail();
            return HttpStatus.OK;
        } catch (Exception e) {
            LOGGER.error(e);
            return HttpStatus.BAD_REQUEST;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(DocumentRestController.class);

}