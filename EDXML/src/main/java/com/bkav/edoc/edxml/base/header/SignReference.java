package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SignReference extends BaseElement {
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

    public static SignReference fromContent(Element elementNode) {
        SignReference signReference = new SignReference();
        signReference.setURI(getAttributeWithPrefix(elementNode, "URI"));
        List<Element> elementList = elementNode.getChildren();
        if (elementList != null && elementList.size() != 0) {
            Element element = null;
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

    public void accumulate(Element element) {
        element.setAttribute("URI", Strings.nullToEmpty(this.URI));
        Element SignReference;
        if (this.transforms != null && !this.transforms.isEmpty()) {
            SignReference = this.accumulateWithoutPrefix(element, "Transforms");
            for (String str : this.transforms) {
                Element transform = this.accumulateWithoutPrefix(SignReference, "Transform");
                this.accumulateAttWithoutPrefix(transform, "Algorithm", str);
            }
        }
        SignReference = this.accumulateWithoutPrefix(element, "DigestMethod");
        this.accumulateAttWithoutPrefix(SignReference, "Algorithm", this.digestMethod);
        this.accumulateWithoutPrefix(element, "DigestValue", this.digestValue);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("URI", this.URI).add("Transforms", this.transforms)
                .add("DigestMethod Algorithm", this.digestMethod).add("DigestValue", this.digestValue).toString();
    }
}
