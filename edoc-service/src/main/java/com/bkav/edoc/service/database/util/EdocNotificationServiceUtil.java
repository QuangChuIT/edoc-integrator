package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.services.EdocNotificationService;

import java.util.List;

public class EdocNotificationServiceUtil {

    private final static EdocNotificationService NOTIFICATION_SERVICE = new EdocNotificationService();

    public static void addNotification(EdocNotification edocNotification) {
        NOTIFICATION_SERVICE.addNotification(edocNotification);
    }

    public static List<EmailRequest> emailScheduleSend() {
        return NOTIFICATION_SERVICE.getEmailRequestScheduleSend();
    }
}
