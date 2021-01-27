/**
 *
 */
package com.bkav.edoc.edxml.base.header;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class ErrorList {

    private List<java.lang.Error> errors;

    /**
     * @return the errors
     */
    public List<java.lang.Error> getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(List<java.lang.Error> errors) {
        this.errors = errors == null ? new ArrayList<java.lang.Error>() : errors;
    }

    /**
     * @param errors
     */
    public ErrorList(List<java.lang.Error> errors) {
        super();
        this.errors = errors;
    }

    /**
     *
     */
    public ErrorList() {
        super();
        this.errors = new ArrayList<java.lang.Error>();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Errors", this.errors).toString();
    }
}
