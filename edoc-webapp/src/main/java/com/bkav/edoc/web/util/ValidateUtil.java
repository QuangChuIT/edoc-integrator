package com.bkav.edoc.web.util;

import com.bkav.edoc.web.payload.DocumentRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidateUtil {

    private final MessageSourceUtil messageSourceUtil;

    public ValidateUtil(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }

    public List<String> validate(DocumentRequest documentRequest) {
        List<String> errors = new ArrayList<>();
        // validate document request
        if (documentRequest.getFromOrgan().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.from.organ", null));
        }

        if (documentRequest.getToOrganDomain() == null || documentRequest.getToOrganDomain().size() == 0) {
            errors.add(messageSourceUtil.getMessage("edoc.error.to.organ", null));
        }

        if (documentRequest.getCodeNumber().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.code.number", null));
        }

        if (documentRequest.getCodeNation().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.code.nation", null));
        }

        if (documentRequest.getPromulgationDate().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.promulgation.date", null));
        }

        if (documentRequest.getStaffName().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.staff.name", null));
        }

        if (documentRequest.getSignerFullName().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.signer.info", null));
        }

        if (documentRequest.getAttachmentIds() == null || documentRequest.getAttachmentIds().size() == 0) {
            errors.add(messageSourceUtil.getMessage("edoc.error.attachment", null));
        }

        return errors;
    }
}
