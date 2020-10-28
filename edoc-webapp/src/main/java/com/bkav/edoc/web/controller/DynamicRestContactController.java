package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.web.util.TokenUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DynamicRestContactController {

    @RequestMapping(value = "/contact/-/document/contacts", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseBody
    public ResponseEntity<?> getAllContact() {
        try {
            List<OrganizationCacheEntry> organs = EdocDynamicContactServiceUtil.getAllContacts();
            if (organs != null) {
                return new ResponseEntity<>(organs, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/contact/-/document/contacts/{organId}", //
            method = RequestMethod.GET, //
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<OrganizationCacheEntry> getOrgan(@PathVariable("organId") String organId) {
        try {
            long organ_Id = Long.parseLong(organId);
            OrganizationCacheEntry organ = EdocDynamicContactServiceUtil.findById(organ_Id);
            if (organ != null) {
                return new ResponseEntity<>(organ, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/contact/-/document/contact/token/{organId}", method = RequestMethod.POST)
    public ResponseEntity<OrganizationCacheEntry> updateToken(@PathVariable("organId") String organId) {
        if (organId != null) {
            long contactId = Long.parseLong(organId);
            EdocDynamicContact contact = EdocDynamicContactServiceUtil.findDynamicContactById(contactId);
            String newToken = TokenUtil.getRandomNumber(contact.getDomain(), contact.getName());
            contact.setToken(newToken);
            EdocDynamicContactServiceUtil.updateContact(contact);
            OrganizationCacheEntry organizationCacheEntry = MapperUtil.modelToOrganCache(contact);
            return new ResponseEntity<>(organizationCacheEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    private static final Logger logger = Logger.getLogger(com.bkav.edoc.web.controller.DynamicRestContactController.class);
}