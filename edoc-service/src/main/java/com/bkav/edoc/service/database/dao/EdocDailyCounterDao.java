package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocDailyCounter;

import java.util.Date;
import java.util.List;

public interface EdocDailyCounterDao {

    boolean checkExistCounter(Date date);

    List<EdocDailyCounter> getOverStat(String organDomain);

    List<EdocDailyCounter> getOverStat(String organDomain, Date fromDate, Date toDate);

    Long getStat();

}
