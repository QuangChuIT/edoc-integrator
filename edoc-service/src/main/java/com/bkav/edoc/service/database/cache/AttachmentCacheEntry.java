package com.bkav.edoc.service.database.cache;

import java.io.Serializable;
import java.util.Date;

public class AttachmentCacheEntry implements Serializable {
    private Long attachmentId;
    private String organDomain;
    private String name;
    private Date createDate;
    private String fullPath;
    private String relativePath;
    private String fileType;
    private String size;
    private String toOrganDomain;
    private long documentId;

    public AttachmentCacheEntry() {
    }

    public AttachmentCacheEntry(Long attachmentId, String organDomain, String name,
                                Date createDate, String fullPath, String fileType, String size, String toOrganDomain, long documentId) {
        this.attachmentId = attachmentId;
        this.organDomain = organDomain;
        this.name = name;
        this.createDate = createDate;
        this.fullPath = fullPath;
        this.fileType = fileType;
        this.size = size;
        this.toOrganDomain = toOrganDomain;
        this.documentId = documentId;
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

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }
}
