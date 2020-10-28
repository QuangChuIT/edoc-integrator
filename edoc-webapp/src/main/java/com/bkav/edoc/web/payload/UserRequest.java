package com.bkav.edoc.web.payload;

public class UserRequest {
    private long userId;
    private boolean status;

    public UserRequest(long userId, boolean status) {
        this.userId = userId;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
