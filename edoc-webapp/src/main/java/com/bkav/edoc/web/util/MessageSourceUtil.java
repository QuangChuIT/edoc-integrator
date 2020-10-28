package com.bkav.edoc.web.util;

import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageSourceUtil {

    private final Locale locale = new Locale("vi", "VN");

    private final MessageSource messageSource;

    public MessageSourceUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public String getMessage(String key, @Nullable Object[] args) {
        return messageSource.getMessage(key, args, locale);
    }
}
