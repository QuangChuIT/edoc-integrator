package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.resource.StringPool;
import com.google.common.base.MoreObjects;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "Organizations", namespace = StringPool.TARGET_NAMESPACE)
@XmlType(name = "Organizations", propOrder = {"organization"})
@XmlAccessorType(XmlAccessType.FIELD)
public class GetOrganizationsResponse {

    @XmlElement(name = "Organization")
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
