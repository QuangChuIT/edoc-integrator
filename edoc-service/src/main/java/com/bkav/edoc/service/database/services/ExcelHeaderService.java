package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.ExcelOrganHeaderDaoImpl;
import com.bkav.edoc.service.database.daoimpl.ExcelUserHeaderDaoImpl;
import com.bkav.edoc.service.database.entity.ExcelOrganHeader;
import com.bkav.edoc.service.database.entity.ExcelUserHeader;

import java.util.List;

public class ExcelHeaderService {
    private final ExcelUserHeaderDaoImpl excelUserHeaderDao = new ExcelUserHeaderDaoImpl();
    private final ExcelOrganHeaderDaoImpl excelOrganHeaderDao = new ExcelOrganHeaderDaoImpl();

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
}
