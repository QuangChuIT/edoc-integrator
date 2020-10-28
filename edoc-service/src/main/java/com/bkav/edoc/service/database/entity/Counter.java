package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class Counter implements Serializable {
    private String name;
    private long currentId;

    public Counter() {
    }

    public Counter(String name, long currentId) {
        this.name = name;
        this.currentId = currentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
    }
}
