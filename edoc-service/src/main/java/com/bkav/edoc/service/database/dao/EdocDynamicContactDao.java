package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;

import java.util.List;

public interface EdocDynamicContactDao {

    EdocDynamicContact findByDomain(String domain);

    List<EdocDynamicContact> getDynamicContactsByAgency(boolean agency);

    List<String> getAllDomain();

    Long countOrgan(boolean agency);

    boolean checkPermission(String organId, String token);

    void updateContact(EdocDynamicContact edocDynamicContact);

    void createContact(EdocDynamicContact contact);

    EdocDynamicContact deleteOrgan(long organId);

    String getNameByOrganId(String organId);

    List<EdocDynamicContact> getContactByMultipleDomain(List<String> domains);
}
