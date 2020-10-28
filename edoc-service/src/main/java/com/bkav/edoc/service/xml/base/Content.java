package com.bkav.edoc.service.xml.base;

import java.io.File;

public class Content {
    private File content;
    private String hashCode;

    public Content(File paramFile, String paramString) {
        this.content = paramFile;
        this.hashCode = paramString;
    }

    public File getContent() {
        return this.content;
    }

    public void setContent(File paramFile) {
        this.content = paramFile;
    }

    public String getHashCode() {
        return this.hashCode;
    }

    public void setHashCode(String paramString) {
        this.hashCode = paramString;
    }

    public String toString() {
        return "Content{content=" + this.content + ", hashCode='" + this.hashCode + '\'' + '}';
    }
}
