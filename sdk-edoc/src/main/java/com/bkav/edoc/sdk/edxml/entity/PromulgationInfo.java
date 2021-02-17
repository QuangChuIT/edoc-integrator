package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.DateUtils;
import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.Date;

public class PromulgationInfo extends CommonElement implements IElement<PromulgationInfo> {
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

    @Override
    public void createElement(Element element) {
        Element promulgationInfoElement = this.createElement(element, "PromulgationInfo");
        this.createElement(promulgationInfoElement, "Place", this.place);
        this.createElement(promulgationInfoElement, "PromulgationDate", DateUtils.format(this.promulgationDate));
    }

    @Override
    public PromulgationInfo getData(Element element) {
        PromulgationInfo promulgationInfo = new PromulgationInfo();
        promulgationInfo.setPlace(EdxmlUtils.getString(element, "Place"));
        promulgationInfo.setPromulgationDateValue(EdxmlUtils.getString(element, "PromulgationDate"));
        promulgationInfo.setPromulgationDate(DateUtils.parse(EdxmlUtils.getString(element, "PromulgationDate"), DateUtils.DEFAULT_DATE_FORMAT));
        return promulgationInfo;
    }
}
