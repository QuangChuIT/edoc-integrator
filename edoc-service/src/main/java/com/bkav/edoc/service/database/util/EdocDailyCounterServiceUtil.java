package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EPublic;
import com.bkav.edoc.service.database.entity.EPublicStat;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import com.bkav.edoc.service.database.entity.EdocStatisticDetail;
import com.bkav.edoc.service.database.services.EdocDailyCounterService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EdocDailyCounterServiceUtil {
    private final static EdocDailyCounterService EDOC_DAILY_COUNTER_SERVICE = new EdocDailyCounterService();

    public static boolean checkExistCounter(Date date) {
        return EDOC_DAILY_COUNTER_SERVICE.checkExistCounter(date);
    }

    public static void createDailyCounter(EdocDailyCounter dailyCounter) {
        EDOC_DAILY_COUNTER_SERVICE.createDailyCounter(dailyCounter);
    }

    public static List<EPublicStat> getStatsDetail(Date fromDate, Date toDate) {
        return EDOC_DAILY_COUNTER_SERVICE.getStatsDetail(fromDate, toDate);
    }

    public static EPublic getStat() {
        return EDOC_DAILY_COUNTER_SERVICE.getStat();
    }

    public static String getSentReceivedForChart(int year, String organDomain) {
        return EDOC_DAILY_COUNTER_SERVICE.getSentReceivedForChart(year, organDomain);
    }

    public static List<EdocStatisticDetail> getStatisticDetail (String fromDate, String toDate, String organDomain) {
        return EDOC_DAILY_COUNTER_SERVICE.getStatisticSentReceivedExtDetail(fromDate, toDate, organDomain);
    }
}
