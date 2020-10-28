package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.database.services.EdocDailyCounterService;

import java.util.Date;
import java.util.List;

public class EdocDailyCounterServiceUtil {
    private final static EdocDailyCounterService EDOC_DAILY_COUNTER_SERVICE = new EdocDailyCounterService();

    public static boolean checkExistCounter(Date date) {
        return EDOC_DAILY_COUNTER_SERVICE.checkExistCounter(date);
    }

    public static void createDailyCounter(EdocDailyCounter dailyCounter) {
        EDOC_DAILY_COUNTER_SERVICE.createDailyCounter(dailyCounter);
    }

    public static List<EPublicStat> getStatsDetail(String organDomain, Date fromDate, Date toDate) {
        return EDOC_DAILY_COUNTER_SERVICE.getStatsDetail(organDomain, fromDate, toDate);
    }

    public static EPublic getStat(String organDomain) {
        return EDOC_DAILY_COUNTER_SERVICE.getStat(organDomain);
    }
}
