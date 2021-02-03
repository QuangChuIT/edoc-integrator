package com.bkav.edoc.converter.impl;

import com.bkav.edoc.converter.util.DBConnectionUtil;
import com.bkav.edoc.converter.util.DatabaseUtil;
import com.bkav.edoc.converter.util.StringQuery;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DailyCounterConvert {
    private static final Logger LOGGER = Logger.getLogger(DailyCounterConvert.class);
    private Date _counterDate;

    public void runCounterStatDocument() throws SQLException {
        try (Connection connection = DBConnectionUtil.initConvertDBConnection()) {
            Statement stm;
            stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(StringQuery.GET_DATE);

            while (rs.next()) {
                Map<String, EdocDailyCounter> dailyCounterMap = new HashMap<>();
                _counterDate = rs.getDate(1);
                LOGGER.info("Starting counter document in date: " + _counterDate);
                List<String> docCodes = DatabaseUtil.getDocCodeByCounterDate(connection, _counterDate);
                for (String docCode: docCodes) {
                    List<EdocDocument> documents = DatabaseUtil.getDocumentByCounterDate(connection, _counterDate, docCode);
                    String fromOrgan = "";
                    for (EdocDocument document : documents) {
                        LOGGER.info("Start count with document id: " + document.getDocumentId());
                        fromOrgan = document.getFromOrganDomain();

                        String toOrgans = document.getToOrganDomain();
                        String[] toOrgansList = toOrgans.split("#");
                        for (String toOrgan : toOrgansList) {
                            if (checkCurrentOrgan(toOrgan)) {
                                countReceived(toOrgan, dailyCounterMap);
                            }
                        }
                    }
                    if (checkCurrentOrgan(fromOrgan)) {
                        countSent(fromOrgan, dailyCounterMap);
                    }
                }
                submitDatabase(dailyCounterMap);
                LOGGER.info("Counter document in date " + _counterDate + " successfully!!");
            }
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
    }

    public boolean checkCurrentOrgan(String organDomain) {
        boolean result = false;
        try {
            EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(organDomain);
            if (edocDynamicContact != null) {
                result = edocDynamicContact.getAgency();
            }
        } catch (Exception e) {
            LOGGER.error("Error check organ to stat cause " + e);
        }
        return result;
    }

    private void submitDatabase(Map<String, EdocDailyCounter> dailyCounterMap) {
        for (Map.Entry<String, EdocDailyCounter> entry : dailyCounterMap.entrySet()) {
            EdocDailyCounter dailyCounter = entry.getValue();
            EdocDailyCounterServiceUtil.createDailyCounter(dailyCounter);
        }
    }

    private void countSent(String organDomain, Map<String, EdocDailyCounter> dailyCounterMap) {
        EdocDailyCounter dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int sent = dailyCounter.getSent() + 1;
            dailyCounter.setSent(sent);
        } else {
            dailyCounter = new EdocDailyCounter();
            dailyCounter.setSent(1);
            dailyCounter.setDateTime(_counterDate);
            dailyCounter.setReceived(0);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    private void countReceived(String organDomain, Map<String, EdocDailyCounter> dailyCounterMap) {
        EdocDailyCounter dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int received = dailyCounter.getReceived() + 1;
            dailyCounter.setReceived(received);
        } else {
            dailyCounter = new EdocDailyCounter();
            dailyCounter.setSent(0);
            dailyCounter.setDateTime(_counterDate);
            dailyCounter.setReceived(1);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    /*private void countSentExt(String organDomain, Map<String, EdocStatDetail> dailyCounterMap) {
        EdocStatDetail dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int sent = dailyCounter.getSent_ext() + 1;
            dailyCounter.setSent_ext(sent);
        } else {
            dailyCounter = new EdocStatDetail();
            dailyCounter.setSent_ext(1);
            dailyCounter.setSent_int(0);
            dailyCounter.setSigned(0);
            dailyCounter.setNot_signed(0);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    private void countSentInt(String organDomain, Map<String, EdocStatDetail> dailyCounterMap) {
        EdocStatDetail dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int sent = dailyCounter.getSent_int() + 1;
            dailyCounter.setSent_int(sent);
        } else {
            dailyCounter = new EdocStatDetail();
            dailyCounter.setSent_int(1);
            dailyCounter.setSent_ext(0);
            dailyCounter.setSigned(0);
            dailyCounter.setNot_signed(0);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    private void countSigned(String organDomain, Map<String, EdocStatDetail> dailyCounterMap) {
        EdocStatDetail dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int signed = dailyCounter.getSigned() + 1;
            dailyCounter.setSigned(signed);
        } else {
            dailyCounter = new EdocStatDetail();
            dailyCounter.setNot_signed(0);
            dailyCounter.setSent_int(0);
            dailyCounter.setSent_ext(0);
            dailyCounter.setSigned(1);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    private void countNotSigned(String organDomain, Map<String, EdocStatDetail> dailyCounterMap) {
        EdocStatDetail dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
            int not_signed = dailyCounter.getNot_signed() + 1;
            dailyCounter.setNot_signed(not_signed);
        } else {
            dailyCounter = new EdocStatDetail();
            dailyCounter.setSigned(0);
            dailyCounter.setSent_ext(0);
            dailyCounter.setSent_int(0);
            dailyCounter.setNot_signed(1);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

    private void countNone(String organDomain, Map<String, EdocStatDetail> dailyCounterMap) {
        EdocStatDetail dailyCounter;
        if (dailyCounterMap.containsKey(organDomain)) {
            dailyCounter = dailyCounterMap.get(organDomain);
            dailyCounterMap.remove(organDomain);
        } else {
            dailyCounter = new EdocStatDetail();
            dailyCounter.setSigned(0);
            dailyCounter.setSent_ext(0);
            dailyCounter.setSent_int(0);
            dailyCounter.setNot_signed(0);
            dailyCounter.setOrganDomain(organDomain);
        }
        dailyCounterMap.put(organDomain, dailyCounter);
    }

     */

    /*public Map<String, EdocStatDetail> runCounterStatDocumentForTayNinh(Connection connection) {
        Map<String, EdocStatDetail> dailyCounterMap = new HashMap<>();

        String counterDate =  "2020-12-31";

        List<EdocDynamicContact> organs = EdocDynamicContactServiceUtil.getDynamicContactByAgency(true);

        for (EdocDynamicContact organ: organs) {
            String organDomain = organ.getDomain();
            LOGGER.info("Start count with organ " + organDomain + "!!!!!!!!!!");
            EdocStatDetail edocStatDetail = new EdocStatDetail();

            int received_ext = DatabaseUtil.countSentExt(connection, organDomain, counterDate, true);
            edocStatDetail.setOrganDomain(organDomain);
            edocStatDetail.setReceived_ext(received_ext);

            int received_int = DatabaseUtil.countSentExt(connection, organDomain, counterDate, false);
            edocStatDetail.setReceived_int(received_int);
            int total = received_int + received_ext;
            edocStatDetail.setTotal(total);
            LOGGER.info("Total received: " + total);

            List<Long> listDocCode = DatabaseUtil.getDocCodeByOrganDomain(connection, counterDate, organDomain);
            LOGGER.info("Has " + listDocCode.size() + " documents!!!!");
            for (long doc_code: listDocCode) {
                //List<Long> docIds = DatabaseUtil.getDocumentIdByDocCode(connection, counterDate, doc_code);
                //for (long docId: docIds) {
                    LOGGER.info("Start at documentID " + doc_code);
                    if (DatabaseUtil.CheckSignedAttachment(connection, doc_code)) {
                        int signed = edocStatDetail.getSigned() + 1;
                        edocStatDetail.setSigned(signed);
                    } else {
                        int not_signed = edocStatDetail.getNot_signed() + 1;
                        edocStatDetail.setNot_signed(not_signed);
                    }
                //}
            }

            dailyCounterMap.put(organDomain, edocStatDetail);
            LOGGER.info("End count organ domain " + organDomain);
        }

//        for (EdocDocument document : documents) {
//            LOGGER.info("Staring with document id: " + document.getDocumentId());
//
//            String fromOrgan = document.getFromOrganDomain();
//            if (checkCurrentOrgan(fromOrgan)) {
//                boolean sent_ext = document.getSendExt();
//                if (sent_ext) {
//                    countSentExt(fromOrgan, dailyCounterMap);
//                } else {
//                    countSentInt(fromOrgan, dailyCounterMap);
//                }
//                LOGGER.info("Starting counting signed document!!!!!!!");
//                if (DatabaseUtil.CheckSignedAttachment(connection, document.getDocumentId())) {
//                    countSigned(fromOrgan, dailyCounterMap);
//                } else {
//                    countNotSigned(fromOrgan, dailyCounterMap);
//                }
//            }

//            String toOrgans = document.getToOrganDomain();
//            String[] toOrgansList = toOrgans.split("#");
//            for (String toOrgan : toOrgansList) {
//                if (checkCurrentOrgan(toOrgan)) {
//                    boolean received_ext = document.getReceivedExt();
//                    if(received_ext) {
//                        countReceivedExt(toOrgan, dailyCounterMap);
//                    } else {
//                        countReceivedInt(toOrgan, dailyCounterMap);
//                    }
//
//                    //LOGGER.info("Starting counting signed document!!!!!!!");
//                    if (DatabaseUtil.CheckSignedAttachment(connection, document.getDocumentId())) {
//                        countSigned(toOrgan, dailyCounterMap);
//                    } else {
//                        countNotSigned(toOrgan, dailyCounterMap);
//                    }
//                }
//            }
//        }
        return dailyCounterMap;
    }

     */

    /*public static void main(String[] args) throws IOException {
        System.out.println("Processing...");
        long startTime = System.currentTimeMillis();
        int totalSent = 0;
        DailyCounterConvert dailyCounterConverter = new DailyCounterConvert();
        Connection connection = DBConnectionUtil.initConvertDBConnection();
        Map<String, EdocStatDetail> dailyCounterMap = dailyCounterConverter.runCounterStatDocumentForTayNinh(connection);
        List<EdocStatDetail> edocStatDetails = new ArrayList<>();
        for (Map.Entry<String, EdocStatDetail> entry : dailyCounterMap.entrySet()) {
            EdocStatDetail dailyStat = entry.getValue();
            edocStatDetails.add(dailyStat);
        }

        LOGGER.info("Has " + edocStatDetails.size() + " organs!!!!!!!!!!");
        LOGGER.info("Starting write data to excel!!!!!!!!!!!!!!!!!!!!!!!!");
        ExcelUtil.exportStatDetailForTayNinh(edocStatDetails);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Run time: " + (endTime-startTime)/60000 + " minutes");
        LOGGER.info("Done!!!!!!!!!!!!!!");
    }
    */

    public static void main(String[] args) throws SQLException {
        System.out.println("Processing...");
        long startTime = System.currentTimeMillis();

        DailyCounterConvert dailyCounterConverter = new DailyCounterConvert();
        dailyCounterConverter.runCounterStatDocument();

        long endTime = System.currentTimeMillis();
        LOGGER.info("Run time: " + (endTime-startTime)/60000.0 + " minutes");
        LOGGER.info("Done!!!!!!!!!!!!!!");
    }
}
