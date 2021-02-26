package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.ExcelDailyCounterHeader;
import com.bkav.edoc.service.database.entity.ExcelOrganHeader;
import com.bkav.edoc.service.database.entity.ExcelUserHeader;
import com.bkav.edoc.service.database.services.ExcelHeaderService;

import java.util.List;

public class ExcelHeaderServiceUtil {
    private final static ExcelHeaderService EXCEL_HEADER_SERVICE = new ExcelHeaderService();

    public static List<ExcelUserHeader> getUserHeader() {
        return EXCEL_HEADER_SERVICE.findAllUserHeader();
    }

    public static ExcelUserHeader getUserHeaderById(long id) {
        return EXCEL_HEADER_SERVICE.findUserById(id);
    }

    public static List<ExcelOrganHeader> getOrganHeader() {
        return EXCEL_HEADER_SERVICE.findAllOrganHeader();
    }

    public static ExcelOrganHeader getOrganHeaderById(long id) {
        return EXCEL_HEADER_SERVICE.findOrganById(id);
    }

    public static ExcelDailyCounterHeader getDailyCounterHeaderById(long id) {
        return EXCEL_HEADER_SERVICE.getDailyCounterHeaderById(id);
    }
}
