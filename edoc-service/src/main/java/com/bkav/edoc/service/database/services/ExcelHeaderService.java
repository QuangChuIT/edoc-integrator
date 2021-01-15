package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.ExcelDailyCounterHeaderDaoImpl;
import com.bkav.edoc.service.database.daoimpl.ExcelOrganHeaderDaoImpl;
import com.bkav.edoc.service.database.daoimpl.ExcelUserHeaderDaoImpl;
import com.bkav.edoc.service.database.entity.ExcelDailyCounterHeader;
import com.bkav.edoc.service.database.entity.ExcelOrganHeader;
import com.bkav.edoc.service.database.entity.ExcelUserHeader;

import java.util.List;

public class ExcelHeaderService {
    private final ExcelUserHeaderDaoImpl excelUserHeaderDao = new ExcelUserHeaderDaoImpl();
    private final ExcelOrganHeaderDaoImpl excelOrganHeaderDao = new ExcelOrganHeaderDaoImpl();
    private final ExcelDailyCounterHeaderDaoImpl excelDailyCounterHeaderDao = new ExcelDailyCounterHeaderDaoImpl();

    public ExcelUserHeader findUserById(long id) {
        return excelUserHeaderDao.getHeaderById(id);
    }

    public List<ExcelUserHeader> findAllUserHeader() {
        return excelUserHeaderDao.getUserHeader();
    }

    public ExcelOrganHeader findOrganById(long id) {
        return excelOrganHeaderDao.getHeaderById(id);
    }

    public List<ExcelOrganHeader> findAllOrganHeader() {
        return excelOrganHeaderDao.getOrganHeader();
    }

    public List<ExcelDailyCounterHeader> getDailyCounterHeader() {
        return excelDailyCounterHeaderDao.getDailyCounterHeader();
    }

    public ExcelDailyCounterHeader getDailyCounterHeaderById(long id) {
        return excelDailyCounterHeaderDao.getHeaderById(id);
    }
}
