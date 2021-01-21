package com.bkav.edoc.converter.entity;

import java.io.Serializable;

public class EdocStatDetail implements Serializable {
    private long edocStatDetailId;
    private String organDomain;
    private int total;
    private int received_int;
    private int received_ext;
    private int signed;
    private int not_signed;

    public EdocStatDetail() {
    }

    public long getEdocStatDetailId() {
        return edocStatDetailId;
    }

    public void setEdocStatDetailId(long edocStatDetailId) {
        this.edocStatDetailId = edocStatDetailId;
    }

    public String getOrganDomain() {
        return organDomain;
    }

    public void setOrganDomain(String organDomain) {
        this.organDomain = organDomain;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getReceived_int() {
        return received_int;
    }

    public void setReceived_int(int received_int) {
        this.received_int = received_int;
    }

    public int getReceived_ext() {
        return received_ext;
    }

    public void setReceived_ext(int received_ext) {
        this.received_ext = received_ext;
    }

    public int getSigned() {
        return signed;
    }

    public void setSigned(int signed) {
        this.signed = signed;
    }

    public int getNot_signed() {
        return not_signed;
    }

    public void setNot_signed(int not_signed) {
        this.not_signed = not_signed;
    }
}
