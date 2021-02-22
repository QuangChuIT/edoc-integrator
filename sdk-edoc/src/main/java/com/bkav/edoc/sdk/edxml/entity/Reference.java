package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

public class Reference {
    private String contentId;
    private String contentType;
    private String attachmentName;
    private String description;

    public Reference() {
    }

    public Reference(String contentId, String contentType, String attachmentName, String description) {
        this.contentId = contentId;
        this.contentType = contentType;
        this.attachmentName = attachmentName;
        this.description = description;
    }

    public String getContentId() {
        return this.contentId;
    }

    public void setContentId(String paramString) {
        this.contentId = paramString;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String paramString) {
        this.contentType = paramString;
    }

    public String getAttachmentName() {
        return this.attachmentName;
    }

    public void setAttachmentName(String paramString) {
        this.attachmentName = paramString;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("contentId", this.contentId).add("contentType", this.contentType)
                .add("attachmentName", this.attachmentName).add("Description", this.description).toString();
    }

}
