package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class EdocPriority implements Serializable {
    private Integer priorityId;
    private String value;

    public EdocPriority() {
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
