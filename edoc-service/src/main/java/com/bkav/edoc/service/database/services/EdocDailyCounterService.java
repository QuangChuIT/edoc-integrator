package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDailyCounterDaoImpl;
import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.xml.base.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EdocDailyCounterService {
    private final EdocDailyCounterDaoImpl edocDailyCounterDao = new EdocDailyCounterDaoImpl();
    private final EdocDynamicContactService edocDynamicContactService = new EdocDynamicContactService();

    public boolean checkExistCounter(Date date) {
        edocDailyCounterDao.openCurrentSession();
        boolean result = edocDailyCounterDao.checkExistCounter(date);
        edocDailyCounterDao.closeCurrentSession();
        return result;
    }

    public void createDailyCounter(EdocDailyCounter dailyCounter) {
        edocDailyCounterDao.createDailyCounter(dailyCounter);
    }

    public List<EPublicStat> getStatsDetail(String organDomain, Date fromDate, Date toDate) {
        List<EPublicStat> ePublicStats = new ArrayList<>();
        List<OrganizationCacheEntry> contacts = edocDynamicContactService.getDynamicContactsByFilterDomain(organDomain);
        edocDailyCounterDao.openCurrentSession();
        for (OrganizationCacheEntry contact : contacts) {
            List<EdocDailyCounter> counters = new ArrayList<>();
            String organId = contact.getDomain();
            if (fromDate == null || toDate == null) {
                counters = edocDailyCounterDao.getOverStat(organId);
            } else {
                counters = edocDailyCounterDao.getOverStat(organId, fromDate, toDate);
            }

            EdocDailyCounter dailyCounter = getOverStat(counters);
            EPublicStat ePublicStat = new EPublicStat();
            ePublicStat.setLastUpdate(new Date());
            ePublicStat.setOrganDomain(organDomain);
            ePublicStat.setOrganName(contact.getName());
            ePublicStat.setSent(dailyCounter.getSent());
            ePublicStat.setReceived(dailyCounter.getReceived());
            long total = dailyCounter.getSent() + dailyCounter.getReceived();
            ePublicStat.setTotal(total);
            ePublicStats.add(ePublicStat);
        }
        edocDailyCounterDao.closeCurrentSession();
        return ePublicStats;
    }

    public EPublic getStat(String organDomain) {
        EPublic ePublic = new EPublic();
        edocDailyCounterDao.openCurrentSession();
        Long total = edocDailyCounterDao.getStat();
        edocDailyCounterDao.closeCurrentSession();
        ePublic.setTotal(total);
        ePublic.setTotalOrgan(edocDynamicContactService.countOrgan(organDomain));
        ePublic.setDateTime(DateUtils.format(new Date(), DateUtils.VN_DATETIME_FORMAT_NEW));
        return ePublic;
    }

    private EdocDailyCounter getOverStat(List<EdocDailyCounter> lstCounter) {
        EdocDailyCounter result = new EdocDailyCounter();
        int sumSentOfChild = 0;
        int sumReceivedOfChild = 0;
        for (int j = 0; j < lstCounter.size(); j++) {
            EdocDailyCounter tmpCounter = lstCounter.get(j);
            if (j == 0) {
                result.setOrganDomain(tmpCounter.getOrganDomain());
            }
            sumSentOfChild += tmpCounter.getSent();
            sumReceivedOfChild += tmpCounter.getReceived();
        }
        result.setSent(sumSentOfChild);
        result.setReceived(sumReceivedOfChild);
        return result;
    }
}
