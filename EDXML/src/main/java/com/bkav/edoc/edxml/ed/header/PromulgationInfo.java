package com.bkav.edoc.edxml.ed.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.bkav.edoc.edxml.base.util.BaseXmlUtils;
import com.bkav.edoc.edxml.base.util.DateUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.Date;

public class PromulgationInfo extends BaseElement {
    private String place;
    private Date promulgationDate;

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

    public static PromulgationInfo fromContent(Element element) {
        return new PromulgationInfo(BaseXmlUtils.getString(element, "Place"),
                DateUtils.parse(BaseXmlUtils.getString(element, "PromulgationDate"), DateUtils.DEFAULT_DATE_FORMAT));
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
