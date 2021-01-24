package com.bkav.edoc.edxml.base.header;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;

public class GetOrganizationsResponse {
    protected List<Organization> organization;

    /**
     * @return the organization
     */
    public List<Organization> getOrganization() {
        return organization;
    }

    /**
     * @param organizations the organization to set
     */
    public void setOrganization(List<Organization> organizations) {
        if (organizations == null) {
            this.organization = new ArrayList<>();
        } else {
            this.organization = organizations;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Organization", organization).toString();
    }
}
