package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.entity.Role;
import com.bkav.edoc.service.database.util.RoleServiceUtil;
import com.bkav.edoc.web.util.MessageSourceUtil;
import com.bkav.edoc.web.util.ValidateUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class RoleController {
    private final MessageSourceUtil messageSourceUtil;
    private final ValidateUtil validateUtil;

    public RoleController(MessageSourceUtil messageSourceUtil, ValidateUtil validateUtil) {
        this.messageSourceUtil = messageSourceUtil;
        this.validateUtil = validateUtil;
    }

    @RequestMapping(value = "/role/-/create/role", method = RequestMethod.POST)
    public HttpStatus createRole() {
        Role role = new Role();
        if (!RoleServiceUtil.checkExistRoleByRoleName(messageSourceUtil.getMessage("user.admin.displayname", null))
                || !RoleServiceUtil.checkExistRoleByRoleName(messageSourceUtil.getMessage("user.role.displayname", null))) {
            if (!RoleServiceUtil.checkExistRoleByRoleName(messageSourceUtil.getMessage("user.admin.displayname", null))) {
                role.setRoleName(messageSourceUtil.getMessage("user.admin.displayname", null));
                role.setRoleKey(messageSourceUtil.getMessage("user.admin.key", null));
                role.setDescription(messageSourceUtil.getMessage("user.admin.username", null));
                role.setRoleId(1);
                role.setActive(true);
                Date date = new Date();
                role.setCreateDate(date);

                RoleServiceUtil.createRole(role);
            }
            if (!RoleServiceUtil.checkExistRoleByRoleName(messageSourceUtil.getMessage("user.role.displayname", null))) {
                role.setRoleName(messageSourceUtil.getMessage("user.role.displayname", null));
                role.setRoleKey(messageSourceUtil.getMessage("user.role.key", null));
                role.setDescription(messageSourceUtil.getMessage("user.username", null));
                role.setActive(true);
                role.setRoleId(2);
                Date date = new Date();
                role.setCreateDate(date);

                RoleServiceUtil.createRole(role);
            }
            LOGGER.info(messageSourceUtil.getMessage("user.create.role.success", null));
            return HttpStatus.CREATED;
        }
        LOGGER.error(messageSourceUtil.getMessage("user.create.role.fail", null));
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/public/-/role/{roleName}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> getRoleIdByRoleKey(@PathVariable("roleName") String roleName) {
        try {
            Role role = RoleServiceUtil.getRoleByRoleName(roleName);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.controller.RoleController.class);
}
