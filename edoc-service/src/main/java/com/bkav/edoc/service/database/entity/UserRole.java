package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class UserRole implements Serializable {
    private long id;
    private User user;
    private Role role;

    public UserRole() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
