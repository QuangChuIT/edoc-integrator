package com.bkav.edoc.converter.impl;

import com.bkav.edoc.converter.entity.ParentDynamiccontact;
import com.bkav.edoc.converter.util.DBConnectionUtil;
import com.bkav.edoc.converter.util.StringQuery;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class ExportParentDomain {
    private static final Logger LOGGER = Logger.getLogger(ExportParentDomain.class);

    public void exportParentDomain() throws SQLException {
        try (Connection connection = DBConnectionUtil.initConvertDBConnection()) {
            Statement stm;
            stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(StringQuery.GET_DOMAIN);

            //List<ParentDynamiccontact> list = new ArrayList<>();

            while (rs.next()) {
                String domain = rs.getString(1);
                String parentDomain = getParentDomain(domain);
                LOGGER.info("Find parent of organ with domain " + domain + " : " + parentDomain);

                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(domain);
                if (parentDomain.equals("Root")) {
                    contact.setParent(contact.getId());
                    EdocDynamicContactServiceUtil.updateContact(contact);
                } else {
                    EdocDynamicContact parentContact = EdocDynamicContactServiceUtil.findContactByDomain(parentDomain);
                    if (parentContact != null) {
                        contact.setParent(parentContact.getId());
                        EdocDynamicContactServiceUtil.updateContact(contact);
                    }
                }
                LOGGER.info("Updated organ with domain " + domain);

                /*String name = "";
                if (contact != null) {
                    //name = contact.getName();
                    ParentDynamiccontact parentDynamiccontact = new ParentDynamiccontact();
                    parentDynamiccontact.setName(name);
                    parentDynamiccontact.setDomain(domain);
                    parentDynamiccontact.setParentDomain(parentDomain);
                    list.add(parentDynamiccontact);
                }*/
            }
            //exportToExcel(list);
        } catch (SQLException throwable) {
            LOGGER.error(throwable);
        }
    }

    private String getParentDomain(String domain) {
        String parent = "";
        String[] subDomain = domain.split("\\.");

        if (subDomain[0].equals("000")) {
            if (subDomain[1].equals("00")) {
                if (subDomain[2].equals("00")){
                    if (subDomain[3].equals("H53"))
                        return "Root";
                } else {
                    subDomain[2] = "00";
                }
            } else {
                subDomain[1] = "00";
            }
        } else {
            subDomain[0] = "000";
        }

        for (int i = 0; i < subDomain.length; i++) {
            parent += subDomain[i];
            if (i == 3)
                break;
            parent += ".";
        }
        return parent;
    }

    private void exportToExcel (List<ParentDynamiccontact> result) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setDefaultRowHeight((short) 500);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell;

        headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);
        headerCell = header.createCell(1);
        headerCell.setCellValue("Code");
        headerCell.setCellStyle(headerStyle);
        headerCell = header.createCell(2);
        headerCell.setCellValue("Pcode");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        int numRow = 1;

        for (ParentDynamiccontact parentDynamiccontact: result) {
            Row row = sheet.createRow(numRow);

            Cell cell = row.createCell(0);
            cell.setCellValue(parentDynamiccontact.getName());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(parentDynamiccontact.getDomain());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(parentDynamiccontact.getParentDomain());
            cell.setCellStyle(style);

            numRow++;
        }

        FileOutputStream outputStream = new FileOutputStream("/home/huynqy/Desktop/Don_vi.xlsx");
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        LOGGER.info("Write to excel done !!!!!!!!!!!!!!!");
    }

    public static void main(String[] args) throws SQLException {
        LOGGER.info("Starting..........");
        long startTime = System.currentTimeMillis();
        ExportParentDomain exportParentDomain = new ExportParentDomain();
        exportParentDomain.exportParentDomain();
        long endTime = System.currentTimeMillis();
        LOGGER.info("Run time: " + (endTime-startTime)/60000.0 + " minutes");
        LOGGER.info("Done!!!!!!!!!!!!!!");
    }
}
