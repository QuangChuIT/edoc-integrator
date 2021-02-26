package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.ExcelOrganHeader;

import java.util.List;

public interface ExcelOrganHeaderDao {

    List<ExcelOrganHeader> getOrganHeader();

    ExcelOrganHeader getHeaderById(long id);
}
