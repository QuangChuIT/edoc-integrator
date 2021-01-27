package com.bkav.edoc.service.database.cache;

import java.io.Serializable;

public class OrganizationCacheEntry implements Serializable {
    private Long id;
    private String name;
    private String inCharge;
    private String domain;
    private String email;
    private String address;
    private String telephone;
    private String token;
    private boolean status;
    private Long parent;
    private boolean isAgency;
    private boolean receivedNotify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInCharge() {
        return inCharge;
    }

    public void setInCharge(String inCharge) {
        this.inCharge = inCharge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean getAgency() {
        return isAgency;
    }

    public void setAgency(boolean agency) {
        isAgency = agency;
    }

    public boolean getReceivedNotify() {
        return receivedNotify;
    }

    public void setReceivedNotify(boolean receivedNotify) {
        this.receivedNotify = receivedNotify;
    }
}
