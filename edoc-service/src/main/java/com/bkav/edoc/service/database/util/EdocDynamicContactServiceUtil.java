package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.services.EdocDynamicContactService;

import java.util.List;
import java.util.Map;

public class EdocDynamicContactServiceUtil {

    private final static EdocDynamicContactService DYNAMIC_CONTACT_SERVICE = new EdocDynamicContactService();

    public static List<OrganizationCacheEntry> getAllContacts() {
        return DYNAMIC_CONTACT_SERVICE.getAllContacts();
    }

    public static int countContacts(PaginationCriteria paginationCriteria) {
        return DYNAMIC_CONTACT_SERVICE.countContacts(paginationCriteria);
    }

    public static Map<String, Object> getContacts(PaginationCriteria paginationCriteria) {
        return DYNAMIC_CONTACT_SERVICE.getContacts(paginationCriteria);
    }

    public static void createContact(EdocDynamicContact edocDynamicContact) {
        DYNAMIC_CONTACT_SERVICE.createContact(edocDynamicContact);
    }

    public static OrganizationCacheEntry findByDomain(String organDomain) {
        return DYNAMIC_CONTACT_SERVICE.findByDomain(organDomain);
    }

    public static EdocDynamicContact findContactByDomain(String organDomain) {
        return DYNAMIC_CONTACT_SERVICE.findContactByDomain(organDomain);
    }

    public static List<EdocDynamicContact> getAllDynamicContacts() {
        return DYNAMIC_CONTACT_SERVICE.getAllDynamicContact();
    }

    public static OrganizationCacheEntry findById(long organId) {
        return DYNAMIC_CONTACT_SERVICE.findById(organId);
    }

    public static List<OrganizationCacheEntry> getDyCacheEntriesByFilterDomain(String domain) {
        return DYNAMIC_CONTACT_SERVICE.getDynamicContactsByFilterDomain(domain);
    }

    public static EdocDynamicContact findDynamicContactById(long contactId) {
        return DYNAMIC_CONTACT_SERVICE.findEdocDynamicContactById(contactId);
    }

    public static void updateContact(OrganizationCacheEntry organizationCacheEntry, EdocDynamicContact contact) {
        DYNAMIC_CONTACT_SERVICE.updateContact(organizationCacheEntry, contact);
    }

    public static boolean deleteContact(long organId) {
        return DYNAMIC_CONTACT_SERVICE.deleteOrgan(organId);
    }
}
