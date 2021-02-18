package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class Organization {

    private String organId;
    private String organizationInCharge;
    private String organName;
    private String organAdd;
    private String email;
    private String telephone;
    private String fax;
    private String website;

    public Organization() {
    }

    public Organization(String organId, String organName, String email) {
        this.organId = organId;
        this.organName = organName;
        this.email = email;
    }

    public Organization(String organId, String organizationInCharge, String organName, String organAdd, String email, String telephone, String fax, String website) {
        this.organId = organId;
        this.organizationInCharge = organizationInCharge;
        this.organName = organName;
        this.organAdd = organAdd;
        this.email = email;
        this.telephone = telephone;
        this.fax = fax;
        this.website = website;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganizationInCharge() {
        return organizationInCharge;
    }

    public void setOrganizationInCharge(String organizationInCharge) {
        this.organizationInCharge = organizationInCharge;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganAdd() {
        return organAdd;
    }

    public void setOrganAdd(String organAdd) {
        this.organAdd = organAdd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId", this.organId)
                .add("OrganizationInCharge", this.organizationInCharge)
                .add("OrganName", this.organName)
                .add("OrganAdd", this.organAdd)
                .add("Email", this.email).add("Telephone", this.telephone)
                .add("Fax", this.fax).add("Website", this.website).toString();
    }

    public static Organization getData(Element element) {
        if (element == null) {
            return null;
        } else {
            List<Element> elements = element.getChildren();
            if (elements != null && elements.size() != 0) {
                Organization organization = new Organization();
                for (Element itemElement : elements) {
                    if ("OrganId".equals(itemElement.getName())) {
                        organization.setOrganId(itemElement.getTextTrim());
                    } else if ("OrganizationInCharge".equals(itemElement.getName())) {
                        organization.setOrganizationInCharge(itemElement.getTextTrim());
                    } else if ("OrganName".equals(itemElement.getName())) {
                        organization.setOrganName(itemElement.getTextTrim());
                    } else if ("OrganAdd".equals(itemElement.getName())) {
                        organization.setOrganAdd(itemElement.getTextTrim());
                    } else if ("Email".equals(itemElement.getName())) {
                        organization.setEmail(itemElement.getTextTrim());
                    } else if ("Telephone".equals(itemElement.getName())) {
                        organization.setTelephone(itemElement.getTextTrim());
                    } else if ("Fax".equals(itemElement.getName())) {
                        organization.setFax(itemElement.getTextTrim());
                    } else if ("Website".equals(itemElement.getName())) {
                        organization.setWebsite(itemElement.getTextTrim());
                    }
                }
                return organization;
            } else {
                return null;
            }
        }
    }

}
