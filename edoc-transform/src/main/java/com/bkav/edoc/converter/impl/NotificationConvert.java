package com.bkav.edoc.converter.impl;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NotificationConvert {
    private static final Logger LOGGER = Logger.getLogger(NotificationConvert.class);

    public void getNotification() throws SQLException, ParseException {

        String strDate = "2021-01-25";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(strDate);

        int i = 0;

        List<EdocDocument> documents = EdocDocumentServiceUtil.getDocumentByDate(date);
        LOGGER.info("Has " + documents.size() + " documents!!!!!!");
        for (EdocDocument document: documents) {
            long docId = document.getDocumentId();
            LOGGER.info("Check with document id: " + docId);
            String organs = document.getToOrganDomain();
            List<String> toOrgans = Arrays.asList(organs.split("#"));

            for (String domain: toOrgans) {
                if (!EdocNotificationServiceUtil.checkExistNotification(domain, docId)) {
                    EdocNotification edocNotification = new EdocNotification();
                    edocNotification.setReceiverId(domain);
                    edocNotification.setDateCreate(document.getCreateDate());
                    edocNotification.setModifiedDate(document.getModifiedDate());
                    edocNotification.setDocument(document);
                    edocNotification.setTaken(false);
                    //EdocNotificationServiceUtil.addNotification(edocNotification);
                    i++;
                    LOGGER.info("Created notification with organ domain: " + domain);
                }
            }
        }
        LOGGER.info("Has " + i + " new notification!!!!!!!!!!!");
    }

    public static void main(String[] args) throws SQLException, ParseException {
        NotificationConvert notificationConvert = new NotificationConvert();
        notificationConvert.getNotification();
    }
}
