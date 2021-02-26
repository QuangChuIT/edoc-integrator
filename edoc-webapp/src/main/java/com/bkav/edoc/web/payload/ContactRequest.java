package com.bkav.edoc.web.payload;

import java.io.Serializable;

public class ContactRequest implements Serializable {
    private long id;
    private String name;
    private String inCharge;
    private String domain;
    private String email;
    private String address;
    private String telephone;
    private boolean agency;
    private boolean receiveNotify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public boolean getAgency() {
        return agency;
    }

    public void setAgency(boolean agency) {
        this.agency = agency;
    }

    public boolean getReceiveNotify() {
        return receiveNotify;
    }

    public void setReceiveNotify(boolean receiveNotify) {
        this.receiveNotify = receiveNotify;
    }
}
