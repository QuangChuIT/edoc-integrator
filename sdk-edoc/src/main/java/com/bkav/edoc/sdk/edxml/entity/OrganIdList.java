package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class OrganIdList extends CommonElement implements IElement<OrganIdList> {
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

    @Override
    public void createElement(Element element) {
        for (String organId : this.organId) {
            this.createElement(element, "OrganId", organId);
        }
    }

    @Override
    public OrganIdList getData(Element element) {
        OrganIdList organIdList = new OrganIdList();
        List<Element> localList = element.getChildren();
        if (localList != null && localList.size() != 0) {
            for (Element item : localList) {
                if ("OrganId".equals(item.getName())) {
                    organIdList.addOrganId(item.getText());
                }
            }

        }
        return organIdList;
    }
}
