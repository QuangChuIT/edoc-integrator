package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SignedInfo extends CommonElement implements IElement<SignedInfo> {
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


    }

    private void accumulateWithoutPrefix(Element element, CommonElement commonElement, String name) {
        if (commonElement != null) {
            if (!Strings.isNullOrEmpty(name)) {
                element = this.createWithoutPrefix(element, name);
            }

            commonElement.createElement(element);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("CanonicalizationMethod Algorithm",
                this.canonicalizationMethod).add("SignatureMethod Algorithm",
                this.signatureMethod).add("Reference", this.reference).toString();
    }

    @Override
    public void createElement(Element element) {
        Element signedInfo = this.createElement(element, "SignedInfo");
        Element canonicalizationMethod = this.createWithoutPrefix(signedInfo, "CanonicalizationMethod");
        this.createAttWithoutPrefix(canonicalizationMethod, "Algorithm", this.canonicalizationMethod);
        Element signatureMethod = this.createWithoutPrefix(signedInfo, "SignatureMethod");
        this.createAttWithoutPrefix(signatureMethod, "Algorithm", this.signatureMethod);
        for (SignReference signReference : this.reference) {
            this.accumulateWithoutPrefix(signedInfo, signReference, "Reference");
        }
    }

    @Override
    public SignedInfo getData(Element element) {
        return null;
    }
}
