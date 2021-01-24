package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class Agency extends BaseElement {
    private String organId;
    private String organName;
    private String departmentId;
    private String departmentName;
    private int executeRole = 1;

    public Agency() {
    }

    public Agency(String organId, String organName, String departmentName, int executeRole) {
        this.organId = organId;
        this.organName = organName;
        this.departmentName = departmentName;
        this.executeRole = executeRole;
    }

    public Agency(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt) {
        this.organId = paramString1;
        this.organName = paramString2;
        this.departmentName = paramString4;
        this.departmentId = paramString3;
        this.executeRole = paramInt;
    }

    public String getOrganId() {
        return this.organId;
    }

    public void setOrganId(String paramString) {
        this.organId = paramString;
    }

    public String getOrganName() {
        return this.organName;
    }

    public void setOrganName(String paramString) {
        this.organName = paramString;
    }

    public String getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(String paramString) {
        this.departmentId = paramString;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public void setDepartmentName(String paramString) {
        this.departmentName = paramString;
    }

    public int getExecuteRole() {
        return this.executeRole;
    }

    public void setExecuteRole(int paramInt) {
        this.executeRole = paramInt;
    }

    public void accumulate(Element element) {
        Element agency = this.accumulate(element, "Agency");
        this.accumulate(agency, "OrganId", this.organId);
        this.accumulate(agency, "OrganName", this.organName);
        this.accumulate(agency, "DepartmentId", this.departmentId);
        this.accumulate(agency, "DepartmentName", this.departmentName);
        this.accumulate(agency, "ExecuteRole", this.executeRole);
    }

    public static Agency fromContent(Element element) {
        List<Element> childrenElement = element.getChildren();
        if (childrenElement != null && childrenElement.size() != 0) {
            Agency agency = new Agency();
            for (Element children : childrenElement) {
                if ("OrganId".equals(children.getName())) {
                    agency.setOrganId(children.getTextTrim());
                }
                if ("OrganName".equals(children.getName())) {
                    agency.setOrganName(children.getTextTrim());
                }
                if ("DepartmentId".equals(children.getName())) {
                    agency.setDepartmentId(children.getTextTrim());
                }
                if ("DepartmentName".equals(children.getName())) {
                    agency.setDepartmentName(children.getTextTrim());
                }
                if ("ExecuteRole".equals(children.getName())) {
                    agency.setExecuteRole(Integer.parseInt(children.getTextTrim()));
                }
            }
            return agency;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass())
                .add("organId", this.organId)
                .add("OrganName", this.organName)
                .add("DepartmentId", this.departmentId)
                .add("DepartmentName", this.departmentName)
                .add("ExecuteRole", this.executeRole).toString();
    }
}
