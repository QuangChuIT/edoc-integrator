package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.ExcelDailyCounterHeader;

import java.util.List;

public interface ExcelDailyCounterHeaderDao {
    List<ExcelDailyCounterHeader> getDailyCounterHeader();

    ExcelDailyCounterHeader getHeaderById(long id);
}
