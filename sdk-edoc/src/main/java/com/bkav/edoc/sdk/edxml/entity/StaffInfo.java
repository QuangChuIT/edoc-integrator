package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.xml.base.BaseElement;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "StaffInfo", namespace = StringPool.TARGET_NAMESPACE)
@XmlType(name = "StaffInfo", propOrder = {"department", "departmentId", "staff", "staffId", "email", "mobile"})
@XmlAccessorType(XmlAccessType.FIELD)
public class StaffInfo extends BaseElement {

    @XmlElement(name = "Department")
    private String department;
    @XmlElement(name = "DepartmentId")
    private String departmentId;
    @XmlElement(name = "Staff")
    private String staff;
    @XmlElement(name = "StaffId")
    private String staffId;
    @XmlElement(name = "Email")
    private String email;
    @XmlElement(name = "Mobile")
    private String mobile;

    public StaffInfo() {
    }

    public StaffInfo(String department, String staff) {
        this.department = department;
        this.staff = staff;
    }

    public StaffInfo(String department, String departmentId, String staff, String staffId, String email, String mobile) {
        this.department = department;
        this.departmentId = departmentId;
        this.staff = staff;
        this.staffId = staffId;
        this.email = email;
        this.mobile = mobile;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public static StaffInfo fromNode(Element element) {
        return new StaffInfo(BaseXmlUtils.getString(element, "Department"),
                BaseXmlUtils.getString(element, "DepartmentId"),
                BaseXmlUtils.getString(element, "Staff"), BaseXmlUtils.getString(element, "StaffId"),
                BaseXmlUtils.getString(element, "Email"), BaseXmlUtils.getString(element, "Mobile"));
    }

    public static StaffInfo fromContent(Element paramElement) {
        StaffInfo staffInfo = new StaffInfo();
        List<Element> elementList = paramElement.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element element : elementList) {
                if ("Department".equals(element.getName())) {
                    staffInfo.setDepartment(element.getText());
                } else if ("DepartmentId".equals(element.getName())) {
                    staffInfo.setDepartmentId(element.getText());
                } else if ("Staff".equals(element.getName())) {
                    staffInfo.setStaff(element.getText());
                } else if ("StaffId".equals(element.getName())) {
                    staffInfo.setStaffId(element.getText());
                } else if ("Email".equals(element.getName())) {
                    staffInfo.setEmail(element.getText());
                } else if ("Mobile".equals(element.getName())) {
                    staffInfo.setMobile(element.getText());
                }
            }

        }
        return staffInfo;
    }

    @Override
    public void accumulate(Element element) {
        Element staffInfo = this.accumulate(element, "StaffInfo");
        this.accumulate(staffInfo, "Department", this.department);
        this.accumulate(staffInfo, "DepartmentId", this.departmentId);
        this.accumulate(staffInfo, "Staff", this.staff);
        this.accumulate(staffInfo, "StaffId", this.staffId);
        this.accumulate(staffInfo, "Email", this.email);
        this.accumulate(staffInfo, "Mobile", this.mobile);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Department", this.department)
                .add("DepartmentId", this.departmentId)
                .add("StaffId", this.staffId).add("Staff", this.staff)
                .add("Email", this.email).add("Mobile", this.mobile).toString();
    }
}
