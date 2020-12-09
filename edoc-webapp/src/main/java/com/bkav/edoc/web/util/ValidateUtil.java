package com.bkav.edoc.web.util;

import com.bkav.edoc.service.database.util.ExcelHeaderServiceUtil;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.web.payload.AddUserRequest;
import com.bkav.edoc.web.payload.ContactRequest;
import com.bkav.edoc.web.payload.DocumentRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class ValidateUtil {

    private final MessageSourceUtil messageSourceUtil;

    public ValidateUtil(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }

    public List<String> validate(DocumentRequest documentRequest) {
        List<String> errors = new ArrayList<>();
        // validate document request
        if (documentRequest.getFromOrgan().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.from.organ", null));
        }

        if (documentRequest.getToOrganDomain() == null || documentRequest.getToOrganDomain().size() == 0) {
            errors.add(messageSourceUtil.getMessage("edoc.error.to.organ", null));
        }

        if (documentRequest.getCodeNumber().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.code.number", null));
        }

        if (documentRequest.getCodeNation().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.code.nation", null));
        }

        if (documentRequest.getPromulgationDate().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.promulgation.date", null));
        }

        if (documentRequest.getStaffName().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.staff.name", null));
        }

        if (documentRequest.getSignerFullName().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.signer.info", null));
        }

        if (documentRequest.getAttachmentIds() == null || documentRequest.getAttachmentIds().size() == 0) {
            errors.add(messageSourceUtil.getMessage("edoc.error.attachment", null));
        }

        return errors;
    }

    public List<String> validateAddUser(AddUserRequest addUserRequest) {
        List<String> errors = new ArrayList<>();
        if (addUserRequest.getDisplayName().equals("")) {
            errors.add(messageSourceUtil.getMessage("user.error.displayName", null));
        }

        if (addUserRequest.getUserName().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.username", null));
        }

        if (addUserRequest.getEmailAddress().equals("")) {
            errors.add(messageSourceUtil.getMessage("edoc.error.email.address", null));
        }

//        if (addUserRequest.getPassword().equals("")) {
//            errors.add(messageSourceUtil.getMessage("edoc.error.password", null));
//        }

        return errors;
    }

    public List<String> validateAddOrgan(ContactRequest contactRequest) {
        List<String> errors = new ArrayList<>();
        if (contactRequest.getName().equals("")) {
            errors.add(messageSourceUtil.getMessage("organ.error.name", null));
        }

        if (contactRequest.getDomain().equals("")) {
            errors.add(messageSourceUtil.getMessage("organ.error.domain", null));
        }

        if (contactRequest.getAddress().equals("")) {
            errors.add(messageSourceUtil.getMessage("organ.error.address", null));
        }

        if (contactRequest.getInCharge().equals("")) {
            errors.add(messageSourceUtil.getMessage("organ.error.inChrage", null));
        }

        if (contactRequest.getEmail().equals("")) {
            errors.add(messageSourceUtil.getMessage("organ.error.email", null));
        }
        return errors;
    }

    public boolean checkExtensionFile(MultipartFile file) {
        String extension = AttachmentGlobalUtil.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        return extension.equals("xlsx") || extension.equals("xls");
    }

    public boolean checkHeaderExcelFileForUser (MultipartFile file) throws IOException {
        boolean flag = false;

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        //Iterate through each row from first sheet
        Iterator<Row> rowIterator = sheet.iterator();

        Row row = rowIterator.next();

        //For each row iterate through each columns
        Iterator<Cell> cellIterator = row.cellIterator();

        int colIndex = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (colIndex == 0 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getUserHeaderById(1).getHeaderName())) {
                colIndex++;
            } else if (colIndex == 1 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getUserHeaderById(2).getHeaderName())) {
                colIndex++;
            } else if (colIndex == 2 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getUserHeaderById(3).getHeaderName())) {
                colIndex++;
            } else if (colIndex == 3 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getUserHeaderById(4).getHeaderName())) {
                colIndex++;
            } else if (colIndex == 4 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getUserHeaderById(5).getHeaderName())) {
                colIndex++;
            } else if (colIndex == 5 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getUserHeaderById(6).getHeaderName())) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean checkHeaderExcelFileForOrgan (MultipartFile file) throws IOException {
        boolean flag = false;

        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        //Iterate through each row from first sheet
        Iterator<Row> rowIterator = sheet.iterator();

        Row row = rowIterator.next();

        //For each row iterate through each columns
        Iterator<Cell> cellIterator = row.cellIterator();

        int colIndex = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (colIndex == 0 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(1).getHeaderName())) {
                colIndex++;
                continue;
            } else if (colIndex == 1 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(2).getHeaderName())) {
                colIndex++;
                continue;
            } else if (colIndex == 2 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(3).getHeaderName())) {
                colIndex++;
                continue;
            } else if (colIndex == 3 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(4).getHeaderName())) {
                colIndex++;
                continue;
            } else if (colIndex == 4 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(5).getHeaderName())) {
                colIndex++;
                continue;
            } else if (colIndex == 5 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(6).getHeaderName())) {
                colIndex++;
                flag = true;
            } else if (colIndex == 6 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(7).getHeaderName())) {
                colIndex++;
                flag = true;
            } else if (colIndex == 7 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(8).getHeaderName())) {
                colIndex++;
                flag = true;
            } else if (colIndex == 8 && cell.getStringCellValue().equals(ExcelHeaderServiceUtil.getOrganHeaderById(9).getHeaderName())) {
                flag = true;
            }
        }

        return flag;
    }
}
