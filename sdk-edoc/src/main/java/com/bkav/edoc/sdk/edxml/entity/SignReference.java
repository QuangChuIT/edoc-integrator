package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("URI", this.URI).add("Transforms", this.transforms)
                .add("DigestMethod Algorithm", this.digestMethod).add("DigestValue", this.digestValue).toString();
    }

}
