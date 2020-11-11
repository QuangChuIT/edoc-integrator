package com.bkav.edoc.web.util.ExcelService;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.ExcelHeaderServiceUtil;
import com.bkav.edoc.service.database.util.UserServiceUtil;
import com.bkav.edoc.web.util.PropsUtil;
import com.bkav.edoc.web.util.TokenUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelService {

    public List<User> readExcelFileForUser (MultipartFile file) throws IOException {
        List<User> users = new ArrayList<>();

        InputStream inputStream = file.getInputStream();

        // Create workbook, sheet of excel
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        // Read each row of excel file
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
                currentCell.setCellType(Cell.CELL_TYPE_STRING);
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
            Date currentDate = new Date();
            user.setCreateDate(currentDate);
            user.setModifiedDate(currentDate);
            user.setLastLoginDate(currentDate);
            user.setStatus(true);

            users.add(user);
            count++;
        }
        workbook.close();
        return users;
    }

    public List<EdocDynamicContact> readExcelFileForOrganization(MultipartFile file) throws IOException {
        List<EdocDynamicContact> organs = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(1);
        Iterator<Row> rows = sheet.iterator();


        int rowNum = 0;
        int count = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // skip & check header
            if (rowNum == 0) {
                rowNum++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();

            EdocDynamicContact organ = new EdocDynamicContact();

            int cellIndex = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                currentCell.setCellType(Cell.CELL_TYPE_STRING);
                switch (cellIndex) {
                    case 1:
                        organ.setName(currentCell.getStringCellValue());
                        break;
                    case 2:
                        organ.setInCharge(currentCell.getStringCellValue());
                        break;
                    case 3:
                        organ.setDomain(currentCell.getStringCellValue());
                        break;
                    case 4:
                        organ.setEmail(currentCell.getStringCellValue());
                        break;
                    case 5:
                        organ.setAddress(currentCell.getStringCellValue());
                        break;
                    case 6:
                        organ.setTelephone(currentCell.getStringCellValue());
                        break;
                    default:
                        organ.setStatus(true);
                        break;
                }
                cellIndex++;
            }
            String newToken = TokenUtil.getRandomNumber(organ.getDomain(), organ.getName());
            organ.setToken(newToken);
            organs.add(organ);
            count++;
        }
        workbook.close();
        return organs;
    }

    public boolean ExportUserToExcel(List<User> users) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Danh sách tài khoản");
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 8000);
        sheet.setColumnWidth(5, 5000);
        sheet.setDefaultRowHeight((short) 500);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;

        // Write header row to excel file for user
        for (int i = 0, j = 1; i < 6; i++, j++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(j).getHeaderName());
            headerCell.setCellStyle(headerStyle);
        }

//        headerCell = header.createCell(0);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(1).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(1);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(2).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(2);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(3).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(3);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(4).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(4);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(5).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(5);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(6).getHeaderName());
//        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int numRow = 1;

        for (User user: users) {
            Row row = sheet.createRow(numRow);

            Cell cell = row.createCell(0);
            cell.setCellValue(numRow);
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(user.getUsername());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(user.getPassword());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(user.getEmailAddress());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(user.getDisplayName());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(user.getDynamicContact().getDomain());
            cell.setCellStyle(style);
            numRow++;
        }
        File currDir = new File("/home/huynq/edoc/ExportExcel/Danh_sach_tai_khoan.xlsx");
        String path = currDir.getAbsolutePath();

        FileOutputStream outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
        workbook.close();
        return true;
    }

    public boolean ExportOrganToExcel(List<EdocDynamicContact> organs) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Danh sách đơn vị");
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 4000);

        sheet.setDefaultRowHeight((short) 450);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;

        // Write header row to excel file for organization
        for (int i = 0, j = 1; i < 11; i++, j++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(j).getHeaderName());
            headerCell.setCellStyle(headerStyle);
        }

//        headerCell = header.createCell(0);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(1).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(1);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(2).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(2);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(3).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(3);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(4).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(4);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(5).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(5);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(6).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(6);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(7).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(7);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(8).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(8);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(9).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(9);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(10).getHeaderName());
//        headerCell.setCellStyle(headerStyle);
//
//        headerCell = header.createCell(10);
//        headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(11).getHeaderName());
//        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int numRow = 1;

        for (EdocDynamicContact organ: organs) {
            Row row = sheet.createRow(numRow);

            Cell cell = row.createCell(0);
            cell.setCellValue(numRow);
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(organ.getName());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(organ.getInCharge());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(organ.getDomain());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(organ.getEmail());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(organ.getAddress());
            cell.setCellStyle(style);

            cell = row.createCell(6);
            cell.setCellValue(organ.getTelephone());
            cell.setCellStyle(style);

            cell = row.createCell(7);
            cell.setCellValue(organ.getFax());
            cell.setCellStyle(style);

            cell = row.createCell(8);
            cell.setCellValue(organ.getWebsite());
            cell.setCellStyle(style);

            cell = row.createCell(9);
            cell.setCellValue(organ.getType());
            cell.setCellStyle(style);

            cell = row.createCell(10);
            cell.setCellValue(organ.getVersion());
            cell.setCellStyle(style);
            numRow++;
        }
        File currDir = new File("/home/huynq/edoc/ExportExcel/Danh_sach_to_chuc.xlsx");
        String path = currDir.getAbsolutePath();

        FileOutputStream outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
        workbook.close();
        return true;
    }

    public long PushExcelDataToSSO (List<User> users) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String is_username= PropsUtil.get(ConfigParams.IS_USERNAME);
        String is_password= PropsUtil.get(ConfigParams.IS_PASSWORD);
        String is_post_url= PropsUtil.get(ConfigParams.IS_POST_URL);

        // Count number of user push to sso successfully
        long count = 0;

        /* Push each user to SSO:
         Convert user object to json
         Then, push json to sso */
        for (User user: users) {
            String json = PostUserToSSO.createJson(user);
            String out = PostUserToSSO.postUser(is_username, is_password, is_post_url, json);

            // If push to sso successfully
            // Then, insert user object to database
            if(out != null) {
                // Set SSO field to true
                user.setSso(true);
                UserServiceUtil.createUser(user);
            }
            count++;
        }
        return count;
    }

}
