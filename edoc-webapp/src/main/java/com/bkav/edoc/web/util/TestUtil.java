package com.bkav.edoc.web.util;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;

import java.util.List;

public class TestUtil {
    public static void main(String[] args) {
        List<EdocDynamicContact> edocDynamicContactList = EdocDynamicContactServiceUtil.getAllDynamicContacts();
        for (EdocDynamicContact edocDynamicContact : edocDynamicContactList) {
            String token = TokenUtil.getRandomNumber(edocDynamicContact.getDomain(), edocDynamicContact.getName());
            edocDynamicContact.setToken(token);
            EdocDynamicContactServiceUtil.updateContact(edocDynamicContact);
        }
        System.out.println("done");
    }
}
