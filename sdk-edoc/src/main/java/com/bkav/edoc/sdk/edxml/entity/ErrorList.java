package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class ErrorList {

    private List<Error> errors;

    /**
     * @return the errors
     */
    public List<Error> getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(List<Error> errors) {
        this.errors = errors == null ? new ArrayList<>() : errors;
    }

    /**
     * @param errors
     */
    public ErrorList(List<Error> errors) {
        super();
        this.errors = errors;
    }

    /**
     *
     */
    public ErrorList() {
        super();
        this.errors = new ArrayList<>();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Errors", this.errors).toString();
    }
}
