package com.bkav.edoc.converter.entity;

import java.io.Serializable;

public class ParentDynamiccontact implements Serializable {
    private String name;
    private String domain;
    private String parentDomain;

    public ParentDynamiccontact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getParentDomain() {
        return parentDomain;
    }

    public void setParentDomain(String parentDomain) {
        this.parentDomain = parentDomain;
    }
}
