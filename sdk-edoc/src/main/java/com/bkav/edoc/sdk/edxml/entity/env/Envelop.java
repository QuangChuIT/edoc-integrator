package com.bkav.edoc.sdk.edxml.entity.env;

import com.bkav.edoc.sdk.edxml.entity.Attachment;
import com.google.common.base.MoreObjects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Envelop {
    private Header header;
    private Body body;
    private List<Attachment> attachments;

    public Envelop(Header header, Body body) {
        this.header = header;
        this.body = body;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }

        this.attachments.add(attachment);
    }

    public void clean() {
        if (this.getAttachments() != null && this.getAttachments().size() > 0) {
            Attachment attachment = this.getAttachments().get(0);
            if (attachment.getContent() != null) {
                this.deleteFolder(attachment.getContent().getParentFile());
            }
        }
    }

    private void deleteFolder(File file) {
        if (file.isDirectory()) {
            File[] arrFiles = file.listFiles();
            if (arrFiles != null && arrFiles.length > 0) {
                for (File localFile : arrFiles) {
                    this.deleteFolder(localFile);
                }
            }
        }
        file.delete();
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("header",
                this.header).add("attachments", this.attachments).toString();
    }
}
