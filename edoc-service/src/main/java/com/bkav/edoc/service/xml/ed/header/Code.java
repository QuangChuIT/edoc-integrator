package com.bkav.edoc.service.xml.ed.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.google.common.base.Strings;
import org.jdom2.Element;

public class Code extends BaseElement {
    private String codeNumber;
    private String codeNotation;

    public Code() {
    }

    public Code(String codeNumber, String codeNotation) {
        this.codeNumber = codeNumber;
        this.codeNotation = codeNotation;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getCodeNotation() {
        return codeNotation;
    }

    public void setCodeNotation(String codeNotation) {
        this.codeNotation = codeNotation;
    }

    public static Code fromContent(Element element) {
        return new Code(BaseXmlUtils.getString(element, "CodeNumber"), BaseXmlUtils.getString(element, "CodeNotation"));
    }

    public void accumulate(Element element) {
        Element code = this.accumulate(element, "Code");
        this.accumulate(code, "CodeNumber", this.codeNumber);
        this.accumulate(code, "CodeNotation", this.codeNotation);
    }

    @Override
    public String toString() {
        if (Strings.isNullOrEmpty(this.codeNumber)) {
            return this.codeNotation;
        } else if (Strings.isNullOrEmpty(this.codeNotation)) {
            return this.codeNumber;
        } else {
            String str = "/" + this.codeNotation;
            return this.codeNumber + str;
        }
    }
}
