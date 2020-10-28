package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class CheckPermission extends BaseElement {
    private String organId;
    private String token;

    public CheckPermission() {
    }

    public CheckPermission(String organId, String token) {
        this.organId = organId;
        this.token = token;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static CheckPermission fromContent(Element rootElement) {
        CheckPermission checkPermission = new CheckPermission();
        List<Element> childElements = rootElement.getChildren();
        if (childElements != null && childElements.size() > 0) {
            for (Element childElement : childElements) {
                if ("OrganId".equals(childElement.getName())) {
                    checkPermission.setOrganId(childElement.getText());
                }

                if ("Token".equals(childElement.getName())) {
                    checkPermission.setToken(childElement.getText());
                }
            }
            return checkPermission;
        }
        return null;
    }

    @Override
    public void accumulate(Element parentElement) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId", this.organId).add("Token", this.token).toString();
    }
}
