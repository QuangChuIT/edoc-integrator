package com.bkav.edoc.edxml.base;

import com.bkav.edoc.edxml.base.attachment.Attachment;
import com.bkav.edoc.edxml.base.body.Body;
import com.bkav.edoc.edxml.base.header.Header;
import com.google.common.base.MoreObjects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Base {
    private Header header;
    private Body body;
    private List<Attachment> attachments = new ArrayList<>();

    public Base() {
    }

    public Base(Header header, List<Attachment> attachments) {
        this.header = header;
        this.attachments = attachments;
    }

    public Base(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public List<Attachment> getAttachments() {
        return this.attachments;
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

    public Body getBody() {
        return this.body;
    }

    public void setBody(Body body) {
        this.body = body;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("header",
                this.header).add("attachments", this.attachments).toString();
    }
}
