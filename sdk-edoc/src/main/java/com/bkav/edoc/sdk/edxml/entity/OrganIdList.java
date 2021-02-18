package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class OrganIdList {
    private List<String> organId;

    public OrganIdList() {
    }

    public List<String> getOrganId() {
        return this.organId;
    }

    public void setOrganId(List<String> organId) {
        this.organId = organId;
    }

    public void addOrganId(String organId) {
        if (this.organId == null) {
            this.organId = new ArrayList<>();
        }

        this.organId.add(organId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganIdList", this.organId).toString();
    }
}
