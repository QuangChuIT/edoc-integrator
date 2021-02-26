package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.ExcelUserHeader;

import java.util.List;

public interface ExcelUserHeaderDao {

    List<ExcelUserHeader> getUserHeader();

    ExcelUserHeader getHeaderById(long id);
}
