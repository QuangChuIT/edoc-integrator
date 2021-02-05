package com.bkav.edoc.sdk.entity;

import java.util.List;

public class OrganResp extends BaseResp {
    private List<Organization> organizations;

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }
}
