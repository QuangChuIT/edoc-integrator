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
        excelUserHeaderDao.openCurrentSession();
        ExcelUserHeader header = excelUserHeaderDao.getHeaderById(id);
        excelUserHeaderDao.closeCurrentSession();
        return header;
    }

    public List<ExcelUserHeader> findAllUserHeader() {
        excelUserHeaderDao.openCurrentSession();
        List<ExcelUserHeader> headers = excelUserHeaderDao.getUserHeader();
        excelUserHeaderDao.closeCurrentSession();
        return headers;
    }

    public ExcelOrganHeader findOrganById(long id) {
        excelOrganHeaderDao.openCurrentSession();
        ExcelOrganHeader header = excelOrganHeaderDao.getHeaderById(id);
        excelOrganHeaderDao.closeCurrentSession();
        return header;
    }

    public List<ExcelOrganHeader> findAllOrganHeader() {
        excelOrganHeaderDao.openCurrentSession();
        List<ExcelOrganHeader> headers = excelOrganHeaderDao.getOrganHeader();
        excelOrganHeaderDao.closeCurrentSession();
        return headers;
    }
}
