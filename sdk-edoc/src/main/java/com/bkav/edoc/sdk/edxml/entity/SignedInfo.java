package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SignedInfo {
    private String canonicalizationMethod = "http://www.w3.org/2001/10/xml-exc-c14n#";
    private String signatureMethod = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
    private List<SignReference> reference;

    public SignedInfo() {
    }

    public SignedInfo(String canonicalizationMethod, String signatureMethod, List<SignReference> reference) {
        this.canonicalizationMethod = canonicalizationMethod;
        this.signatureMethod = signatureMethod;
        this.reference = reference;
    }

    public String getCanonicalizationMethod() {
        return this.canonicalizationMethod;
    }

    public void setCanonicalizationMethod(String canonicalizationMethod) {
        this.canonicalizationMethod = canonicalizationMethod;
    }

    public String getSignatureMethod() {
        return this.signatureMethod;
    }

    public void setSignatureMethod(String signatureMethod) {
        this.signatureMethod = signatureMethod;
    }

    public List<SignReference> getReference() {
        return this.reference;
    }

    public void setReference(List<SignReference> reference) {
        this.reference = reference;
    }

    public void addReference(SignReference signReference) {
        if (this.reference == null) {
            this.reference = new ArrayList<>();
        }

        this.reference.add(signReference);
    }


    public static SignedInfo getData(Element element) {
        SignedInfo signedInfo = new SignedInfo();
        List<Element> childrenElement = element.getChildren();
        if (childrenElement != null && childrenElement.size() != 0) {
            for (Element children : childrenElement) {
                if ("CanonicalizationMethod".equals(children.getName())) {
                    signedInfo.setCanonicalizationMethod(EdxmlUtils.getAttributeWithPrefix(children, "Algorithm"));
                }

                if ("SignatureMethod".equals(children.getName())) {
                    signedInfo.setSignatureMethod(EdxmlUtils.getAttributeWithPrefix(children, "Algorithm"));
                }

                if ("Reference".equals(children.getName())) {
                    signedInfo.addReference(SignReference.getData(children));
                }
            }
        }
        return signedInfo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("CanonicalizationMethod Algorithm",
                this.canonicalizationMethod).add("SignatureMethod Algorithm",
                this.signatureMethod).add("Reference", this.reference).toString();
    }

}
