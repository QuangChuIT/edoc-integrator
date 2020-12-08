package com.bkav.edoc.web.util;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.web.util.ExcelService.ExcelService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ExcelUtil {

    private final MessageSourceUtil messageSourceUtil;
    private final static ExcelService excelService = new ExcelService();

    public ExcelUtil(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }

    public static List<User> importUserFromExcel(MultipartFile file) throws IOException {
        return excelService.readExcelFileForUser(file);
    }

    public static List<EdocDynamicContact> importOrganFromExcel(MultipartFile file) throws IOException {
        return excelService.readExcelFileForOrganization(file);
    }

    // Optimizing code...

    public static boolean exportUserToExcel(List<User> users) throws IOException {
        return excelService.ExportUserToExcel(users);
    }

    public static boolean exportOrganToExcel(List<EdocDynamicContact> organs) throws IOException {
        return excelService.ExportOrganToExcel(organs);
    }

    public static long PushUsersToSSO(List<User> users) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return excelService.PushExcelDataToSSO(users);
    }
}
