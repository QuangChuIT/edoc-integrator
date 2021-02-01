package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.EdocAttachmentServiceUtil;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.ExcelUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PublicStatRestController {

    @RequestMapping(value = "/public/-/stat/detail", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<EPublicStat> getStatDetail(@RequestParam(value = "fromDate", required = false) String fromDate,
                                           @RequestParam(value = "toDate", required = false) String toDate) {
        if (fromDate == null || toDate == null) {
            return EdocDailyCounterServiceUtil.getStatsDetail(null, null);
        } else {
            Date fromDateValue = DateUtils.parse(fromDate);
            Date toDateValue = DateUtils.parse(toDate);
            return EdocDailyCounterServiceUtil.getStatsDetail(fromDateValue, toDateValue);
        }
    }

    @GetMapping(value = "/public/-/document/stat", produces = {MediaType.APPLICATION_JSON_VALUE})
    public EPublic getStat() {
        return EdocDailyCounterServiceUtil.getStat();
    }

    @RequestMapping(value = "/public/-/statistic/chart", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getSentReceivedForChart(@RequestParam(value = "year") int year,
                                                          @RequestParam(value = "organDomain", required = false) String organDomain) {
        String results = EdocDailyCounterServiceUtil.getSentReceivedForChart(year, organDomain);
        try {
            if (results.length() > 0)
                return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(results, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/public/-/document/attachments")
    public ResponseEntity<List<Long>> getAttachmentIds(@RequestParam("docCode") String docCode) {
        if (docCode == null || docCode.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            EdocDocument document = EdocDocumentServiceUtil.getDocumentByCode(docCode);
            if (document == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Set<EdocAttachment> attachments = document.getAttachments();
                if (attachments.size() > 0) {
                    List<Long> attachmentIds = attachments.stream().map(EdocAttachment::getAttachmentId).collect(Collectors.toList());
                    return new ResponseEntity<>(attachmentIds, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.ACCEPTED);
                }
            }
        }
    }

    @RequestMapping(value = "/public/-/document/attachment/{attachmentId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadAttachment(@PathVariable Long attachmentId, HttpServletRequest request) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        try {
            if (attachmentId == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                EdocAttachment attachment = EdocAttachmentServiceUtil.findById(attachmentId);

                if (attachment == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                String specPath = attachment.getFullPath();
                LOGGER.info("Spec Path " + specPath);

                String fullPath = PropsUtil.get("edxml.attachment.dir");

                String filePath = fullPath + "/attachment/" + specPath;

                LOGGER.info("File Path " + filePath);

                File file = new File(filePath);

                byte[] data = FileUtils.readFileToByteArray(file);

                String filename = URLEncoder.encode(attachment.getName(),
                        StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
                // Set mimeType response
                responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                // Config information file response
                responseHeader.set("Content-disposition", "attachment; filename=" + filename);
                responseHeader.setContentLength(data.length);
                InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
                InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
            }
        } catch (
                Exception e) {
            LOGGER.error("Error process download file with attachmentId " + attachmentId + " cause " + e.getMessage());
            return new ResponseEntity<>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/public/-/document/trace",
            method = RequestMethod.GET, //
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<DocumentCacheEntry> getDocument(@RequestParam String docCode, @RequestParam String organ) {
        try {
            DocumentCacheEntry document = EdocDocumentServiceUtil.getDocumentByCodeAndDomain(docCode, organ);
            if (document != null) {
                return new ResponseEntity<>(document, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/public/-/stat/export/excel", method = RequestMethod.GET)
    public void exportToExcel(HttpServletResponse response, @RequestParam(value = "fromDate", required = false) String fromDate,
                              @RequestParam(value = "toDate", required = false) String toDate) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        if (fromDate == null || toDate == null) {
            String headerValue = "attachment; filename=ThongKeVanBan.xlsx";
            response.setHeader(headerKey, headerValue);
            ExcelUtil.exportExcelDailyCounter(response, null, null);
        } else {
            Date fromDateValue = DateUtils.parse(fromDate);
            Date toDateValue = DateUtils.parse(toDate);
            String headerValue = "attachment; filename=ThongKeVanBan_" + DateUtils.format(fromDateValue, DateUtils.VN_DATE_FORMAT_D)
                    + "-" + DateUtils.format(toDateValue, DateUtils.VN_DATE_FORMAT_D) + ".xlsx";
            response.setHeader(headerKey, headerValue);
            ExcelUtil.exportExcelDailyCounter(response, fromDateValue, toDateValue);
        }
    }


    private static final Logger LOGGER = Logger.getLogger(PublicStatRestController.class);
}
