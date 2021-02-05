package com.bkav.edoc.payload;

import com.bkav.edoc.service.xml.base.header.Organization;

import java.util.List;

public class GetOrganizationResp extends BaseResp {
    private List<Organization> organizations;

    public GetOrganizationResp() {

    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
}
