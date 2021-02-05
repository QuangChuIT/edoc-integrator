package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class EdocStatisticDetail implements Serializable {
    private long edocStatDetailId;
    private String organDomain;
    private int total_received;
    private int total_sent;
    private int received_int;
    private int received_ext;
    private int sent_int;
    private int sent_ext;
    private int signed;
    private int not_signed;

    public EdocStatisticDetail() {
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

    public int getTotal_received() {
        return total_received;
    }

    public void setTotal_received(int total_received) {
        this.total_received = total_received;
    }

    public int getTotal_sent() {
        return total_sent;
    }

    public void setTotal_sent(int total_sent) {
        this.total_sent = total_sent;
    }

    public int getSent_int() {
        return sent_int;
    }

    public void setSent_int(int sent_int) {
        this.sent_int = sent_int;
    }

    public int getSent_ext() {
        return sent_ext;
    }

    public void setSent_ext(int sent_ext) {
        this.sent_ext = sent_ext;
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
