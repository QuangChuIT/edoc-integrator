package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SignReference extends CommonElement implements IElement<SignReference> {
    private String URI;
    private List<String> transforms;
    private String digestMethod;
    private String digestValue;

    public SignReference() {
    }

    public SignReference(String uri, String digestMethod, String digestValue) {
        if (!Strings.isNullOrEmpty(uri)) {
            this.URI = "cid:" + uri;
        }

        this.digestMethod = digestMethod;
        this.digestValue = digestValue;
    }

    public String getURI() {
        return this.URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public List<String> getTransforms() {
        return this.transforms;
    }

    public void setTransforms(List<String> transforms) {
        this.transforms = transforms;
    }

    public void addToTransform(String transform) {
        if (this.transforms == null) {
            this.transforms = new ArrayList<>();
        }

        this.transforms.add(transform);
    }

    public String getDigestMethod() {
        return this.digestMethod;
    }

    public void setDigestMethod(String digestMethod) {
        this.digestMethod = digestMethod;
    }

    public String getDigestValue() {
        return this.digestValue;
    }

    public void setDigestValue(String digestValue) {
        this.digestValue = digestValue;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("URI", this.URI).add("Transforms", this.transforms)
                .add("DigestMethod Algorithm", this.digestMethod).add("DigestValue", this.digestValue).toString();
    }

    @Override
    public void createElement(Element element) {
        element.setAttribute("URI", Strings.nullToEmpty(this.URI));
        Element SignReference;
        if (this.transforms != null && !this.transforms.isEmpty()) {
            SignReference = this.createWithoutPrefix(element, "Transforms");
            for (String str : this.transforms) {
                Element transform = this.createWithoutPrefix(SignReference, "Transform");
                this.createAttWithoutPrefix(transform, "Algorithm", str);
            }
        }
        SignReference = this.createWithoutPrefix(element, "DigestMethod");
        this.createAttWithoutPrefix(SignReference, "Algorithm", this.digestMethod);
    }

    @Override
    public SignReference getData(Element rootElement) {
        SignReference signReference = new SignReference();
        signReference.setURI(this.getAttributeWithPrefix(rootElement, "URI"));
        List<Element> elementList = rootElement.getChildren();
        if (elementList != null && elementList.size() != 0) {
            Element element;
            for (Element thisElement : elementList) {
                element = thisElement;
                if ("Transforms".equals(element.getName())) {
                    List<Element> childrenElement = element.getChildren();
                    if (childrenElement != null && childrenElement.size() != 0) {
                        Element childElement = null;
                        for (Element ele : childrenElement) {
                            childElement = ele;
                            if ("Transform".equals(childElement.getName())) {
                                signReference.addToTransform(getAttributeWithPrefix(childElement, "Algorithm"));
                            }
                        }
                    }
                } else if ("DigestMethod".equals(element.getName())) {
                    signReference.setDigestMethod(getAttributeWithPrefix(element, "Algorithm"));
                } else if ("DigestValue".equals(element.getName())) {
                    signReference.setDigestValue(element.getText());
                }
            }

        }
        return signReference;
    }
}
