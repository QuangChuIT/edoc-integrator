package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SignedInfo extends BaseElement {
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

    public static SignedInfo fromContent(Element element) {
        SignedInfo signedInfo = new SignedInfo();
        List<Element> childrenElement = element.getChildren();
        if (childrenElement != null && childrenElement.size() != 0) {
            for (Element children : childrenElement) {
                if ("CanonicalizationMethod".equals(children.getName())) {
                    signedInfo.setCanonicalizationMethod(getAttributeWithPrefix(children, "Algorithm"));
                }

                if ("SignatureMethod".equals(children.getName())) {
                    signedInfo.setSignatureMethod(getAttributeWithPrefix(children, "Algorithm"));
                }

                if ("Reference".equals(children.getName())) {
                    signedInfo.addReference(SignReference.fromContent(children));
                }
            }
        }
        return signedInfo;
    }

    public void accumulate(Element element) {
        Element signedInfo = this.accumulateWithoutPrefix(element, "SignedInfo");
        Element canonicalizationMethod = this.accumulateWithoutPrefix(signedInfo, "CanonicalizationMethod");
        this.accumulateAttWithoutPrefix(canonicalizationMethod, "Algorithm", this.canonicalizationMethod);
        Element signatureMethod = this.accumulateWithoutPrefix(signedInfo, "SignatureMethod");
        this.accumulateAttWithoutPrefix(signatureMethod, "Algorithm", this.signatureMethod);
        for (SignReference signReference : this.reference) {
            this.accumulateWithoutPrefix(signedInfo, signReference, "Reference");
        }

    }

    private void accumulateWithoutPrefix(Element element, BaseElement baseElement, String name) {
        if (baseElement != null) {
            if (!Strings.isNullOrEmpty(name)) {
                element = this.accumulateWithoutPrefix(element, name);
            }

            baseElement.accumulate(element);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("CanonicalizationMethod Algorithm",
                this.canonicalizationMethod).add("SignatureMethod Algorithm",
                this.signatureMethod).add("Reference", this.reference).toString();
    }
}
