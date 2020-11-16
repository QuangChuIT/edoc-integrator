package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.EdocDailyCounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("statDocumentBean")
public class StatDocumentBean {

    public void runSchedulerStatDocument() {
        try {
            Calendar yesterday = Calendar.getInstance();
            Map<String, EdocDailyCounter> dailyCounterMap = new HashMap<>();
            //yesterday.add(Calendar.DATE, -1);
            //yesterday.add(Calendar.HOUR, 7);
            _counterDate = yesterday.getTime();
            if (!EdocDailyCounterServiceUtil.checkExistCounter(_counterDate)) {
                List<EdocDocument> documents = EdocDocumentServiceUtil.selectForDailyCounter(_counterDate);
                for (EdocDocument document : documents) {
                    String fromOrgan = document.getFromOrganDomain();
                    countSent(fromOrgan, dailyCounterMap);
                    String toOrgans = document.getToOrganDomain();
                    String[] toOrgansList = toOrgans.split("#");
                    for (String toOrgan : toOrgansList) {
                        countReceived(toOrgan, dailyCounterMap);
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

    private void submitDatabase(Map<String, EdocDailyCounter> dailyCounterMap) {
        for (Map.Entry<String, EdocDailyCounter> entry : dailyCounterMap.entrySet()) {
            EdocDailyCounter dailyCounter = entry.getValue();
            EdocDailyCounterServiceUtil.createDailyCounter(dailyCounter);
        }
    }

    private Date _counterDate;
    private final static Logger LOGGER = Logger.getLogger(StatDocumentBean.class);
}