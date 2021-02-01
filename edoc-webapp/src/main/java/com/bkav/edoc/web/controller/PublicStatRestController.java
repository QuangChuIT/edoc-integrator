package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.entity.EdocStatisticDetail;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.UserServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.ExcelUtil;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

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
    public ResponseEntity<String> getSentReceivedForChart(@RequestParam(value = "year", required = false) String year_str,
                                                          @RequestParam(value = "userId", required = false) String userId) {
        String results;
        int year = Integer.parseInt(year_str);
        Long id = new Long(userId);
        User user = UserServiceUtil.findUserById(id);
        if (user == null)
            results = EdocDailyCounterServiceUtil.getSentReceivedForChart(year, "");
        else {
            if (user.getUsername().equals(PropsUtil.get("user.admin.username"))) {
                results = EdocDailyCounterServiceUtil.getSentReceivedForChart(year, "");
            } else {
                results = EdocDailyCounterServiceUtil.getSentReceivedForChart(year, user.getDynamicContact().getDomain());
            }
        }
        if (results.length() > 0)
            return new ResponseEntity<>(results, HttpStatus.OK);
        return new ResponseEntity<>(results, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/public/-/statistic/detail", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<EdocStatisticDetail> getStatisticDetail(@RequestParam(value = "fromDate", required = false) String fromDate,
                                                        @RequestParam(value = "toDate", required = false) String toDate,
                                                        @RequestParam(value = "organDomain", required = false) String organDomain) {

        if (fromDate == null || toDate == null || organDomain == null) {
            return EdocDailyCounterServiceUtil.getStatisticDetail(null, null, null);
        } else {
            return EdocDailyCounterServiceUtil.getStatisticDetail(fromDate, toDate, organDomain);
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

    @RequestMapping(value = "/public/-/dailycounter/converter", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public HttpStatus dailycounterConvert(@RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate) {
        if (fromDate != null && toDate != null) {
            Date fromDateValue = DateUtils.parse(fromDate);
            Date toDateValue = DateUtils.parse(toDate);
            EdocDocumentServiceUtil.getDailycounterDocument(fromDateValue, toDateValue);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/public/-/stat/export/excel", method = RequestMethod.GET)
    public void exportToExcel(HttpServletResponse response, @RequestParam(value = "fromDate", required = false) String fromDate,
                              @RequestParam(value = "toDate", required = false) String toDate) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        if (fromDate == null || toDate == null) {
            String headerValue = "attachment; filename=ThongKeVanBan.xlsx";
            response.setHeader(headerKey, headerValue);
            ExcelUtil.exportExcelDailyCounter(response,null, null);
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
