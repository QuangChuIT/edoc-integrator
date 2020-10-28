package com.bkav.edoc.service.xml.base.builder;

public class BuildException extends Exception {

    public BuildException(String message) {
        super(message);
    }

    public BuildException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
