package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jdom2.Element;

public class SignerInfo extends BaseElement {
    private String competence;
    private String position;
    private String fullName;

    public SignerInfo() {
    }

    public SignerInfo(String competence, String position, String fullName) {
        this.competence = competence;
        this.position = position;
        this.fullName = fullName;
    }

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static SignerInfo fromContent(Element element) {
        return new SignerInfo(BaseXmlUtils.getString(element, "Competence"),
                BaseXmlUtils.getString(element, "Position"),
                BaseXmlUtils.getString(element, "FullName"));
    }

    public void accumulate(Element elementNode) {
        Element signerInfo = this.accumulate(elementNode, "SignerInfo");
        this.accumulate(signerInfo, "Competence", this.competence);
        this.accumulate(signerInfo, "Position", this.position);
        this.accumulate(signerInfo, "FullName", this.fullName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Competence", this.competence).add("Position", this.position).add("FullName", this.fullName).toString();
    }
}
