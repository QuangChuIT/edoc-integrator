package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

import java.util.Date;

public class PromulgationInfo {
    private String place;
    private Date promulgationDate;
    private String promulgationDateValue;

    public PromulgationInfo() {
    }

    public PromulgationInfo(String place, Date promulgationDate) {
        this.place = place;
        this.promulgationDate = promulgationDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getPromulgationDate() {
        return promulgationDate;
    }

    public void setPromulgationDate(Date promulgationDate) {
        this.promulgationDate = promulgationDate;
    }

    public String getPromulgationDateValue() {
        return promulgationDateValue;
    }

    public void setPromulgationDateValue(String promulgationDateValue) {
        this.promulgationDateValue = promulgationDateValue;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("Place", this.place).add("PromulgationDate", this.promulgationDate).toString();
    }

}
