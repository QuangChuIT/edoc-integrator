package com.bkav.edoc.web.util;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.web.util.ExcelService.ExcelService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    private final static ExcelService excelService = new ExcelService();

    public static Map<String, Object> importUserFromExcel(MultipartFile file) throws IOException {
        return excelService.readExcelFileForUser(file);
    }

    public static List<EdocDynamicContact> importOrganFromExcel(MultipartFile file) throws IOException {
        return excelService.readExcelFileForOrganization(file);
    }

    // Fixing...
//    public static boolean exportUserToExcel(List<User> users) throws IOException {
//        return excelService.ExportUserToExcel(users);
//    }
//
//    public static boolean exportOrganToExcel(List<EdocDynamicContact> organs) throws IOException {
//        return excelService.ExportOrganToExcel(organs);
//    }

    public static long pushUsersToSSO(List<User> users) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return excelService.pushExcelDataToSSO(users);
    }

    public static void exportExcelDailyCounter(HttpServletResponse response, Date fromDate, Date toDate) throws IOException {
        excelService.ExportDailyCounterToExcel(response, fromDate, toDate);
    }
}
