package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class SignReference {
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

    public static SignReference getData(Element elementNode) {
        SignReference signReference = new SignReference();
        signReference.setURI(EdxmlUtils.getAttributeWithPrefix(elementNode, "URI"));
        List<Element> elementList = elementNode.getChildren();
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
                                signReference.addToTransform(EdxmlUtils.getAttributeWithPrefix(childElement, "Algorithm"));
                            }
                        }
                    }
                } else if ("DigestMethod".equals(element.getName())) {
                    signReference.setDigestMethod(EdxmlUtils.getAttributeWithPrefix(element, "Algorithm"));
                } else if ("DigestValue".equals(element.getName())) {
                    signReference.setDigestValue(element.getText());
                }
            }

        }
        return signReference;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("URI", this.URI).add("Transforms", this.transforms)
                .add("DigestMethod Algorithm", this.digestMethod).add("DigestValue", this.digestValue).toString();
    }

}
