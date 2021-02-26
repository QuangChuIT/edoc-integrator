package com.bkav.edoc.web.controller;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @RequestMapping(value = "/api/logout", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void logoutFromSSO(@RequestBody String data) {
        LOGGER.info("Back channel logout request received.");
        LOGGER.info("Logout Token " + data);
    }

    private static final Logger LOGGER = Logger.getLogger(LogoutController.class);
}
