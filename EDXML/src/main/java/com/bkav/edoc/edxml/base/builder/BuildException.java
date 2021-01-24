package com.bkav.edoc.edxml.base.builder;

public class BuildException extends Exception {

    public BuildException(String message) {
        super(message);
    }

    public BuildException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
