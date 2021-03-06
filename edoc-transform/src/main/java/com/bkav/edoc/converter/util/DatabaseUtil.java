package com.bkav.edoc.converter.util;

import com.bkav.edoc.service.database.entity.*;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DatabaseUtil {

    public static int getTotalDocument(Connection connection) {
        int total = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(StringQuery.COUNT_DOCUMENTS);
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return total;
    }

    public static List<EdocDocument> getFromDatabase(Connection connection, String checkDate) throws SQLException {
        List<EdocDocument> documents = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(StringQuery.GET_DOCUMENT);
            /*statement.setDate(1, java.sql.Date.valueOf(checkDate));*/
//            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                EdocDocument edocDocument = new EdocDocument();
                edocDocument.setDocumentId(resultSet.getLong(1));
                edocDocument.setEdXMLDocId(resultSet.getString(2));
                Date createDate = resultSet.getDate(3);
                Date sentDate = resultSet.getDate(14);
                if (createDate == null) {
                    createDate = sentDate;
                }
                edocDocument.setCreateDate(createDate);
                edocDocument.setSentDate(sentDate);

                edocDocument.setModifiedDate(resultSet.getDate(4));
                edocDocument.setSubject(resultSet.getString(5));
                String codeNumber = resultSet.getString(6);
                String codeNotation = resultSet.getString(7);
                edocDocument.setCodeNumber(codeNumber);
                edocDocument.setCodeNotation(codeNotation);
                if (codeNotation.contains("#")) {
                    codeNotation = codeNotation.substring(0, codeNotation.indexOf("#"));
                }
                String docCode = codeNumber + "/" + codeNotation;
                edocDocument.setDocCode(docCode);
                edocDocument.setPromulgationPlace(resultSet.getString(8));
                edocDocument.setPromulgationDate(resultSet.getDate(9));
                edocDocument.setDocumentTypeDetail(resultSet.getInt(10));
                edocDocument.setDocumentType(resultSet.getInt(10));
                edocDocument.setDocumentTypeName(resultSet.getString(11));
                edocDocument.setPriority(resultSet.getInt(12));
                edocDocument.setDraft(resultSet.getBoolean(13));
                edocDocument.setToOrganDomain(resultSet.getString(15));
                edocDocument.setFromOrganDomain(resultSet.getString(16));
                edocDocument.setVisited(false);
                edocDocument.setVisible(resultSet.getBoolean(17));
                documents.add(edocDocument);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
        return documents;
    }

    public static EdocDocumentDetail getEdocDocumentDetailByDocId(long documentId, Connection connection) {
        EdocDocumentDetail documentDetail = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(StringQuery.GET_DOCUMENT_DETAIL_BY_DOC_ID);
            preparedStatement.setLong(1, documentId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                documentDetail = new EdocDocumentDetail();
                documentDetail.setDocumentId(rs.getLong(1));
                documentDetail.setContent(rs.getString(2));
                documentDetail.setPromulgationAmount(rs.getInt(5));
                documentDetail.setPageAmount(rs.getInt(6));
                documentDetail.setToPlaces(rs.getString(12));
                documentDetail.setDueDate(null);
                documentDetail.setSignerCompetence(rs.getString(9));
                documentDetail.setSignerPosition(rs.getString(10));
                documentDetail.setSignerFullName(rs.getString(11));
                documentDetail.setSphereOfPromulgation(rs.getString(13));
                documentDetail.setTyperNotation(rs.getString(14));
                documentDetail.setAppendixes(rs.getString(15));
                documentDetail.setSteeringType(EdocDocumentDetail.SteeringType.STEER);
            }
            rs.close();
            preparedStatement.close();
        } catch (Exception e) {
            LOGGER.error("Error when get document detail by docId " + documentId + " cause " + e);
        }
        return documentDetail;
    }

    public static List<EdocAttachment> getAttachmentsByDocId(long documentId, Connection connection) {
        List<EdocAttachment> edocAttachments = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(StringQuery.GET_ATTACHMENT_BY_DOC_ID);
            statement.setLong(1, documentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                EdocAttachment edocAttachment = new EdocAttachment();
                edocAttachment.setAttachmentId(resultSet.getLong(1));
                edocAttachment.setOrganDomain(resultSet.getString(2));
                edocAttachment.setName(resultSet.getString(3));
                edocAttachment.setCreateDate(resultSet.getDate(4));
                edocAttachment.setFullPath(resultSet.getString(5));
                edocAttachment.setType(resultSet.getString(6));
                edocAttachment.setSize(resultSet.getString(7));
                edocAttachment.setToOrganDomain(resultSet.getString(8));
                edocAttachments.add(edocAttachment);
            }
        } catch (Exception e) {
            LOGGER.error("Error when get list attachment of document id " + documentId + " cause " + e);
        }
        return edocAttachments;
    }

    public static List<EdocNotification> getEdocNotificationsByDocId(long documentId, Connection connection) {
        List<EdocNotification> notifications = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(StringQuery.GET_NOTIFICATION_BY_DOC_ID);
            statement.setLong(1, documentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                EdocNotification edocNotification = new EdocNotification();
                edocNotification.setNotificationId(resultSet.getLong(1));
                edocNotification.setReceiverId(resultSet.getString(2));
                edocNotification.setSendNumber(resultSet.getInt(4));
                edocNotification.setDateCreate(resultSet.getDate(5));
                Date date = java.util.Calendar.getInstance().getTime();
                edocNotification.setModifiedDate(date);
                edocNotification.setDueDate(resultSet.getDate(7));
                edocNotification.setTaken(resultSet.getInt(8) == 0 && resultSet.getInt(9) == 0);
                notifications.add(edocNotification);
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            LOGGER.error("Error when get list notification of document id " + documentId + " cause " + e);
        }
        return notifications;
    }

    public static EdocTraceHeaderList getEdocTraceHeaderListByDocId(long documentId, Connection connection) {
        EdocTraceHeaderList edocTraceHeaderList = null;

        try {
            PreparedStatement statement = connection.prepareStatement(StringQuery.GET_TRACE_HEADER_LIST_BY_DOC_ID);
            statement.setLong(1, documentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                edocTraceHeaderList = new EdocTraceHeaderList();
                int businessDocType = resultSet.getInt(1);
                edocTraceHeaderList.setBusinessDocType(getBusinessDocType(businessDocType));
                edocTraceHeaderList.setBusinessDocReason(resultSet.getString(2));
                edocTraceHeaderList.setPaper(resultSet.getInt(3));
            }
        } catch (Exception e) {
            LOGGER.error("Error when get trace header list of document id " + documentId + " cause " + e);
        }

        return edocTraceHeaderList;
    }

    public static Set<EdocTraceHeader> getTraceHeaderByDocId(long oldDocumentId, Connection connection) {
        Set<EdocTraceHeader> traceHeaders = new HashSet<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(StringQuery.GET_TRACE_HEADER_BY_DOC_ID);
            preparedStatement.setLong(1, oldDocumentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EdocTraceHeader traceHeader = new EdocTraceHeader();
                traceHeader.setTraceHeaderId(oldDocumentId);
                traceHeader.setOrganDomain(resultSet.getString(1));
                traceHeader.setTimeStamp(resultSet.getTimestamp(2));
                traceHeaders.add(traceHeader);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return traceHeaders;
    }

    private static EdocTraceHeaderList.BusinessDocType getBusinessDocType(int type) {
        EdocTraceHeaderList.BusinessDocType result;
        switch (type) {
            case 0:
                result = EdocTraceHeaderList.BusinessDocType.NEW;
                break;
            case 1:
                result = EdocTraceHeaderList.BusinessDocType.REVOKE;
                break;
            case 2:
                result = EdocTraceHeaderList.BusinessDocType.UPDATE;
                break;
            case 3:
                result = EdocTraceHeaderList.BusinessDocType.REPLACE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return result;
    }

    public static List<EdocDocument> getDocumentByCounterDate(Connection connection, java.sql.Date _counterDate, String doc_code) {
        List<EdocDocument> documents = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(StringQuery.GET_DOCUMENT_BY_COUNTER_DATE);
            preparedStatement.setDate(1, _counterDate);
            preparedStatement.setString(2, doc_code);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EdocDocument edocDocument = new EdocDocument();
                edocDocument.setDocumentId(resultSet.getLong(1));
                edocDocument.setFromOrganDomain(resultSet.getString(2));
                edocDocument.setToOrganDomain(resultSet.getString(3));
                edocDocument.setSentDate(resultSet.getDate(4));
                documents.add(edocDocument);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
        return documents;
    }

    public static List<String> getDocCodeByCounterDate (Connection connection, java.sql.Date _counterDate) {
        List<String> docCode = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(StringQuery.GET_DOC_CODE_BY_COUNTER_DATE);
            preparedStatement.setDate(1, _counterDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String doc_code = resultSet.getString(1);
                docCode.add(doc_code);
            }
        } catch (SQLException throwables) {
            LOGGER.error(throwables);
        }
        return docCode;
    }

    public static boolean CheckSignedAttachment(Connection connection, long docId) {
        boolean result = false;
        int size = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(StringQuery.CHECK_SIGNED_ATTACHMENT);
            preparedStatement.setLong(1, docId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                size = resultSet.getInt(1);
            if (size > 0)
                result = true;
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
        return result;
    }

    public static int countSentExt(Connection connection, String domain, String _counterDate, boolean received_ext) {
        int size = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(StringQuery.COUNT_SENT_EXT_DOCUMENT);
            preparedStatement.setString(1, _counterDate);
            preparedStatement.setString(2, domain);
            preparedStatement.setBoolean(3, received_ext);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs != null) {
                rs.last();          // moves cursor to the last row
                size = rs.getRow(); // get row id
            }
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
        return size;
    }

    public static List<Long> getDocCodeByOrganDomain(Connection connection, String _counterDate, String domain) {
        List<Long> docCodes = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(StringQuery.GET_DOC_CODE_BY_DOMAIN);
            preparedStatement.setString(1, _counterDate);
            preparedStatement.setString(2, domain);
            ResultSet rs = preparedStatement.executeQuery();
            int size_ = rs.getFetchSize();
            while (rs.next()) {
                long doc_code = rs.getLong(1);
                docCodes.add(doc_code);
            }
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
        return docCodes;
    }

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class);
}
