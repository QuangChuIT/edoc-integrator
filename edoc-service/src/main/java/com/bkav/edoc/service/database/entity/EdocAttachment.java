package com.bkav.edoc.service.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class EdocAttachment implements Serializable, Cloneable {

    private Long attachmentId;
    private String organDomain;
    private String name;
    private Date createDate;
    private String fullPath;
    private String type;
    private String size;
    private String toOrganDomain;
    @JsonIgnore
    private EdocDocument document;

    public EdocAttachment() {
    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getOrganDomain() {
        return organDomain;
    }

    public void setOrganDomain(String organDomain) {
        this.organDomain = organDomain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getToOrganDomain() {
        return toOrganDomain;
    }

    public void setToOrganDomain(String toOrganDomain) {
        this.toOrganDomain = toOrganDomain;
    }

    public EdocDocument getDocument() {
        return document;
    }

    public void setDocument(EdocDocument document) {
        this.document = document;
    }

    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        EdocAttachment obj = new EdocAttachment();
        obj.setOrganDomain(this.organDomain);
        obj.setToOrganDomain(this.toOrganDomain);
        obj.setDocument(this.document);
        obj.setType(this.type);
        obj.setSize(this.size);
        obj.setName(this.name);
        obj.setFullPath(this.fullPath);
        obj.setCreateDate(this.createDate);
        return obj;
    }
}
