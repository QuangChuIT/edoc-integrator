package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.entity.TelegramMessage;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.services.EdocNotificationService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EdocNotificationServiceUtil {

    private final static EdocNotificationService NOTIFICATION_SERVICE = new EdocNotificationService();

    public static void addNotification(EdocNotification edocNotification) {
        NOTIFICATION_SERVICE.addNotification(edocNotification);
    }

    public static List<EmailRequest> emailScheduleSend(Date fromDate, Date toDate) {
        return NOTIFICATION_SERVICE.getEmailRequestScheduleSend(fromDate, toDate);
    }

    public static List<TelegramMessage> telegramScheduleSend() {
        return NOTIFICATION_SERVICE.getTelegramMessages();
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

    /*public static Map<String, Object> getAllDocumentNotTaken(PaginationCriteria paginationCriteria, Date fromDate, Date toDate) {
        return NOTIFICATION_SERVICE.getAllDocumentNotTaken(paginationCriteria, fromDate, toDate);
    }*/

    public static Map<String, Object> getAllDocumentNotTaken(PaginationCriteria paginationCriteria) {
        return NOTIFICATION_SERVICE.getAllDocumentNotTaken(paginationCriteria);
    }
}
