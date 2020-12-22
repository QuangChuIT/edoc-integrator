package com.bkav.edoc.web.controller;

import com.bkav.edoc.web.util.MessageSourceUtil;
import com.bkav.edoc.web.util.ValidateUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationRestController {
    private final MessageSourceUtil messageSourceUtil;
    private final ValidateUtil validateUtil;

    public NotificationRestController(MessageSourceUtil messageSourceUtil, ValidateUtil validateUtil) {
        this.messageSourceUtil = messageSourceUtil;
        this.validateUtil = validateUtil;
    }

    private static final Logger logger = Logger.getLogger(com.bkav.edoc.web.controller.NotificationRestController.class);
}
