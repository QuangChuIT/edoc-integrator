/**
 *
 */
package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.resource.StringPool;
import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.*;


/**
 * @author QuangCV
 *
 */
@XmlRootElement(name = "Error", namespace = StringPool.TARGET_NAMESPACE)
@XmlType(name = "Error", propOrder = {"code", "description"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {
    /**
     * @param code
     * @param description
     */
    public Error(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     *
     */
    public Error() {
        this.code = "";
        this.description = "";
    }

    @XmlElement(name = "Code")
    String code;

    @XmlElement(name = "Description")
    String description;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Code", this.code).
                add("Description", this.description).toString();
    }
}
