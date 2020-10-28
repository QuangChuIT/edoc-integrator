package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.util.UserServiceUtil;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.payload.UserRequest;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserRestController {

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


    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.web.controller.UserRestController.class);
}