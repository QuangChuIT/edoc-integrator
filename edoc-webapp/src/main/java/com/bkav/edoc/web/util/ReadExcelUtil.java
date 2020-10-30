package com.bkav.edoc.web.util;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.UserServiceUtil;
import com.bkav.edoc.web.util.importExcel.ConfigParams;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadExcelUtil {
    public static List<User> readExcelFileForUser(MultipartFile file) throws IOException {
        List<User> users = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        int rowNum = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // skip & check header
            if (rowNum == 0) {
                rowNum++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();

            User user = new User();

            int cellIndex = 0;
            int count = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIndex) {
                    case 1:
                        user.setUsername(currentCell.getStringCellValue());
                        break;

                    case 2:
                        user.setPassword(currentCell.getStringCellValue());
                        break;

                    case 3:
                        user.setEmailAddress(currentCell.getStringCellValue());
                        break;

                    case 4:
                        user.setDisplayName(currentCell.getStringCellValue());
                        break;

                    case 5:
                        String organ_id = currentCell.getStringCellValue();
                        String[] organDomain = organ_id.split("@");
                        EdocDynamicContact organ = EdocDynamicContactServiceUtil.findContactByDomain(organDomain[0]);
                        user.setDynamicContact(organ);
                        break;

                    default:
                        break;
                }
                cellIndex++;
            }
            users.add(user);
        }
        workbook.close();
        return users;
    }

    public static void PushExcelDataToSSO(List<User> users) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String is_username = PropsUtil.get(ConfigParams.IS_USERNAME);
        String is_password = PropsUtil.get(ConfigParams.IS_PASSWORD);
        String is_post_url = PropsUtil.get(ConfigParams.IS_POST_URL);

//        users.forEach(user -> {
//            String json = PostUserToSSO.createJson(user);
//
//            String out = PostUserToSSO.postUser(is_username, is_password, is_post_url, json);
//
//        });

        for (User user : users) {
            /*String json = PostUserToSSO.createJson(user);*/
            /*String out = PostUserToSSO.postUser(is_username, is_password, is_post_url, json);*/
            /*LOGGER.info("Post user to sso for user " + user.getUsername() + " with response " + out);*/
            UserServiceUtil.createUser(user);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ReadExcelUtil.class);
}
