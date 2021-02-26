package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Organization", namespace = StringPool.TARGET_NAMESPACE)
@XmlType(name = "Organization", propOrder = {"organId", "organizationInCharge", "organName", "organAdd", "email", "telephone", "fax", "website"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Organization extends BaseElement {

    @XmlElement(name = "OrganId")
    private String organId;
    @XmlElement(name = "OrganizationInCharge")
    private String organizationInCharge;
    @XmlElement(name = "OrganName")
    private String organName;
    @XmlElement(name = "OrganAdd")
    private String organAdd;
    @XmlElement(name = "Email")
    private String email;
    @XmlElement(name = "Telephone")
    private String telephone;
    @XmlElement(name = "Fax")
    private String fax;
    @XmlElement(name = "Website")
    private String website;

    public Organization() {
    }

    public Organization(String organId, String organName, String email) {
        this.organId = organId;
        this.organName = organName;
        this.email = email;
    }

    public Organization(String organId, String organizationInCharge, String organName, String organAdd, String email, String telephone, String fax, String website) {
        this.organId = organId;
        this.organizationInCharge = organizationInCharge;
        this.organName = organName;
        this.organAdd = organAdd;
        this.email = email;
        this.telephone = telephone;
        this.fax = fax;
        this.website = website;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganizationInCharge() {
        return organizationInCharge;
    }

    public void setOrganizationInCharge(String organizationInCharge) {
        this.organizationInCharge = organizationInCharge;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganAdd() {
        return organAdd;
    }

    public void setOrganAdd(String organAdd) {
        this.organAdd = organAdd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public static Organization fromContent(Element element) {
        if (element == null) {
            return null;
        } else {
            List<Element> elements = element.getChildren();
            if (elements != null && elements.size() != 0) {
                Organization organization = new Organization();
                for (Element itemElement : elements) {
                    if ("OrganId".equals(itemElement.getName())) {
                        organization.setOrganId(itemElement.getTextTrim());
                    } else if ("OrganizationInCharge".equals(itemElement.getName())) {
                        organization.setOrganizationInCharge(itemElement.getTextTrim());
                    } else if ("OrganName".equals(itemElement.getName())) {
                        organization.setOrganName(itemElement.getTextTrim());
                    } else if ("OrganAdd".equals(itemElement.getName())) {
                        organization.setOrganAdd(itemElement.getTextTrim());
                    } else if ("Email".equals(itemElement.getName())) {
                        organization.setEmail(itemElement.getTextTrim());
                    } else if ("Telephone".equals(itemElement.getName())) {
                        organization.setTelephone(itemElement.getTextTrim());
                    } else if ("Fax".equals(itemElement.getName())) {
                        organization.setFax(itemElement.getTextTrim());
                    } else if ("Website".equals(itemElement.getName())) {
                        organization.setWebsite(itemElement.getTextTrim());
                    }
                }
                return organization;
            } else {
                return null;
            }
        }
    }

    public void accumulate(Element element) {
        this.accumulate(element, "OrganId", this.organId);
        this.accumulate(element, "OrganizationInCharge", this.organizationInCharge);
        this.accumulate(element, "OrganName", this.organName);
        this.accumulate(element, "OrganAdd", this.organAdd);
        this.accumulate(element, "Email", this.email);
        this.accumulate(element, "Telephone", this.telephone);
        this.accumulate(element, "Fax", this.fax);
        this.accumulate(element, "Website", this.website);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId", this.organId)
                .add("OrganizationInCharge", this.organizationInCharge)
                .add("OrganName", this.organName)
                .add("OrganAdd", this.organAdd)
                .add("Email", this.email).add("Telephone", this.telephone)
                .add("Fax", this.fax).add("Website", this.website).toString();
    }
}
