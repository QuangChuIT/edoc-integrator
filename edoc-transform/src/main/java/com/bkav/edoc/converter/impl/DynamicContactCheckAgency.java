package com.bkav.edoc.converter.impl;

import com.bkav.edoc.converter.entity.DynamicContactAgency;
import com.bkav.edoc.converter.util.DBConnectionUtil;
import com.bkav.edoc.converter.util.StringQuery;
import com.bkav.edoc.converter.util.TokenUtil;
import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DynamicContactCheckAgency {
    private static final Logger LOGGER = Logger.getLogger(DynamicContactCheckAgency.class);
    private final String fileName = "excel/Don_vi.xlsx";

    public List<DynamicContactAgency> getAgencyByExcel() throws IOException {

        List<DynamicContactAgency> dynamicContactAgencies = new ArrayList<>();

        LOGGER.info("----------Start read excel to get agency----------------");

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);

        // Create workbook, sheet of excel
        Workbook workbook = new XSSFWorkbook(is);
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

            DynamicContactAgency dynamicContactAgency = new DynamicContactAgency();

            int cellIndex = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                currentCell.setCellType(Cell.CELL_TYPE_STRING);
                switch (cellIndex) {
                    case 0:
                        String name = currentCell.getStringCellValue();
                        dynamicContactAgency.setAgencyName(name);
                        break;
                    case 1:
                        String domain = currentCell.getStringCellValue();
                        dynamicContactAgency.setAgencyDomain(domain);
                        break;
                }
                cellIndex++;
            }
            dynamicContactAgencies.add(dynamicContactAgency);
            rowNum++;
        }
        workbook.close();

        LOGGER.info("----Got " + dynamicContactAgencies.size() + " agencies---------");

        return dynamicContactAgencies;
    }

    public void submitAgencyToDatabase(List<DynamicContactAgency> dynamicContactAgencies) throws SQLException {

        LOGGER.info("--------------Start check agency in database------------------");

        long count_new = 0;
        int flag = 0;
        try (Connection connection = DBConnectionUtil.initConvertDBConnection()) {
            Statement stm;
            stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(StringQuery.GET_ALL_DOMAIN_CONTACT);

            for (DynamicContactAgency dynamicContactAgency: dynamicContactAgencies) {
                String agencyDomain = dynamicContactAgency.getAgencyDomain();

                LOGGER.info(" Start check Agency domain is " + agencyDomain);

                //List<String> contactDomains = EdocDynamicContactServiceUtil.getAllDomain();
                while (rs.next() && flag == 0) {
                    String contactDomain = rs.getString(1);
                    if (agencyDomain.equals(contactDomain)) {
                        EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(contactDomain);
                        OrganizationCacheEntry organizationCacheEntry = MapperUtil.modelToOrganCache(contact);
                        contact.setAgency(true);
                        EdocDynamicContactServiceUtil.updateContact(organizationCacheEntry, contact);
                        flag++;
                    }
                }
                if (flag == 0) {
                    EdocDynamicContact contact = new EdocDynamicContact();
                    contact.setDomain(agencyDomain);
                    contact.setName(dynamicContactAgency.getAgencyName());
                    contact.setInCharge("Lâm Đồng");
                    contact.setAddress("tỉnh Lâm Đồng");
                    contact.setStatus(true);
                    String newToken = TokenUtil.getRandomNumber(agencyDomain, dynamicContactAgency.getAgencyName());
                    contact.setToken(newToken);
                    contact.setAgency(true);
                    EdocDynamicContactServiceUtil.createContact(contact);
                    count_new++;
                }
                flag = 0;
            }

        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
        LOGGER.info(count_new + " new agency has been created!!!!!");
    }

    public static void main(String[] args) throws IOException, SQLException {
        SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy: HH:mm:ss");
        long start = System.currentTimeMillis();
        Date dateTimeStart = new Date(start);
        LOGGER.info("--------Check agency invoke--------------");
        LOGGER.info("Time start: " + dt.format(dateTimeStart));
        DynamicContactCheckAgency dynamicContactCheckAgency = new DynamicContactCheckAgency();
        List<DynamicContactAgency> dynamicContactAgencyList = dynamicContactCheckAgency.getAgencyByExcel();
        dynamicContactCheckAgency.submitAgencyToDatabase(dynamicContactAgencyList);
        LOGGER.info("-----------Check agency done-------------");

        long end = System.currentTimeMillis();
        Date dateTimeEnd = new Date(end);
        LOGGER.info("Time end: " + dt.format(dateTimeEnd));
    }
}
