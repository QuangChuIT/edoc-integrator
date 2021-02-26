package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.entity.UserRole;
import com.bkav.edoc.service.database.util.UserRoleServiceUtil;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.payload.UserRoleRequest;
import com.bkav.edoc.web.util.MessageSourceUtil;
import com.bkav.edoc.web.util.ValidateUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserRoleRestController {
    private final MessageSourceUtil messageSourceUtil;
    private final ValidateUtil validateUtil;

    public UserRoleRestController(MessageSourceUtil messageSourceUtil, ValidateUtil validateUtil) {
        this.messageSourceUtil = messageSourceUtil;
        this.validateUtil = validateUtil;
    }

    @RequestMapping(value = "/public/-/user/userroles",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> getAllUserRole() {
        try {
            List<UserRole> userRoles = UserRoleServiceUtil.getAllUserRole();
            if (userRoles != null) {
                return new ResponseEntity<>(userRoles, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/public/-/role/{userId}", //
            method = RequestMethod.GET, //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<?> getUserRoleByUserId(@PathVariable("userId") String userId) {
        try {
            long user_id = Long.parseLong(userId);
            UserRole serRole = UserRoleServiceUtil.getRoleByUserId(user_id);
            return new ResponseEntity<>(serRole, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/public/-/create/role/", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createUserRole(@RequestBody UserRoleRequest userRoleRequest) {
        List<String> errors = new ArrayList<>();
        try {
            String message = "";
            int code;
            long userId = userRoleRequest.getUserId();
            long roleId = userRoleRequest.getRoleId();

            /*if (!UserRoleServiceUtil.checkExistUserRole(userId)) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                UserRoleServiceUtil.createUserRole(userRole);
                code = 201;
                message = messageSourceUtil.getMessage("user.message.create.role.success", null);
                Response response = new Response(code, errors, message);
                return new ResponseEntity<>(response, HttpStatus.valueOf(code));
            } else {
                UserRole userRole = UserRoleServiceUtil.getUserRoleByUserId(userId);
                userRole.setRoleId(roleId);
                UserRoleServiceUtil.updateUserRole(userRole);
                code = 200;
                message = messageSourceUtil.getMessage("user.message.create.role.success", null);
                Response response = new Response(code, errors, message);
                return new ResponseEntity<>(response, HttpStatus.valueOf(code));
            }*/
        } catch (Exception e) {
            errors.add(messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            Response response = new Response(500, errors, messageSourceUtil.getMessage("edoc.message.error.exception", new Object[]{e.getMessage()}));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.controller.UserRoleRestController.class);
}
