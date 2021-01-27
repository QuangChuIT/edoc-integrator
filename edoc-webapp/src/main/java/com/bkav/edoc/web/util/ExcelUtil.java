package com.bkav.edoc.web.util;

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

    public static Map<String, Object> importOrganFromExcel(MultipartFile file) throws IOException {
        return excelService.readExcelFileForOrganization(file);
    }

    // Fixing...
    public static void exportUserToExcel(HttpServletResponse response) throws IOException {
        excelService.ExportUserToExcel(response);
    }

    public static void exportSampleUserExcelFile(HttpServletResponse response) throws IOException {
        excelService.exportSampleUserExcelFile(response);
    }

    public static void exportOrganToExcel(HttpServletResponse response) throws IOException {
        excelService.ExportOrganToExcel(response);
    }

    public static void exportOrganSampleExcelFile(HttpServletResponse response) throws IOException {
        excelService.ExportSampleOrganExcelFile(response);
    }

    public static long pushUsersToSSO(List<User> users) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return excelService.pushExcelDataToSSO(users);
    }

    public static void exportExcelDailyCounter(HttpServletResponse response, Date fromDate, Date toDate) throws IOException {
        excelService.ExportDailyCounterToExcel(response, fromDate, toDate);
    }

    /*public static void exportStatDetailForTayNinh(List<EdocStatDetail> edocStatDetails) throws IOException {
        excelService.exportStatDetailForTayNinh(edocStatDetails);
    }

     */
}
