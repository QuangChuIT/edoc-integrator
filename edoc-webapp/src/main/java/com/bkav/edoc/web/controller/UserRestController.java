package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.util.*;
import com.bkav.edoc.web.payload.AddUserRequest;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.payload.UserRequest;
import com.bkav.edoc.web.util.*;
import com.bkav.edoc.web.util.ExcelUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class UserRestController {

    private final MessageSourceUtil messageSourceUtil;
    private final ValidateUtil validateUtil;

    public UserRestController(MessageSourceUtil messageSourceUtil, ValidateUtil validateUtil) {
        this.messageSourceUtil = messageSourceUtil;
        this.validateUtil = validateUtil;
    }


    @RequestMapping(value = "/public/-/users",
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

    @RequestMapping(value = "/public/-/user/{userId}", //
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
            value = "/public/-/user/import")
    public ResponseEntity<?> importUserFromExcel(@RequestParam("importUserFromExcel") MultipartFile file) {
        List<String> errors = new ArrayList<>();
        long numOfUser = 0;
        try {
            if (validateUtil.checkExtensionFile(file)) {
                if(validateUtil.checkHeaderExcelFileForUser(file)) {
                    List<User> users = ExcelUtil.importUserFromExcel(file);
                    numOfUser = ExcelUtil.PushUsersToSSO(users);
                    String readFileSuccess = messageSourceUtil.getMessage("edoc.message.read.file.success", null);
                    errors.add(readFileSuccess);
                    return new ResponseEntity<>(numOfUser, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(numOfUser, HttpStatus.NOT_ACCEPTABLE);
                }
            } else {
                String invalidFormat = messageSourceUtil.getMessage("edoc.message.user.file.format.error", null);
                LOGGER.error(invalidFormat);
                errors.add(invalidFormat);
                return new ResponseEntity<>(numOfUser, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            String uploadExcelError = messageSourceUtil.getMessage("edoc.message.file.upload.error", null);
            LOGGER.error(uploadExcelError + e.getMessage());
            errors.add(uploadExcelError);
            return new ResponseEntity<>(numOfUser, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/public/-/user/delete/{userId}")
    public HttpStatus deleteUser(@PathVariable("userId") Long userId) {
        if (userId == null) {
            return HttpStatus.BAD_REQUEST;
        } else {
            boolean deleteResult = UserServiceUtil.deleteUser(userId);
            if (deleteResult) {
                return HttpStatus.OK;
            } else {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    @RequestMapping(value = "/public/-/user/create", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> createUser(@RequestBody AddUserRequest addUserRequest) {
        List<String> errors = new ArrayList<>();
        try {
            String message = "";
            int code = 200;
            if (addUserRequest != null) {
                errors = validateUtil.validateAddUser(addUserRequest);
                if (errors.size() == 0) {
                    String fullName = addUserRequest.getDisplayName();
                    String userName = addUserRequest.getUserName();
                    String emailAddress = addUserRequest.getEmailAddress();
                    List<String> organDomains = addUserRequest.getOrganDomain();
                    String organDomain = organDomains.get(0);
                    String password = addUserRequest.getPassword();
                    EdocDynamicContact organization = EdocDynamicContactServiceUtil.findContactByDomain(organDomain);

                    User newUser = new User();
                    newUser.setDisplayName(fullName);
                    newUser.setUsername(userName);
                    newUser.setEmailAddress(emailAddress);
                    newUser.setDynamicContact(organization);
                    Date currentDate = new Date();
                    newUser.setCreateDate(currentDate);
                    newUser.setModifiedDate(currentDate);
                    newUser.setStatus(true);
                    newUser.setPassword(password);

                    UserServiceUtil.createUser(newUser);
                    message = messageSourceUtil.getMessage("user.message.create.success", null);
                } else {
                    code = 400;
                    message = messageSourceUtil.getMessage("user.message.create.fail", null);
                }
            }
            Response response = new Response(code, errors, message);
            return new ResponseEntity<>(response, HttpStatus.valueOf(code));
        } catch (Exception e) {
            errors.add(messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            Response response = new Response(500, errors, messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/public/-/user/export", method = RequestMethod.POST)
    public HttpStatus ExportUserToExcel() throws IOException {
        boolean result;
        List<User> users = UserServiceUtil.getUser();
        result = ExcelUtil.exportUserToExcel(users);
        if(result)
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;
    }



    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.controller.UserRestController.class);
}