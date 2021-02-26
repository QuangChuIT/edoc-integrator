package com.bkav.edoc.converter.impl;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DynamiccontactCheckReceiveNotify {
    private static final Logger LOGGER = Logger.getLogger(DynamicContactCheckAgency.class);
    private final String fileName = "excel/don_vi_khong_nhan_notify.xlsx";

    public List<String> getOrganDomainNotReceiveNotify() throws IOException {
        List<String> domains = new ArrayList<>();

        LOGGER.info("--------------Starting to get organ domain not receive email notify---------------");

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);

        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        int rowNum = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            // Skip header row
            if (rowNum == 0) {
                rowNum++;
                continue;
            }

            Iterator<Cell> cellIterator = currentRow.iterator();

            int cellIndex = 0;
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                currentCell.setCellType(Cell.CELL_TYPE_STRING);
                switch (cellIndex) {
                    case 1:
                        String domain = currentCell.getStringCellValue();
                        domains.add(domain);
                        break;
                }
                cellIndex++;
            }
            rowNum++;
        }
        workbook.close();

        LOGGER.info("End check organ not receive email notify!!!!");

        return domains;
    }

    public void submitToDatabase(List<String> domains) {
        LOGGER.info("--------------Start set in database------------------");

        try {
            for (String domain: domains) {
                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(domain);
                if (contact != null) {
                    contact.setModifiedDate(new Date());
                    contact.setReceiveNotify(false);
                    EdocDynamicContactServiceUtil.updateContact(contact);
                    LOGGER.info("Update success with " + domain);
                } else {
                    LOGGER.info("Not find organ with domain " + domain + "!!!!!!!!!!");
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public void updateReceiveNotify () {
        List<EdocDynamicContact> allContact = EdocDynamicContactServiceUtil.getDynamicContactByAgency(true);
        int count = 0;
        try {
            for (EdocDynamicContact contact: allContact) {
                LOGGER.info("Update organ with domain " + contact.getDomain());
                contact.setReceiveNotify(true);
                contact.setModifiedDate(new Date());
                EdocDynamicContactServiceUtil.updateContact(contact);
                count++;
            }
            LOGGER.info("Updated " + count + " organ");
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private boolean checkOrganAgency(String organId) {
        boolean result = false;
        try {
            EdocDynamicContact edocDynamicContact = EdocDynamicContactServiceUtil.findContactByDomain(organId);
            if (edocDynamicContact != null) {
                result = edocDynamicContact.getAgency();
            }
        } catch (Exception e) {
            LOGGER.error("Error check organ to stat cause " + e);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy: HH:mm:ss");
        long start = System.currentTimeMillis();

        LOGGER.info("--------Check receive email notify invoke--------------");
        DynamiccontactCheckReceiveNotify run = new DynamiccontactCheckReceiveNotify();
        List<String> domainList = run.getOrganDomainNotReceiveNotify();
        run.submitToDatabase(domainList);
        LOGGER.info("-----------Check done-------------");

        long end = System.currentTimeMillis();
        LOGGER.info("Run time: " + (end-start)/60000 + " minute(s)");
    }
}
