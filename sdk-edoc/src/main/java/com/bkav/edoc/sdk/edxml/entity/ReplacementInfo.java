package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class ReplacementInfo extends CommonElement implements IElement<ReplacementInfo> {
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


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("DocumentId", this.documentId).add("OrganIdList", this.organIdList).toString();
    }

    @Override
    public void createElement(Element element) {
        Element replacementInfo = this.createElement(element, "ReplacementInfo");
        this.createElement(replacementInfo, "DocumentId", this.documentId);
        this.createElement(replacementInfo, this.organIdList, "OrganIdList");
    }

    @Override
    public ReplacementInfo getData(Element element) {
        ReplacementInfo replacementInfo = new ReplacementInfo();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {
            for (Element thisElement : elements) {
                if ("DocumentId".equals(thisElement.getName())) {
                    replacementInfo.setDocumentId(thisElement.getText());
                }

                if ("OrganIdList".equals(thisElement.getName())) {
                    replacementInfo.setOrganIdList(new OrganIdList().getData(thisElement));
                }
            }
        }
        return replacementInfo;
    }
}
