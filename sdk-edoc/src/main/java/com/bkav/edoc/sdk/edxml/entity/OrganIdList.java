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

    public static OrganIdList getData(Element fromElement) {
        OrganIdList organIdList = new OrganIdList();
        List<Element> localList = fromElement.getChildren();
        if (localList != null && localList.size() != 0) {

            for (Element element : localList) {
                if ("OrganId".equals(element.getName())) {
                    organIdList.addOrganId(element.getText());
                }
            }

        }
        return organIdList;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganIdList", this.organId).toString();
    }
}
