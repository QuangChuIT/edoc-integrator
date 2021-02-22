package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

public class StaffInfo {

    private String department;
    private String departmentId;
    private String staff;
    private String staffId;
    private String email;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Department", this.department)
                .add("DepartmentId", this.departmentId)
                .add("StaffId", this.staffId).add("Staff", this.staff)
                .add("Email", this.email).add("Mobile", this.mobile).toString();
    }
}
