package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class ReplacementInfoList extends CommonElement implements IElement<ReplacementInfoList> {
    private List<ReplacementInfo> replacementInfo;

    public ReplacementInfoList() {
    }

    public List<ReplacementInfo> getReplacementInfo() {
        return this.replacementInfo;
    }

    public void setReplacementInfo(List<ReplacementInfo> replacementInfo) {
        this.replacementInfo = replacementInfo;
    }

    public void addReplacementInfo(ReplacementInfo replacementInfo) {
        if (this.replacementInfo == null) {
            this.replacementInfo = new ArrayList<>();
        }
        this.replacementInfo.add(replacementInfo);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("ReplacementInfo", this.replacementInfo).toString();
    }

    @Override
    public void createElement(Element element) {
        Element replacementInfoList = this.createElement(element, "ReplacementInfoList");
        if (this.replacementInfo != null && !this.replacementInfo.isEmpty()) {
            for (ReplacementInfo replacementInfo : this.replacementInfo) {
                replacementInfo.createElement(replacementInfoList);
            }

        }
    }

    @Override
    public ReplacementInfoList getData(Element element) {
        ReplacementInfoList replacementInfoList = new ReplacementInfoList();
        List<Element> childrenElement = element.getChildren();
        if (childrenElement != null && childrenElement.size() != 0) {
            for (Element children : childrenElement) {
                if ("ReplacementInfo".equals(children.getName())) {
                    replacementInfoList.addReplacementInfo(new ReplacementInfo().getData(children));
                }
            }
        }
        return replacementInfoList;
    }
}
