package com.bkav.edoc.converter.impl;

import com.bkav.edoc.converter.util.DBConnectionUtil;
import com.bkav.edoc.converter.util.DatabaseUtil;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.services.*;
import com.bkav.edoc.service.util.PropsUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EdocConvert {

    public static void main(String[] args) throws SQLException {

        Logger log = Logger.getLogger(EdocConvert.class);
        //
        EdocDocumentService edocDocumentService = new EdocDocumentService();
        EdocDocumentDetailService edocDocumentDetailService = new EdocDocumentDetailService();
        EdocAttachmentService edocAttachmentService = new EdocAttachmentService();
        EdocNotificationService edocNotificationService = new EdocNotificationService();
        EdocTraceHeaderListService edocTraceHeaderListService = new EdocTraceHeaderListService();

        log.info("---------------------- Get Document ----------------------------");

        Connection connection = DBConnectionUtil.initConvertDBConnection();

        String checkDate = "2020-09-25";

        List<EdocDocument> documents = DatabaseUtil.getFromDatabase(connection, checkDate);
        int count = 0;
        log.info("---------------------- Get Document Success with size " + documents.size() + " ----------------------------");
        for (EdocDocument document : documents) {
            try {
                LOGGER.info("Convert success number document " + count);
                long oldDocumentId = document.getDocumentId();

                EdocDocumentDetail documentDetail = DatabaseUtil.getEdocDocumentDetailByDocId(oldDocumentId, connection);

                List<EdocAttachment> attachments = DatabaseUtil.getAttachmentsByDocId(oldDocumentId, connection);

                List<EdocNotification> notifications = DatabaseUtil.getEdocNotificationsByDocId(oldDocumentId, connection);

                EdocTraceHeaderList traceHeaderList = DatabaseUtil.getEdocTraceHeaderListByDocId(oldDocumentId, connection);

                Set<EdocTraceHeader> traceHeaders = DatabaseUtil.getTraceHeaderByDocId(oldDocumentId, connection);
                if (attachments.size() > 0 && documentDetail != null && notifications.size() > 0 && traceHeaderList != null) {
                    LOGGER.info("Converting document with old document id " + oldDocumentId);
                    boolean isSentExt = checkSendToVPCP(document.getToOrganDomain());
                    boolean isReceiverExt = checkReceiverExt(document.getFromOrganDomain());
                    document.setSendExt(isSentExt);
                    document.setReceivedExt(isReceiverExt);
                    // add document
                    edocDocumentService.addDocument(document);
                    //add document detail
                    documentDetail.setDocument(document);
                    edocDocumentDetailService.addDocumentDetail(documentDetail);
                    //add attachment
                    for (EdocAttachment attachment : attachments) {
                        attachment.setDocument(document);
                        edocAttachmentService.addAttachment(attachment);
                    }
                    // add trace notification
                    for (EdocNotification notification : notifications) {
                        notification.setDocument(document);
                        edocNotificationService.addNotification(notification);
                    }
                    // add trace header list
                    traceHeaderList.setDocument(document);
                    edocTraceHeaderListService.createTraceHeaderList(traceHeaderList);

                    //add trace header
                    for (EdocTraceHeader traceHeader : traceHeaders) {
                        traceHeader.setTraceHeaderList(traceHeaderList);
                        edocTraceHeaderListService.createTraceHeader(traceHeader);
                    }
                    count ++;
                } else {
                    LOGGER.warn("Not convert document with old document id " + oldDocumentId + " cause another value reference null");
                }
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
        LOGGER.info("------------------------------------------ Convert document done --------------------------------------------");
        DBConnectionUtil.closeConnection(connection);
    }

    public static boolean checkSendToVPCP(String toOrganId) {
        boolean result = false;
        try {
            String organIdExcept = PropsUtil.get("");
            List<String> stringList = Arrays.asList(organIdExcept.split("#"));
            String[] toDomains = toOrganId.split("#");
            for (String toOrgan : toDomains) {
                String organId = toOrgan.substring(10, 13);
                if (!stringList.contains(organId)) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error when check send status to vpcp for organ domain " + toOrganId);
        }
        return result;
    }

    public static boolean checkReceiverExt(String fromOrganDomain) {
        boolean result = false;
        try {
            String organIdExcept = "";
            List<String> stringList = Arrays.asList(organIdExcept.split("#"));
            String organId = fromOrganDomain.substring(10, 13);
            if (!stringList.contains(organId)) {
                result = true;
            }
        } catch (Exception e) {
            LOGGER.error("Error when check send status to vpcp for organ domain " + fromOrganDomain);
        }
        return result;
    }

    private static final Logger LOGGER = Logger.getLogger(EdocConvert.class);
}
