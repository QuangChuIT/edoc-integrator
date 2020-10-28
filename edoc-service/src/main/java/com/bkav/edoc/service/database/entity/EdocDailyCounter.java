package com.bkav.edoc.service.database.entity;

import java.io.Serializable;
import java.util.Date;

public class EdocDailyCounter implements Serializable {
    private Long dailyCounterId;
    private String organDomain;
    private Date dateTime;
    private int sent;
    private int received;

    public EdocDailyCounter() {
    }

    public Long getDailyCounterId() {
        return dailyCounterId;
    }

    public void setDailyCounterId(Long dailyCounterId) {
        this.dailyCounterId = dailyCounterId;
    }

    public String getOrganDomain() {
        return organDomain;
    }

    public void setOrganDomain(String organDomain) {
        this.organDomain = organDomain;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }
}
