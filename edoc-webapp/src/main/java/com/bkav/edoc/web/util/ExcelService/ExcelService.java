package com.bkav.edoc.web.util.ExcelService;

import com.bkav.edoc.converter.entity.EdocStatDetail;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.util.*;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.payload.ImportExcelError;
import com.bkav.edoc.web.util.ExcelUtil;
import com.bkav.edoc.web.util.PropsUtil;
import com.bkav.edoc.web.util.TokenUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ExcelService {

    private Workbook workbook;
    private Sheet sheet;
    private CellStyle headerStyle;
    private XSSFFont font;

    public Map<String, Object> readExcelFileForUser(MultipartFile file) throws IOException {
        List<User> users = new ArrayList<>();
        List<ImportExcelError> errors = new ArrayList<>();
        boolean flag = true;
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
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                currentCell.setCellType(Cell.CELL_TYPE_STRING);
                ImportExcelError importExcelError = null;
                switch (cellIndex) {
                    case 1:
                        String username = currentCell.getStringCellValue();
                        if (username.equals("")) {
                            importExcelError = new ImportExcelError(cellIndex, rowNum, "user.error.username");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            user.setUsername(username);
                        }
                        break;

                    case 2:
                        String password = currentCell.getStringCellValue();
                        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
                        if (password.equals("")) {
                            importExcelError = new ImportExcelError(cellIndex, rowNum, "user.error.password.empty");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            if (!password.matches(passwordRegex)) {
                                importExcelError = new ImportExcelError(cellIndex, rowNum, "user.error.password.invalid");
                                errors.add(importExcelError);
                                flag = false;
                            } else {
                                user.setPassword(password);
                            }
                        }
                        break;

                    case 3:
                        String email = currentCell.getStringCellValue();
                        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
                        if (email.equals("")) {
                            importExcelError = new ImportExcelError(cellIndex, rowNum, "user.error.email.empty");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            user.setEmailAddress(email);
                            /*if (!email.matches(emailRegex)) {
                                importExcelError = new ImportExcelError(cellIndex, rowNum, "user.error.email.invalid");
                                errors.add(importExcelError);
                                flag = false;
                            } else {
                                user.setEmailAddress(email);
                            }*/
                        }
                        break;

                    case 4:
                        String displayName = currentCell.getStringCellValue();
                        if (displayName.equals("")) {
                            importExcelError = new ImportExcelError(cellIndex, rowNum, "user.error.fullname");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            user.setDisplayName(currentCell.getStringCellValue());
                        }
                        break;

                    case 5:
                        String organ_id = currentCell.getStringCellValue().trim();
                        if (organ_id.equals("")) {
                            importExcelError = new ImportExcelError(cellIndex, rowNum, "user.error.organId");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            EdocDynamicContact organ = EdocDynamicContactServiceUtil.findContactByDomain(organ_id);
                            if (organ == null) {
                                // create organ
                                EdocDynamicContact contact = new EdocDynamicContact();
                                contact.setDomain(organ_id);
                                contact.setName(user.getDisplayName());
                                contact.setEmail(user.getEmailAddress());
                                String token = TokenUtil.getRandomNumber(organ_id, user.getDisplayName());
                                contact.setToken(token);
                                contact.setCreateDate(new Date());
                                contact.setStatus(true);
                                EdocDynamicContactServiceUtil.createContact(contact);
                                user.setDynamicContact(contact);
                            } else {
                                user.setDynamicContact(organ);
                            }
                        }
                        break;
                }
                cellIndex++;
            }
            Date currentDate = new Date();
            user.setCreateDate(currentDate);
            user.setModifiedDate(currentDate);
            user.setLastLoginDate(currentDate);
            user.setStatus(true);
            if (flag) {
                users.add(user);
            }
            rowNum++;
        }
        workbook.close();
        Map<String, Object> map = new HashMap<>();
        map.put("errors", errors);
        map.put("users", users);
        return map;
    }

    public Map<String, Object> readExcelFileForOrganization(MultipartFile file) throws IOException {
        List<EdocDynamicContact> organs = new ArrayList<>();
        List<ImportExcelError> errors = new ArrayList<>();
        boolean flag = true;
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

            EdocDynamicContact organ = new EdocDynamicContact();

            int cellIndex = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                currentCell.setCellType(Cell.CELL_TYPE_STRING);
                ImportExcelError importExcelError = null;
                switch (cellIndex) {
                    case 1:
                        String name = currentCell.getStringCellValue();
                        if (name.equals("")) {
                            importExcelError = new ImportExcelError(cellIndex, rowNum, "organ.error.name");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            organ.setName(name);
                        }
                        break;
                    case 2:
                        organ.setInCharge(currentCell.getStringCellValue());
                        break;
                    case 3:
                        String domain = currentCell.getStringCellValue();
                        if (domain.equals("")) {
                            importExcelError = new ImportExcelError(cellIndex, rowNum, "organ.error.domain");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            organ.setDomain(domain);
                        }
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
                }
                cellIndex++;
            }
            String newToken = TokenUtil.getRandomNumber(organ.getDomain(), organ.getName());
            organ.setToken(newToken);
            organ.setCreateDate(new Date());
            organ.setAgency(true);
            organ.setStatus(true);
            if (flag)
                organs.add(organ);
            rowNum++;
        }
        workbook.close();
        Map<String, Object> map = new HashMap<>();
        map.put("errors", errors);
        map.put("organs", organs);
        return map;
    }

    public void ExportUserToExcel(HttpServletResponse response) throws IOException {
        List<User> users = UserServiceUtil.getUser();
        createUserExcelHeader();

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int numRow = 1;

        for (User user : users) {
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

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        LOGGER.info("Write users to Excel END!!!!!!!!!");
    }

    public void exportSampleUserExcelFile(HttpServletResponse response) throws IOException {
        createUserExcelHeader();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        LOGGER.info("Write a sample file for user end!!!!");
    }

    public void ExportOrganToExcel(HttpServletResponse response) throws IOException {
        List<EdocDynamicContact> organs = EdocDynamicContactServiceUtil.getAllDynamicContacts();
        createOrganExcelHeader();

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int numRow = 1;

        for (EdocDynamicContact organ : organs) {
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
            numRow++;
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        LOGGER.info("Write organs to Excel END!!!!!!!!!");
    }

    public void ExportSampleOrganExcelFile (HttpServletResponse response) throws IOException {
        createOrganExcelHeader();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        LOGGER.info("Write a sample file for organs to Excel END!!!!!!!!!");
    }

    public long pushExcelDataToSSO(List<User> users) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String is_username = PropsUtil.get(ConfigParams.IS_USERNAME);
        String is_password = PropsUtil.get(ConfigParams.IS_PASSWORD);
        String is_post_url = PropsUtil.get(ConfigParams.IS_POST_URL);

        // Count number of user push to sso successfully
        long count = 0;
        /* Push each user to SSO:*/
        for (User user : users) {
            User checkExist = UserServiceUtil.finUserByUsername(user.getUsername());
            if (checkExist == null) {
                //Convert user object to json
                //Then, push json to sso
                String json = PostUserToSSO.createJson(user);
                String out = PostUserToSSO.postUser(is_username, is_password, is_post_url, json);
                if (!out.equals("")) {
                    // Set SSO field to true
                    user.setSso(true);
                    UserServiceUtil.createUser(user);
                    if (!UserRoleServiceUtil.checkExistUserRole(user.getUserId())) {
                        UserRole userRole = new UserRole();
                        Role role = RoleServiceUtil.getRoleByRoleName("USER");
                        userRole.setUserId(user.getUserId());
                        userRole.setRoleId(role.getRoleId());
                        UserRoleServiceUtil.createUserRole(userRole);
                    }
                }
            } else {
                LOGGER.info("Duplicate user with username " + user.getUsername());
                if (!checkExist.isSso()) {
                    String json = PostUserToSSO.createJson(checkExist);
                    String out = PostUserToSSO.postUser(is_username, is_password, is_post_url, json);
                    LOGGER.info(out);
                }
            }
            count++;
        }
        return count;
    }

    public void ExportDailyCounterToExcel(HttpServletResponse response, Date fromDate, Date toDate) throws IOException {
        List<EPublicStat> eStats;
        if (fromDate == null || toDate == null)
            eStats = EdocDailyCounterServiceUtil.getStatsDetail(null, null);
        else
            eStats = EdocDailyCounterServiceUtil.getStatsDetail(fromDate, toDate);
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Thống kê văn bản điện tử");
        sheet.setColumnWidth(0, 15000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 5000);

        sheet.setDefaultRowHeight((short) 450);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;

        // Write header row to excel file for organization
        for (int i = 0, j = 1; i < 4; i++, j++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(ExcelHeaderServiceUtil.getDailyCounterHeaderById(j).getHeaderName());
            headerCell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int numRow = 1;

        for (EPublicStat ePublicStat : eStats) {
            Row row = sheet.createRow(numRow);

            Cell cell = row.createCell(0);
            cell.setCellValue(ePublicStat.getOrganName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(ePublicStat.getSent());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(ePublicStat.getReceived());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(ePublicStat.getTotal());
            cell.setCellStyle(style);

            numRow++;
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

        LOGGER.info("Write data to Excel end!!!!!");
    }

    private void createOrganExcelHeader() {
        workbook = new XSSFWorkbook();

        sheet = workbook.createSheet("Danh sách đơn vị");
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);

        sheet.setDefaultRowHeight((short) 450);

        Row header = sheet.createRow(0);

        createHeaderStyle();

        Cell headerCell;

        // Write header row to excel file for organization
        for (int i = 0, j = 1; i < 9; i++, j++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(ExcelHeaderServiceUtil.getOrganHeaderById(j).getHeaderName());
            headerCell.setCellStyle(headerStyle);
        }
    }

    private void createUserExcelHeader() {
        workbook = new XSSFWorkbook();

        sheet = workbook.createSheet("Danh sách tài khoản");
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 8000);
        sheet.setColumnWidth(5, 5000);
        sheet.setDefaultRowHeight((short) 500);

        Row header = sheet.createRow(0);

        createHeaderStyle();

        Cell headerCell;

        // Write header row to excel file for user
        for (int i = 0, j = 1; i < 6; i++, j++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(ExcelHeaderServiceUtil.getUserHeaderById(j).getHeaderName());
            headerCell.setCellStyle(headerStyle);
        }
    }

    private void createHeaderStyle() {
        headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);
    }

   /* public void exportStatDetailForTayNinh(List<EdocStatDetail> edocStatDetails) throws IOException {
        List<String> header_excel = Arrays.asList("Đơn vị", "Tổng", "Nhận nội bộ", "Nhận bên ngoài", "Ký số", "Không ký số");
        int i = 0;
        workbook = new XSSFWorkbook();

        sheet = workbook.createSheet("Thống kê văn bản");
        sheet.setColumnWidth(0, 15000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setDefaultRowHeight((short) 500);

        Row header = sheet.createRow(0);

        createHeaderStyle();

        Cell headerCell;

        for (String head: header_excel) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(head);
            headerCell.setCellStyle(headerStyle);
            i++;
        }

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int numRow = 1;

        for (EdocStatDetail edocStatDetail : edocStatDetails) {
            Row row = sheet.createRow(numRow);

            Cell cell = row.createCell(0);
            EdocDynamicContact organ = EdocDynamicContactServiceUtil.findContactByDomain(edocStatDetail.getOrganDomain());
            cell.setCellValue(organ.getName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(edocStatDetail.getTotal());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(edocStatDetail.getReceived_int());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(edocStatDetail.getReceived_ext());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(edocStatDetail.getSigned());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(edocStatDetail.getNot_signed());
            cell.setCellStyle(style);
            numRow++;
        }

        FileOutputStream outputStream = new FileOutputStream("/home/huynq/Desktop/ThongKeVanBanNhan.xlsx");
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        LOGGER.info("Write users to Excel END!!!!!!!!!");
    }

    */

    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.util.ExcelUtil.class);

}
