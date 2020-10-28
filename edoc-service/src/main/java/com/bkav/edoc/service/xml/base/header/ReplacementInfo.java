package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jdom2.Element;

import java.util.Iterator;
import java.util.List;

public class ReplacementInfo extends BaseElement {
    private String documentId;
    private OrganIdList organIdList;

    public ReplacementInfo() {
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public OrganIdList getOrganIdList() {
        return this.organIdList;
    }

    public void setOrganIdList(OrganIdList organIdList) {
        this.organIdList = organIdList;
    }

    public ReplacementInfo(String documentId, OrganIdList organIdList) {
        this.documentId = documentId;
        this.organIdList = organIdList;
    }

    public static ReplacementInfo fromContent(Element element) {
        ReplacementInfo replacementInfo = new ReplacementInfo();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {
            for (Element thisElement : elements) {
                if ("DocumentId".equals(thisElement.getName())) {
                    replacementInfo.setDocumentId(thisElement.getText());
                }

                if ("OrganIdList".equals(thisElement.getName())) {
                    replacementInfo.setOrganIdList(OrganIdList.fromContent(thisElement));
                }
            }
        }
        return replacementInfo;
    }

    public void accumulate(Element element) {
        Element replacementInfo = this.accumulate(element, "ReplacementInfo");
        this.accumulate(replacementInfo, "DocumentId", this.documentId);
        this.accumulate(replacementInfo, this.organIdList, "OrganIdList");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("DocumentId", this.documentId).add("OrganIdList", this.organIdList).toString();
    }
}
