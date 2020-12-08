package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.entity.UserRole;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.database.util.UserRoleServiceUtil;
import com.bkav.edoc.service.database.util.UserServiceUtil;
import com.bkav.edoc.web.payload.AddUserRequest;
import com.bkav.edoc.web.payload.EditUserRequest;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.payload.UserRequest;
import com.bkav.edoc.web.util.ExcelUtil;
import com.bkav.edoc.web.util.MessageSourceUtil;
import com.bkav.edoc.web.util.ValidateUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    @RequestMapping(value = "/public/-/user/edit", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Response> editUser(@RequestBody EditUserRequest editUserRequest) {
        List<String> errors = new ArrayList<>();
        try {
            String message = "";
            int code = 200;
            if (editUserRequest != null) {
                if (errors.size() == 0) {
                    String organDomain = editUserRequest.getOrganDomain();
                    EdocDynamicContact organization = EdocDynamicContactServiceUtil.findContactByDomain(organDomain);
                    User user = UserServiceUtil.findUserById(editUserRequest.getUserId());
                    user.setDisplayName(editUserRequest.getDisplayName());
                    user.setEmailAddress(editUserRequest.getEmailAddress());
                    user.setDynamicContact(organization);

                    UserCacheEntry userCacheEntry = MapperUtil.modelToUserCache(user);
                    UserServiceUtil.updateUser(user);
                    message = messageSourceUtil.getMessage("user.message.edit.success", null);
                } else {
                    code = 400;
                    message = messageSourceUtil.getMessage("user.message.edit.fail", null);
                }
            }
            Response response = new Response(code, errors, message);
            return new ResponseEntity<>(response, HttpStatus.valueOf(code));
        } catch (Exception e) {
            errors.add(messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            Response response = new Response(500, errors, messageSourceUtil.getMessage("edoc.message.error.exception", null));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        LOGGER.info("API import user from excel invoke !!!!!!!!!!!!!!!!!!!!!!!!!");
        long numOfUser = 0;
        try {
            if (validateUtil.checkExtensionFile(file)) {
                if (validateUtil.checkHeaderExcelFileForUser(file)) {
                    List<User> users = ExcelUtil.importUserFromExcel(file);
                    LOGGER.info("Convert user data from excel success with user size " + users.size() + " !!!!");
                    numOfUser = ExcelUtil.PushUsersToSSO(users);
                    return new ResponseEntity<>(numOfUser, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(numOfUser, HttpStatus.NOT_ACCEPTABLE);
                }
            } else {
                LOGGER.error("File invalid !!!!!!");
                return new ResponseEntity<>(numOfUser, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Error import user from file cause " + Arrays.toString(e.getStackTrace()));
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
        List<User> users = new ArrayList<>();
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

//                    UserServiceUtil.createUser(newUser);
                    users.add(newUser);
                    long numUserSuccess = ExcelUtil.PushUsersToSSO(users);
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
        if (result)
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/user/-/create/admin", method = RequestMethod.POST)
    public HttpStatus CreateAdminUser() {
        if (UserServiceUtil.checkExistUserByUserName(messageSourceUtil.getMessage("user.create.admin.username", null))) {
            return HttpStatus.OK;
        } else {
            User user = new User();
            user.setUsername(messageSourceUtil.getMessage("user.create.admin.username", null));
            user.setPassword(messageSourceUtil.getMessage("user.create.admin.password", null));
            user.setDisplayName(messageSourceUtil.getMessage("user.create.admin.displayname", null));
            Date currentDate = new Date();
            user.setCreateDate(currentDate);
            user.setModifiedDate(currentDate);
            user.setStatus(true);
            user.setSso(true);

            UserServiceUtil.createUser(user);

            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(1);
            UserRoleServiceUtil.createUserRole(userRole);
            return HttpStatus.CREATED;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.controller.UserRestController.class);
}