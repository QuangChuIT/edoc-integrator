package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

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


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("DocumentId", this.documentId).add("OrganIdList", this.organIdList).toString();
    }

}
