package com.bkav.edoc.service.database.entity;

import java.util.Date;

public class Role {
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
}
