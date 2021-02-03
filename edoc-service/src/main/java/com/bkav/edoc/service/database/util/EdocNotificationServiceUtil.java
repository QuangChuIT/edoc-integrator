package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.entity.TelegramMessage;
import com.bkav.edoc.service.database.services.EdocNotificationService;

import java.util.Date;
import java.util.List;

public class EdocNotificationServiceUtil {

    private final static EdocNotificationService NOTIFICATION_SERVICE = new EdocNotificationService();

    public static void addNotification(EdocNotification edocNotification) {
        NOTIFICATION_SERVICE.addNotification(edocNotification);
    }

    public static List<EmailRequest> emailScheduleSend(Date fromDate, Date toDate) {
        return NOTIFICATION_SERVICE.getEmailRequestScheduleSend(fromDate, toDate);
    }

    public static List<TelegramMessage> telegramScheduleSend(Date date) {
        return NOTIFICATION_SERVICE.getTelegramMessages(date);
    }

    public static boolean checkExistNotification(String organ, long docId) {
        return NOTIFICATION_SERVICE.checkExistNotification(organ, docId);
    }
    public static List<Long> getDocumentIdsByOrganId(String organId) {
        return NOTIFICATION_SERVICE.getDocumentIdsByOrganId(organId);
    }

    public static void removePendingDocId(String organId, long docId) {
        NOTIFICATION_SERVICE.removePendingDocId(organId, docId);
    }

}
