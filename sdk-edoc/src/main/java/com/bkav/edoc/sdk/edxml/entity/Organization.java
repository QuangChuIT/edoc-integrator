package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

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
    
}
