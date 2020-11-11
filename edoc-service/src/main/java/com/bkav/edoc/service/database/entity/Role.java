package com.bkav.edoc.service.database.entity;

import java.io.Serializable;
import java.util.Date;

public class Role implements Serializable {
    private long roleId;
    private String roleKey;
    private String roleName;
    private String description;
    private boolean active;
    private long createByUserId;
    private Date createDate;
    private long lastModifyByUserId;
    private Date lastModifyOnDate;
    private Date version;

    public Role() {
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getCreateByUserId() {
        return createByUserId;
    }

    public void setCreateByUserId(long createByUserId) {
        this.createByUserId = createByUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getLastModifyByUserId() {
        return lastModifyByUserId;
    }

    public void setLastModifyByUserId(long lastModifyByUserId) {
        this.lastModifyByUserId = lastModifyByUserId;
    }

    public Date getLastModifyOnDate() {
        return lastModifyOnDate;
    }

    public void setLastModifyOnDate(Date lastModifyOnDate) {
        this.lastModifyOnDate = lastModifyOnDate;
    }

    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }
}
