package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;

import java.util.List;

public interface EdocDynamicContactDao {

    EdocDynamicContact findByDomain(String domain);

    List<EdocDynamicContact> getDynamicContactsByDomainFilter(String domain);

    Long countOrgan(String organDomain);

    boolean checkPermission(String organId, String token);

    void updateContact(EdocDynamicContact edocDynamicContact);

    void createContact(EdocDynamicContact contact);

    EdocDynamicContact deleteOrgan(long organId);
}
