package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class OtherInfo {
    private int priority;
    private String sphereOfPromulgation;
    private String typerNotation;
    private int promulgationAmount;
    private int pageAmount;
    private List<String> appendixes;
    private String replyFors;
    private String referenceCodes;
    private boolean direction;

    public OtherInfo() {
    }

    public OtherInfo(int priority, String sphereOfPromulgation, String typerNotation, int promulgationAmount, int pageAmount) {
        this.priority = priority;
        this.sphereOfPromulgation = sphereOfPromulgation;
        this.typerNotation = typerNotation;
        this.promulgationAmount = promulgationAmount;
        this.pageAmount = pageAmount;
    }

    public OtherInfo(int priority, String sphereOfPromulgation, String typerNotation, int promulgationAmount, int pageAmount, String replyFors, String referenceCodes, boolean direction) {
        this.priority = priority;
        this.sphereOfPromulgation = sphereOfPromulgation;
        this.typerNotation = typerNotation;
        this.promulgationAmount = promulgationAmount;
        this.pageAmount = pageAmount;
        this.replyFors = replyFors;
        this.referenceCodes = referenceCodes;
        this.direction = direction;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSphereOfPromulgation() {
        return sphereOfPromulgation;
    }

    public void setSphereOfPromulgation(String sphereOfPromulgation) {
        this.sphereOfPromulgation = sphereOfPromulgation;
    }

    public String getTyperNotation() {
        return typerNotation;
    }

    public void setTyperNotation(String typerNotation) {
        this.typerNotation = typerNotation;
    }

    public int getPromulgationAmount() {
        return promulgationAmount;
    }

    public void setPromulgationAmount(int promulgationAmount) {
        this.promulgationAmount = promulgationAmount;
    }

    public int getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(int pageAmount) {
        this.pageAmount = pageAmount;
    }

    public List<String> getAppendixes() {
        return appendixes;
    }

    public void setAppendixes(List<String> appendixes) {
        this.appendixes = appendixes;
    }

    public String getReplyFors() {
        return replyFors;
    }

    public void setReplyFors(String replyFors) {
        this.replyFors = replyFors;
    }

    public String getReferenceCodes() {
        return referenceCodes;
    }

    public void setReferenceCodes(String referenceCodes) {
        this.referenceCodes = referenceCodes;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public static OtherInfo getData(Element otherInfoElement) {
        OtherInfo otherInfo = new OtherInfo(EdxmlUtils.getInt(otherInfoElement, "Priority", 0),
                EdxmlUtils.getString(otherInfoElement, "SphereOfPromulgation"),
                EdxmlUtils.getString(otherInfoElement, "TyperNotation"),
                EdxmlUtils.getInt(otherInfoElement, "PromulgationAmount", 1),
                EdxmlUtils.getInt(otherInfoElement, "PageAmount", 1),
                EdxmlUtils.getString(otherInfoElement, "ReplyFors"),
                EdxmlUtils.getString(otherInfoElement, "ReferenceCodes"),
                EdxmlUtils.getBoolean(otherInfoElement, "Direction", false));
        List<Element> elementList = otherInfoElement.getChildren();
        if (elementList != null && elementList.size() != 0) {
            Element element = null;
            for (Element thisEle : elementList) {
                element = thisEle;
                if ("Appendixes".equals(element.getName())) {
                    List<String> appendixes = new ArrayList<>();
                    List<Element> elements = element.getChildren();

                    for (Element thatEle : elements) {
                        element = thatEle;
                        if ("Appendix".equals(element.getName())) {
                            appendixes.add(element.getText());
                        }
                    }
                    otherInfo.setAppendixes(appendixes);
                    return otherInfo;
                }
            }
        }
        return otherInfo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("Priority", this.priority)
                .add("SphereOfPromulgation", this.sphereOfPromulgation).add("TyperNotation", this.typerNotation)
                .add("PromulgationAmount", this.promulgationAmount).add("PageAmount", this.pageAmount)
                .add("ReplyFors", this.replyFors).add("Direction", this.direction)
                .add("ReferenceCodes", this.referenceCodes).add("Appendixes", this.appendixes).toString();
    }

}
