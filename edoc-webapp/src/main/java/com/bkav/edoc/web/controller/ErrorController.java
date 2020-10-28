package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.web.auth.CookieUtil;
import com.bkav.edoc.web.util.PropsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorController {

    @RequestMapping(value = "/errors", method = RequestMethod.GET)
    public String renderErrorPage(HttpServletRequest httpRequest, HttpServletResponse response) {
        String host = GetterUtil.getString(PropsUtil.get("edoc.server.host"), "");
        CookieUtil.clearAllCookies(httpRequest, response, host);
        return "errorPage";
    }
}
