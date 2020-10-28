package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.PropsUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class PublicStatRestController {

    @GetMapping(value = "/public/-/stat/detail", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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


}
