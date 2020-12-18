package com.bkav.edoc.web.util;

import com.bkav.edoc.web.payload.ImportExcelError;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

    public List<String> convertToMessage(List<ImportExcelError> importExcelErrors) {
        List<String> errors = new ArrayList<>();
        for(ImportExcelError importExcelError : importExcelErrors){
            String message = this.getMessage(importExcelError.getMessageKey(), new Object[]{importExcelError.getRow(), importExcelError.getCol()});
            errors.add(message);
        }
        return errors;
    }
}
