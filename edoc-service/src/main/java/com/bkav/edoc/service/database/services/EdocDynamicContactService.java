package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.daoimpl.EdocDynamicContactDaoImpl;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.redis.RedisKey;

import java.util.ArrayList;
import java.util.List;

public class EdocDynamicContactService {
    private static final EdocDynamicContactDaoImpl dynamicContactDaoImpl = new EdocDynamicContactDaoImpl();

    public EdocDynamicContact getDynamicContactByDomain(String domain) {
        return dynamicContactDaoImpl.findByDomain(domain);
    }

    public OrganizationCacheEntry getOrganizationCache(String domain) {
        String cacheKey = MemcachedKey.getKey(domain, MemcachedKey.DYNAMICCONTACT_KEY);

        OrganizationCacheEntry organizationCacheEntry = (OrganizationCacheEntry) MemcachedUtil.getInstance().read(cacheKey);

        if (organizationCacheEntry == null) {
            EdocDynamicContact dynamicContact = dynamicContactDaoImpl.findByDomain(domain);
            if (dynamicContact != null) {
                organizationCacheEntry = MapperUtil.modelToOrganCache(dynamicContact);
                MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.GET_ORGAN_NAME_BY_DOMAIN_TIME, organizationCacheEntry);
            } else {
                organizationCacheEntry = new OrganizationCacheEntry();
                organizationCacheEntry.setStatus(true);
                organizationCacheEntry.setParent(0L);
                organizationCacheEntry.setToken("");
                organizationCacheEntry.setAddress("");
                organizationCacheEntry.setDomain(domain);
                organizationCacheEntry.setEmail(domain);
                organizationCacheEntry.setName(domain);
                organizationCacheEntry.setInCharge(domain);
                organizationCacheEntry.setTelephone(domain);
            }
        }
        return organizationCacheEntry;

    }

    public List<EdocDynamicContact> getAllDynamicContact() {
        dynamicContactDaoImpl.openCurrentSession();
        List<EdocDynamicContact> contacts = dynamicContactDaoImpl.findAll();
        dynamicContactDaoImpl.closeCurrentSession();
        return contacts;
    }

    public EdocDynamicContact findEdocDynamicContactById(long contactId) {
        dynamicContactDaoImpl.openCurrentSession();
        EdocDynamicContact contact = dynamicContactDaoImpl.findById(contactId);
        dynamicContactDaoImpl.closeCurrentSession();
        return contact;
    }

    public void updateContact(EdocDynamicContact edocDynamicContact) {
        dynamicContactDaoImpl.openCurrentSession();

        dynamicContactDaoImpl.updateContact(edocDynamicContact);

        dynamicContactDaoImpl.closeCurrentSession();
    }

    public OrganizationCacheEntry findById(long organId) {
        OrganizationCacheEntry organizationCacheEntry = null;
        dynamicContactDaoImpl.openCurrentSession();
        EdocDynamicContact dynamicContact = dynamicContactDaoImpl.findById(organId);
        if (dynamicContact != null) {
            organizationCacheEntry = MapperUtil.modelToOrganCache(dynamicContact);
        }
        dynamicContactDaoImpl.closeCurrentSession();
        return organizationCacheEntry;
    }

    public boolean checkPermission(String organId, String token) {
        boolean result = dynamicContactDaoImpl.checkPermission(organId, token);
        return result;
    }

    public EdocDynamicContact findContactByDomain(String organDomain) {
        return dynamicContactDaoImpl.findByDomain(organDomain);
    }

    public OrganizationCacheEntry findByDomain(String organDomain) {
        OrganizationCacheEntry organizationCacheEntry = null;
        dynamicContactDaoImpl.openCurrentSession();
        EdocDynamicContact dynamicContact = dynamicContactDaoImpl.findByDomain(organDomain);
        if (dynamicContact != null) {
            organizationCacheEntry = MapperUtil.modelToOrganCache(dynamicContact);
        }
        dynamicContactDaoImpl.closeCurrentSession();
        return organizationCacheEntry;
    }

    public List<OrganizationCacheEntry> getAllContacts() {
        List<OrganizationCacheEntry> organizationCacheEntries;
        String cacheKey = MemcachedKey.getKey("", RedisKey.GET_LIST_CONTACT_KEY);
        MemcachedUtil.getInstance().delete(cacheKey);
        organizationCacheEntries = (List<OrganizationCacheEntry>) MemcachedUtil.getInstance().read(cacheKey);

        if (organizationCacheEntries == null) {
            organizationCacheEntries = new ArrayList<>();

            dynamicContactDaoImpl.openCurrentSession();

            List<EdocDynamicContact> contacts = dynamicContactDaoImpl.findAll();

            for (EdocDynamicContact contact : contacts) {
                OrganizationCacheEntry organizationCacheEntry = MapperUtil.modelToOrganCache(contact);
                organizationCacheEntries.add(organizationCacheEntry);
            }

            MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, organizationCacheEntries);

            dynamicContactDaoImpl.closeCurrentSession();
        }

        return organizationCacheEntries;
    }

    public Long countOrgan(String domain) {
        dynamicContactDaoImpl.openCurrentSession();
        Long count = dynamicContactDaoImpl.countOrgan(domain);
        dynamicContactDaoImpl.closeCurrentSession();
        return count;
    }

    public List<OrganizationCacheEntry> getDynamicContactsByFilterDomain(String domain) {
        List<OrganizationCacheEntry> organizationCacheEntries;
        String cacheKey = MemcachedKey.getKey("", RedisKey.GET_LIST_CONTACT_BY_KEY);
        organizationCacheEntries = (List<OrganizationCacheEntry>) MemcachedUtil.getInstance().read(cacheKey);

        if (organizationCacheEntries == null) {
            organizationCacheEntries = new ArrayList<>();
            dynamicContactDaoImpl.openCurrentSession();
            List<EdocDynamicContact> contacts = dynamicContactDaoImpl.getDynamicContactsByDomainFilter(domain);

            for (EdocDynamicContact contact : contacts) {
                OrganizationCacheEntry organizationCacheEntry = MapperUtil.modelToOrganCache(contact);
                organizationCacheEntries.add(organizationCacheEntry);
            }

            MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, organizationCacheEntries);

            dynamicContactDaoImpl.closeCurrentSession();
        }

        return organizationCacheEntries;
    }

    public void createContact(EdocDynamicContact contact) {
        dynamicContactDaoImpl.openCurrentSession();
        dynamicContactDaoImpl.createContact(contact);
        dynamicContactDaoImpl.closeCurrentSession();
    }

    public boolean deleteOrgan(long organId) {
        return dynamicContactDaoImpl.deleteOrgan(organId);
    }
}
