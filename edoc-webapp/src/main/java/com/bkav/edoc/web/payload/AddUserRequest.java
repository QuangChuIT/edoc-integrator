package com.bkav.edoc.web.payload;

import java.io.Serializable;
import java.util.List;

public class AddUserRequest implements Serializable {
    private long userId;
    private String displayName;
    private String userName;
    private String emailAddress;
    private List<String> organDomain;
    private String password;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<String> getOrganDomain() {
        return organDomain;
    }

    public void setOrganDomain(List<String> organDomain) {
        this.organDomain = organDomain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
