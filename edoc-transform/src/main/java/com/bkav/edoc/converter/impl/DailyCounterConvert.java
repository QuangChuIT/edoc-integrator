package com.bkav.edoc.converter.impl;

import com.bkav.edoc.converter.util.DBConnectionUtil;
import com.bkav.edoc.converter.util.DatabaseUtil;
import com.bkav.edoc.converter.util.StringQuery;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
                docCodes.forEach(docCode -> {
                    List<EdocDocument> documents = DatabaseUtil.getDocumentByCounterDate(connection, _counterDate, docCode);
                    AtomicReference<String> fromOrgan = new AtomicReference<>("");
                    documents.forEach(document -> {
                        fromOrgan.set(document.getFromOrganDomain());

                        String toOrgans = document.getToOrganDomain();
                        List<String> toOrganList = Arrays.asList(toOrgans.split("#"));
                        toOrganList.stream().filter(toOrgan -> checkCurrentOrgan(toOrgan))
                                .forEach(toOrgan -> countReceived(toOrgan, dailyCounterMap));
                    });
                    if (checkCurrentOrgan(fromOrgan.get()))
                        countSent(fromOrgan.get(), dailyCounterMap);
                });
                /*for (String docCode: docCodes) {
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
                }*/
                //System.out.println(new Gson().toJson(dailyCounterMap));
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
