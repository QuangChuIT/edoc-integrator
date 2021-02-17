package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.Strings;
import org.jdom2.Element;

public class Code extends CommonElement implements IElement<Code> {
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

    @Override
    public void createElement(Element element) {
        Element code = this.createElement(element, "Code");
        this.createElement(code, "CodeNumber", this.codeNumber);
        this.createElement(code, "CodeNotation", this.codeNotation);
    }

    @Override
    public Code getData(Element element) {
        return new Code(EdxmlUtils.getString(element, "CodeNumber"), EdxmlUtils.getString(element, "CodeNotation"));
    }
}
