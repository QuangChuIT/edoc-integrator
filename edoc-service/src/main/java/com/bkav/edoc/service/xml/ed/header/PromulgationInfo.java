package com.bkav.edoc.service.xml.ed.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.Date;

public class PromulgationInfo extends BaseElement {
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

    public static PromulgationInfo fromContent(Element element) {
        PromulgationInfo promulgationInfo = new PromulgationInfo();
        promulgationInfo.setPlace(BaseXmlUtils.getString(element, "Place"));
        promulgationInfo.setPromulgationDateValue(BaseXmlUtils.getString(element, "PromulgationDate"));
        promulgationInfo.setPromulgationDate(DateUtils.parse(BaseXmlUtils.getString(element, "PromulgationDate"), DateUtils.DEFAULT_DATE_FORMAT));
        return promulgationInfo;

    }

    @Override
    public void accumulate(Element element) {
        Element thisElement = this.accumulate(element, "PromulgationInfo");
        this.accumulate(thisElement, "Place", this.place);
        this.accumulate(thisElement, "PromulgationDate", DateUtils.format(this.promulgationDate));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("Place", this.place).add("PromulgationDate", this.promulgationDate).toString();
    }
}
