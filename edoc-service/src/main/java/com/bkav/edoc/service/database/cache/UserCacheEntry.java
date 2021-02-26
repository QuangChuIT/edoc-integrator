package com.bkav.edoc.service.database.cache;


import java.io.Serializable;
import java.util.Date;

public class UserCacheEntry implements Serializable {
    private long userId;
    private String emailAddress;
    private String username;
    private OrganizationCacheEntry organization;
    private boolean status;
    private String displayName;
    private String lastLoginIP;
    private Date lastLoginDate;
    private boolean isOnSso;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public OrganizationCacheEntry getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationCacheEntry organization) {
        this.organization = organization;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean getOnSso() {
        return isOnSso;
    }

    public void setOnSso(boolean onSso) {
        isOnSso = onSso;
    }
}
