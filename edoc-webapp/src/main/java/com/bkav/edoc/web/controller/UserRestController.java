package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.util.UserServiceUtil;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.payload.UserRequest;
import com.bkav.edoc.web.util.*;
import com.bkav.edoc.web.util.ReadExcelUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
public class UserRestController {

    private final MessageSourceUtil messageSourceUtil;

    public UserRestController(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }


    @RequestMapping(value = "/users",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> getAllUser() {
        try {
            List<UserCacheEntry> users = UserServiceUtil.getAllUsers();
            if (users != null) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/public/-/edoc/users", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getUserSSO() {
        try {
            List<UserCacheEntry> userCacheEntries = UserServiceUtil.getUsers(false);
            return new ResponseEntity<>(userCacheEntries, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/public/-/edoc/update", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> updateUserSSO(@RequestBody UserRequest userRequest) {
        Response response;
        try {
            if (userRequest != null) {
                long userId = userRequest.getUserId();
                User user = UserServiceUtil.findUserById(userId);

                if (user != null) {
                    user.setSso(userRequest.isStatus());
                    response = new Response(200, new ArrayList<>(), "Success update user sso!");
                } else {
                    response = new Response(404, new ArrayList<>(), "User not found!");
                }
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            response = new Response(400, new ArrayList<>(), e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response = new Response(400, new ArrayList<>(), "Bad request");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/user/{userId}", //
            method = RequestMethod.GET, //
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<UserCacheEntry> getUser(@PathVariable("userId") String userId) {
        try {
            long user_id = Long.parseLong(userId);
            UserCacheEntry user = UserServiceUtil.getUserById(user_id);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*
     * Excel File Upload
     */
    @RequestMapping(method = RequestMethod.POST,
            value = "/import/-/user/upload")
    public ResponseEntity<Response> importUserFromExcel(@RequestParam("importExcel") MultipartFile file) {
        List<String> errors = new ArrayList<>();
        List<User> users = new ArrayList<>();
        try {
            String extension = AttachmentGlobalUtil.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
            if (!(extension.equals("xlsx") || extension.equals("xls"))) {
                String invalidFormat = messageSourceUtil.getMessage("edoc.message.user.file.format.error", null);
                LOGGER.error(invalidFormat);
                errors.add(invalidFormat);
                Response response = new Response(200);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                if (checkHeaderExcelFileForUser(file)) {
                    users = ReadExcelUtil.readExcelFileForUser(file);
                    // ReadExcelUtil.PushExcelDataToSSO(users);
                    String readFileSuccess = messageSourceUtil.getMessage("edoc.message.read.file.success", null);
                    errors.add(readFileSuccess);
                    Response response = new Response(201);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
                }

                // Return response invalid column...
                Response response = new Response(400);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            String uploadExcelError = messageSourceUtil.getMessage("edoc.message.file.upload.error", null);
            LOGGER.error(uploadExcelError + e.getMessage());
            errors.add(uploadExcelError);
            Response response = new Response(400);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkHeaderExcelFileForUser (MultipartFile file) throws IOException {
        Boolean flag = false;
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        //Iterate through each row from first sheet
        Iterator<Row> rowIterator = sheet.iterator();

        Row row = rowIterator.next();

        //For each row iterate through each columns
        Iterator <Cell> cellIterator = row.cellIterator();

        String stt = messageSourceUtil.getMessage("user.import.excel.header.stt", null);
        String userName = messageSourceUtil.getMessage("user.import.excel.header.username", null);
        String password = messageSourceUtil.getMessage("user.import.excel.header.password", null);
        String email = messageSourceUtil.getMessage("user.import.excel.header.email", null);
        String fullName = messageSourceUtil.getMessage("user.import.excel.header.fullname", null);
        String organDomain = messageSourceUtil.getMessage("user.import.excel.header.organDomain", null);
        int colIndex = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (colIndex == 0 && cell.getStringCellValue().equals(stt)) {
                colIndex++;
                continue;
            } else if (colIndex == 1 && cell.getStringCellValue().equals(userName)) {
                colIndex++;
                continue;
            } else if (colIndex == 2 && cell.getStringCellValue().equals(password)) {
                colIndex++;
                continue;
            } else if (colIndex == 3 && cell.getStringCellValue().equals(email)) {
                colIndex++;
                continue;
            } else if (colIndex == 4 && cell.getStringCellValue().equals(fullName)) {
                colIndex++;
                continue;
            } else if (colIndex == 5 && cell.getStringCellValue().equals(organDomain)) {
                flag = true;
            }
        }
        return flag;
    }

    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.controller.UserRestController.class);
}