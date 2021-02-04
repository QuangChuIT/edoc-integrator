package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("statDocumentBean")
public class StatDocumentBean {

    public void runSchedulerStatDocument() {
        try {
            Calendar yesterday = Calendar.getInstance();
            Map<String, EdocDailyCounter> dailyCounterMap = new HashMap<>();
            yesterday.add(Calendar.DATE, -1);
            //yesterday.add(Calendar.HOUR, 7);
            _counterDate = yesterday.getTime();
            LOGGER.info("Counter date prepare stat " + _counterDate);
            if (!EdocDailyCounterServiceUtil.checkExistCounter(_counterDate)) {
                LOGGER.info("Prepare stat document with counter date " + _counterDate);
//                List<String> docCodes = EdocDocumentServiceUtil.getDocCodeByCounterDate(_counterDate);
//                for (String docCode : docCodes) {
//                    List<EdocDocument> documents = EdocDocumentServiceUtil.selectForDailyCounter(_counterDate);
//                }
                List<EdocDocument> documents = EdocDocumentServiceUtil.selectForDailyCounter(_counterDate);
                LOGGER.info("List document to stat " + documents.size());
                for (EdocDocument document : documents) {
                    String fromOrgan = document.getFromOrganDomain();
                    if (checkOrganToStat(fromOrgan)) {
                        countSent(fromOrgan, dailyCounterMap);
                    }

                    String toOrgans = document.getToOrganDomain();
                    String[] toOrgansList = toOrgans.split("#");
                    for (String toOrgan : toOrgansList) {
                        if (checkOrganToStat(toOrgan)) {
                            countReceived(toOrgan, dailyCounterMap);
                        }
                    }
                }
                submitDatabase(dailyCounterMap);
            }
        } catch (Exception e) {
            LOGGER.error("Error when run Scheduler Stat Document " + e);
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

    private boolean checkOrganToStat(String organId) {
        boolean result = false;
        try {
            EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(organId);
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
            LOGGER.info("Submit database success for organ domain " + dailyCounter.getOrganDomain());
        }
    }

    private Date _counterDate;
    private final static Logger LOGGER = Logger.getLogger(StatDocumentBean.class);
}
