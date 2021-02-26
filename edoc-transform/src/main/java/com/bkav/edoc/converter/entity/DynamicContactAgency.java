package com.bkav.edoc.converter.entity;

import java.io.Serializable;

public class DynamicContactAgency implements Serializable {
    private String agencyName;
    private String agencyDomain;

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyDomain() {
        return agencyDomain;
    }

    public void setAgencyDomain(String agencyDomain) {
        this.agencyDomain = agencyDomain;
    }
}
