package com.bkav.edoc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublicStatController {

    @RequestMapping(value = "/public/stat", method = RequestMethod.GET)
    public String publicStat() {
        return "publicReport";
    }

    @RequestMapping(value = "/public/stat/detail", method = RequestMethod.GET)
    public String publicStatDetail() {
        return "publicReportDetail";
    }

    @RequestMapping(value = "/public/trace", method = RequestMethod.GET)
    public String publicTrace() {
        return "publicTracePage";
    }

    @RequestMapping(value = "/public/dailycounter/convert", method = RequestMethod.GET)
    public String publicDailycounterConvert() { return "dailycounterConvert";}
}
