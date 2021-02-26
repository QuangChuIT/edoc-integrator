package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class ReplacementInfo {
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

    public static ReplacementInfo getData(Element element) {
        ReplacementInfo replacementInfo = new ReplacementInfo();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {
            for (Element thisElement : elements) {
                if ("DocumentId".equals(thisElement.getName())) {
                    replacementInfo.setDocumentId(thisElement.getText());
                }

                if ("OrganIdList".equals(thisElement.getName())) {
                    replacementInfo.setOrganIdList(OrganIdList.getData(thisElement));
                }
            }
        }
        return replacementInfo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("DocumentId", this.documentId).add("OrganIdList", this.organIdList).toString();
    }

}
