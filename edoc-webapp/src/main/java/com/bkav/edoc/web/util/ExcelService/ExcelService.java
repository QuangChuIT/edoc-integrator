package com.bkav.edoc.web.util.ExcelService;

import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.util.*;
import com.bkav.edoc.web.payload.ImportExcelError;
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
import java.util.stream.Collectors;

public class ExcelService {

    private Workbook workbook;
    private Sheet sheet;
    private CellStyle headerStyle;
    private XSSFFont font;
    private CellStyle cellStyle;
    private DataFormat fmt;

    public Map<String, Object> readExcelFileForUser(MultipartFile file) throws IOException {
        List<User> users = new ArrayList<>();
        List<ImportExcelError> errors = new ArrayList<>();
        boolean flag = true;
        InputStream inputStream = file.getInputStream();

        // Create workbook, sheet of excel
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        // Read each row of excel file
        int rowNum = 0;
        int totalCols = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // skip & check header
            if (rowNum == 0) {
                totalCols = currentRow.getPhysicalNumberOfCells();
                rowNum++;
                continue;
            }

            User user = new User();

            for (int cellInd = 0; cellInd < totalCols; cellInd++) {
                Cell cell = currentRow.getCell(cellInd);
                if (cell != null) {
                    createCellStyle();
                    cell.setCellStyle(cellStyle);
                }
                ImportExcelError importExcelError = null;

                switch (cellInd) {
                    case 1:
                        if (cell == null) {
                            importExcelError = new ImportExcelError(cellInd, rowNum, "user.error.username");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            String username = cell.getStringCellValue();
                            user.setUsername(username);
                        }
                        break;
                    case 2:
                        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
                        if (cell == null) {
                            importExcelError = new ImportExcelError(cellInd, rowNum, "user.error.password.empty");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            String password = cell.getStringCellValue();
                            if (!password.matches(passwordRegex)) {
                                importExcelError = new ImportExcelError(cellInd, rowNum, "user.error.password.invalid");
                                errors.add(importExcelError);
                                flag = false;
                            } else {
                                user.setPassword(password);
                            }
                        }
                        break;

                    case 3:
                        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
                        if (cell == null) {
                            importExcelError = new ImportExcelError(cellInd, rowNum, "user.error.email.empty");
                            errors.add(importExcelError);
                            user.setEmailAddress("");
                            flag = false;
                        } else {
                            String email = cell.getStringCellValue();
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
                        if (cell == null) {
                            importExcelError = new ImportExcelError(cellInd, rowNum, "user.error.fullname");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            String displayName = cell.getStringCellValue();
                            user.setDisplayName(cell.getStringCellValue());
                        }
                        break;

                    case 5:
                        if (cell == null) {
                            importExcelError = new ImportExcelError(cellInd, rowNum, "user.error.organId");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            String organ_id = cell.getStringCellValue().trim();
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
                user.setCreateDate(new Date());
                user.setModifiedDate(new Date());
                user.setStatus(true);
                if (flag == true)
                    users.add(user);
                rowNum++;
            }
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

        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        int totalCols = 0;
        int rowNum = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            // skip & check header
            if (rowNum == 0) {
                totalCols = currentRow.getPhysicalNumberOfCells();
                rowNum++;
                continue;
            }

            EdocDynamicContact organ = new EdocDynamicContact();

            for (int cellInd = 0; cellInd < totalCols; cellInd++) {
                Cell cell = currentRow.getCell(cellInd);
                if (cell != null) {
                    createCellStyle();
                    cell.setCellStyle(cellStyle);
                }
                ImportExcelError importExcelError = null;
                switch (cellInd) {
                    case 1:
                        if (cell == null) {
                            importExcelError = new ImportExcelError(cellInd, rowNum, "organ.error.name");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            String name = cell.getStringCellValue();
                            organ.setName(name);
                        }
                        break;
                    case 2:
                        if (cell == null)
                            break;
                        else {
                            String inCharge = cell.getStringCellValue();
                            organ.setInCharge(inCharge);
                        }
                        break;
                    case 3:
                        if (cell == null) {
                            importExcelError = new ImportExcelError(cellInd, rowNum, "organ.error.domain");
                            errors.add(importExcelError);
                            flag = false;
                        } else {
                            String domain = cell.getStringCellValue();
                            organ.setDomain(domain);
                        }
                        break;
                    case 4:
                        if (cell == null)
                            organ.setEmail("");
                        else {
                            String email = cell.getStringCellValue();
                            organ.setEmail(email);
                        }
                        break;
                    case 5:
                        if (cell == null)
                            break;
                        else {
                            String address = cell.getStringCellValue();
                            organ.setAddress(address);
                        }
                        break;
                    case 6:
                        if (cell == null)
                            break;
                        else {
                            String telephone = cell.getStringCellValue();
                            organ.setTelephone(telephone);
                        }
                        break;
                }
            }
            String newToken = TokenUtil.getRandomNumber(organ.getDomain(), organ.getName());
            organ.setToken(newToken);
            organ.setCreateDate(new Date());
            organ.setModifiedDate(new Date());
            organ.setAgency(true);
            organ.setStatus(true);
            if (flag) {
                organs.add(organ);
            }
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

            cell = row.createCell(9);
            cell.setCellValue(organ.getToken());
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

    public Map<String, Long> pushExcelDataToSSO(List<User> users) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, InterruptedException {
        Map<String, Long> results = new HashMap<>();

        String is_username = PropsUtil.get(ConfigParams.IS_USERNAME);
        String is_password = PropsUtil.get(ConfigParams.IS_PASSWORD);
        String is_post_url = PropsUtil.get(ConfigParams.IS_POST_URL);

        String base64encode = PostUserToSSO.getBase64Encode(is_username, is_password);

        // Count number of user push to sso successfully
        long success = 0, duplicate = 0, fail = 0;
        /* Push each user to SSO:*/
        for (User user : users) {
            int out = -1;
            User checkExistUSer = UserServiceUtil.finUserByUsername(user.getUsername());
            if (checkExistUSer == null) {
                //Convert user object to json
                //Then, push json to sso
                String json = PostUserToSSO.createJson(user);
                out = PostUserToSSO.postUser(base64encode, is_post_url, json);
                if (out > 0) {
                    // Set SSO field to true
                    user.setSso(true);
                    UserServiceUtil.createUser(user);
                    /*if (!UserRoleServiceUtil.checkExistUserRole(user.getUserId())) {
                        UserRole userRole = new UserRole();
                        Role role = RoleServiceUtil.getRoleByRoleName("USER");
                        userRole.setUserId(user.getUserId());
                        userRole.setRoleId(role.getRoleId());
                        UserRoleServiceUtil.createUserRole(userRole);
                    }*/
                }
            } else {
                LOGGER.info("Duplicate user with username " + user.getUsername());
                if (!checkExistUSer.isSso()) {
                    String json = PostUserToSSO.createJson(checkExistUSer);
                    out = PostUserToSSO.postUser(base64encode, is_post_url, json);
                    LOGGER.info(out);
                }
            }

            if (out == 201) {
                user.setSso(true);
                UserServiceUtil.updateUser(user);
                success++;
            } else if (out == 409) {
                user.setSso(true);
                UserServiceUtil.updateUser(user);
                duplicate++;
            } else
                fail++;

            /*ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(out, Map.class);

            String username = map.get("userName");
            if (username != null)
                success++;
            else {
                String status = map.get("status");
                if (status.equals("409"))
                    duplicate++;
                else
                    fail++;
            }*/
        }
        LOGGER.info("Push success " + success + " users !!!!!!");
        results.put("Success", success);
        results.put("Duplicate", duplicate);
        results.put("Fail", fail);

        return results;
    }

    public Map<String, Long> syncUserToSSO(List<User> users) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException, InterruptedException {
        Map<String, Long> results = new HashMap<>();

        String is_username = PropsUtil.get(ConfigParams.IS_USERNAME);
        String is_password = PropsUtil.get(ConfigParams.IS_PASSWORD);
        String is_post_url = PropsUtil.get(ConfigParams.IS_POST_URL);

        String base64encode = PostUserToSSO.getBase64Encode(is_username, is_password);

        // Count number of user push to sso successfully
        long success = 0, duplicate = 0, fail = 0;
        /* Push each user to SSO:*/
        for (User user : users) {
            LOGGER.info("Sync user success with username " + user.getUsername());
            String json = PostUserToSSO.createJson(user);
            int out = PostUserToSSO.postUser(base64encode, is_post_url, json);
            LOGGER.info(out);

            if (out == 201) {
                user.setSso(true);
                UserServiceUtil.updateUser(user);
                success++;
            } else if (out == 409) {
                duplicate++;
            } else
                fail++;

            /*ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(out, Map.class);

            String username = map.get("userName");
            if (username != null)
                success++;
            else {
                String status = map.get("status");
                if (status.equals("409"))
                    duplicate++;
                else
                    fail++;
            }*/
        }
        LOGGER.info("Sync success " + success + " users !!!!!!");
        results.put("Success", success);
        results.put("Duplicate", duplicate);
        results.put("Fail", fail);

        return results;
    }

    public void ExportDailyCounterToExcel(HttpServletResponse response, Date fromDate, Date toDate, String keyword) throws IOException {
        List<EPublicStat> eStats;
        if (fromDate == null || toDate == null)
            eStats = EdocDailyCounterServiceUtil.getStatsDetail(null, null, keyword);
        else
            eStats = EdocDailyCounterServiceUtil.getStatsDetail(fromDate, toDate, keyword);

        List<EPublicStat> sortedListStat = eStats.stream().sorted(Comparator.comparing(EPublicStat::getTotal).reversed()).collect(Collectors.toList());

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

        for (EPublicStat ePublicStat : sortedListStat) {
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
        sheet.setColumnWidth(9, 8000);

        sheet.setDefaultRowHeight((short) 450);

        Row header = sheet.createRow(0);

        createHeaderStyle();

        Cell headerCell;

        // Write header row to excel file for organization
        for (int i = 0, j = 1; i < 10; i++, j++) {
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

    private void createCellStyle() {
        cellStyle = workbook.createCellStyle();
        fmt = workbook.createDataFormat();
        cellStyle.setDataFormat(fmt.getFormat("@"));
    }

    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.util.ExcelUtil.class);

}
