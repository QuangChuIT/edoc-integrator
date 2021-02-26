/**
 *
 */
package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.resource.StringPool;
import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ErrorList", namespace = StringPool.TARGET_NAMESPACE)

@XmlType(name = "ErrorList", propOrder = {"errors"})

@XmlAccessorType(XmlAccessType.FIELD)

public class ErrorList {

    @XmlElement(name = "Error")
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
        this.errors = errors == null ? new ArrayList<Error>() : errors;
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
        this.errors = new ArrayList<Error>();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Errors", this.errors).toString();
    }
}
