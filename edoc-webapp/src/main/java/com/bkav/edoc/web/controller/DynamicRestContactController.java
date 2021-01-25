package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.pagination.DataTableResult;
import com.bkav.edoc.service.database.entity.pagination.DatatableRequest;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.payload.ContactRequest;
import com.bkav.edoc.web.payload.ImportExcelError;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.util.*;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class DynamicRestContactController {

    private final MessageSourceUtil messageSourceUtil;
    private final ValidateUtil validateUtil;

    public DynamicRestContactController(MessageSourceUtil messageSourceUtil, ValidateUtil validateUtil) {
        this.messageSourceUtil = messageSourceUtil;
        this.validateUtil = validateUtil;
    }

    @RequestMapping(value = "/contact/-/document/contacts", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String getAllContact(HttpServletRequest request) {
        DatatableRequest<OrganizationCacheEntry> datatableRequest = new DatatableRequest<>(request);
        PaginationCriteria pagination = datatableRequest.getPaginationRequest();
        Map<String, Object> map = EdocDynamicContactServiceUtil.getContacts(pagination);
        DataTableResult<OrganizationCacheEntry> dataTableResult = new DataTableResult<>();
        int count = 0;
        List<OrganizationCacheEntry> organs = new ArrayList<>();
        if (map != null) {
            count = (int) map.get("totalContacts");
            organs = (List<OrganizationCacheEntry>) map.get("contacts");
        }
        dataTableResult.setDraw(datatableRequest.getDraw());
        dataTableResult.setListOfDataObjects(organs);
        dataTableResult.setRecordsTotal(count);
        dataTableResult = new CommonUtils<OrganizationCacheEntry>().getDataTableResult(dataTableResult, organs, count, datatableRequest);
        return new Gson().toJson(dataTableResult);
    }

    @RequestMapping(value = "/contact/-/document/contacts/{organId}", //
            method = RequestMethod.GET, //
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<EdocDynamicContact> getOrgan(@PathVariable("organId") String organId) {
        try {
            long organ_Id = Long.parseLong(organId);
            EdocDynamicContact organ = EdocDynamicContactServiceUtil.findDynamicContactById(organ_Id);
            if (organ != null) {
                return new ResponseEntity<>(organ, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/contact/-/document/contact/token/{organId}", method = RequestMethod.POST)
    public ResponseEntity<OrganizationCacheEntry> updateToken(@PathVariable("organId") String organId) {
        if (organId != null) {
            long contactId = Long.parseLong(organId);
            EdocDynamicContact contact = EdocDynamicContactServiceUtil.findDynamicContactById(contactId);
            OrganizationCacheEntry organizationCacheEntry = MapperUtil.modelToOrganCache(contact);
            String newToken = TokenUtil.getRandomNumber(contact.getDomain(), contact.getName());
            contact.setToken(newToken);
            EdocDynamicContactServiceUtil.updateContact(organizationCacheEntry, contact);
            OrganizationCacheEntry cacheEntry = MapperUtil.modelToOrganCache(contact);
            return new ResponseEntity<>(cacheEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @RequestMapping(value = "/contact/-/update/contact", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> editContact(@RequestBody ContactRequest contactRequest) {
        List<String> errors = new ArrayList<>();
        try {
            String message = "";
            int code = 200;
            if (contactRequest != null) {
                errors = validateUtil.validateAddOrgan(contactRequest);
                if (errors.size() == 0) {
                    EdocDynamicContact organ = EdocDynamicContactServiceUtil.findDynamicContactById(contactRequest.getId());
                    OrganizationCacheEntry organizationCacheEntry = MapperUtil.modelToOrganCache(organ);
                    organ.setDomain(contactRequest.getDomain());
                    organ.setName(contactRequest.getName());
                    organ.setAddress(contactRequest.getAddress());
                    organ.setEmail(contactRequest.getEmail());
                    organ.setInCharge(contactRequest.getInCharge());
                    organ.setAgency(contactRequest.getAgency());
                    organ.setReceiveNotify(contactRequest.getReceivedNotify());
                    if (!contactRequest.getTelephone().equals(""))
                        organ.setTelephone(contactRequest.getTelephone());

                    EdocDynamicContactServiceUtil.updateContact(organizationCacheEntry, organ);
                    message = messageSourceUtil.getMessage("organ.message.edit.success", null);
                } else {
                    code = 400;
                    message = messageSourceUtil.getMessage("organ.message.edit.fail", null);
                }
            }
            Response response = new Response(code, errors, message);
            return new ResponseEntity<>(response, HttpStatus.valueOf(code));
        } catch (Exception e) {
            errors.add(messageSourceUtil.getMessage("edoc.message.error.exception", null));
            Response response = new Response(500, errors, messageSourceUtil.getMessage("edoc.message.error.exception", null));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Excel File Upload
     */
    @RequestMapping(method = RequestMethod.POST,
            value = "/public/-/organ/import", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String importOrganFromExcel(@RequestParam("fileOrganToUpload") MultipartFile file) {
        logger.info("API import organs from excel invoke !!!!!!!!!!!!!!!!!");
        Response response = null;
        try {
            if (validateUtil.checkExtensionFile(file)) {
                if(validateUtil.checkHeaderExcelFileForOrgan(file)) {
                    Map<String, Object>  map = ExcelUtil.importOrganFromExcel(file);
                    List<ImportExcelError> importExcelErrors = (List<ImportExcelError>) map.get("errors");
                    if (importExcelErrors.size() > 0) {
                        List<String> errors = messageSourceUtil.convertToMessage(importExcelErrors);
                        response = new Response(400, new ArrayList<>(), messageSourceUtil.getMessage("data.invalid", null));
                    } else {
                        List<EdocDynamicContact> organs = (List<EdocDynamicContact>) map.get("organs");
                        for (EdocDynamicContact organ: organs) {
                            EdocDynamicContactServiceUtil.createContact(organ);
                        }
                        logger.info("Convert data from excel success with organ size " + organs.size() + "!!!!!!!!");
                        String readFileSuccess = messageSourceUtil.getMessage("edoc.message.read.organ.file.success", null);
                        response = new Response(200, new ArrayList<>(), readFileSuccess);
                    }
                } else {
                    response = new Response(400, new ArrayList<>(), messageSourceUtil.getMessage("organ.error.header", null));
                }
            } else {
                String invalidFormat = messageSourceUtil.getMessage("edoc.message.user.file.format.error", null);
                response = new Response(400, new ArrayList<>(), invalidFormat);
            }
        } catch (Exception e) {
            String uploadExcelError = messageSourceUtil.getMessage("edoc.message.file.upload.error", null);
            List<String> error = new ArrayList<>();
            error.add(e.getMessage());
            logger.error(uploadExcelError + e.getMessage());
            response = new Response(500, error, uploadExcelError);
        }
        return new Gson().toJson(response);
    }

    @RequestMapping(value = "/public/-/organ/export", method = RequestMethod.GET)
    public void exportOrganToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String currentDate = DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT_D);

        String headerValue = "attachment; filename=To_chuc-" + currentDate + ".xlsx";
        response.setHeader(headerKey, headerValue);
        ExcelUtil.exportOrganToExcel(response);
    }

    @RequestMapping(value = "/public/-/organ/export/sample", method = RequestMethod.GET)
    public void ExportSampleUserExcelFile(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headerValue = "attachment; filename=To_chuc-(File_mau).xlsx";
        response.setHeader(headerKey, headerValue);
        ExcelUtil.exportOrganSampleExcelFile(response);
    }

    @DeleteMapping(value = "/public/-/organ/delete/{organId}")
    public HttpStatus deleteOrgan(@PathVariable("organId") Long organId) {
        if (organId == null) {
            return HttpStatus.BAD_REQUEST;
        } else {
            boolean deleteResult = EdocDynamicContactServiceUtil.deleteContact(organId);
            if (deleteResult) {
                return HttpStatus.OK;
            } else {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    @RequestMapping(value = "/public/-/organ/create", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> createUser(@RequestBody ContactRequest contactRequest) {
        List<String> errors = new ArrayList<>();
        try {
            String message = "";
            int code = 200;
            if (contactRequest != null) {
                errors = validateUtil.validateAddOrgan(contactRequest);
                if (errors.size() == 0) {
                    String name = contactRequest.getName();
                    String domain = contactRequest.getDomain();
                    String inChart = contactRequest.getInCharge();
                    String email = contactRequest.getEmail();
                    String address = contactRequest.getAddress();
                    String telephone = contactRequest.getTelephone();
                    boolean agency = contactRequest.getAgency();
                    boolean receivedNotify = contactRequest.getReceivedNotify();

                    EdocDynamicContact organ = new EdocDynamicContact();
                    organ.setName(name);
                    organ.setDomain(domain);
                    organ.setInCharge(inChart);
                    organ.setAddress(address);
                    organ.setEmail(email);
                    organ.setTelephone(telephone);
                    organ.setStatus(true);
                    organ.setAgency(agency);
                    organ.setReceiveNotify(receivedNotify);
                    String newToken = TokenUtil.getRandomNumber(organ.getDomain(), organ.getName());
                    organ.setToken(newToken);
                    organ.setCreateDate(new Date());

                    EdocDynamicContactServiceUtil.createContact(organ);
                    message = messageSourceUtil.getMessage("organ.message.create.success", null);
                } else {
                    code = 400;
                    message = messageSourceUtil.getMessage("organ.message.create.fail", null);
                }
            }
            Response response = new Response(code, errors, message);
            return new ResponseEntity<>(response, HttpStatus.valueOf(code));
        } catch (Exception e) {
            errors.add(messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            Response response = new Response(500, errors, messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final Logger logger = Logger.getLogger(com.bkav.edoc.web.controller.DynamicRestContactController.class);
}