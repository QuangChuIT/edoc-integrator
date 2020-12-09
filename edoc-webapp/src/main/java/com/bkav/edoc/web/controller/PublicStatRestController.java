package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class PublicStatRestController {

    @GetMapping(value = "/public/-/stat/detail", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<EPublicStat> getStatDetail(@RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate) {
        String organDomainCode = PropsUtil.get("edoc.root.organDomain");
        if (fromDate == null || toDate == null) {
            return EdocDailyCounterServiceUtil.getStatsDetail(organDomainCode, null, null);
        } else {
            Date fromDateValue = DateUtils.parse(fromDate);
            Date toDateValue = DateUtils.parse(toDate);
            return EdocDailyCounterServiceUtil.getStatsDetail(organDomainCode, fromDateValue, toDateValue);
        }
    }

    @GetMapping(value = "/public/-/document/stat", produces = {MediaType.APPLICATION_JSON_VALUE})
    public EPublic getStat() {
        String organDomainCode = PropsUtil.get("edoc.root.organDomain");
        return EdocDailyCounterServiceUtil.getStat(organDomainCode);
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

    private static final Logger LOGGER = Logger.getLogger(PublicStatRestController.class);
}
