package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.MailReceiverAdmin;
import com.bkav.edoc.service.database.services.MailReceiverAdminService;

import java.util.List;

public class MailReceiverAdminServiceUtil {
    private static final MailReceiverAdminService MAIL_RECEIVER_SERVICE = new MailReceiverAdminService();

    public static List<MailReceiverAdmin> getAllMailReceiver() {
        return MAIL_RECEIVER_SERVICE.getAllMailReceiver();
    }
}
