package com.bkav.edoc.sdk.edxml.entity.env;

import com.bkav.edoc.sdk.edxml.entity.Manifest;
import com.google.common.base.MoreObjects;

public class Body {

    private Manifest manifest;

    public Body() {
    }

    public Body(Manifest paramManifest) {
        this.manifest = paramManifest;
    }

    public Manifest getManifest() {
        return this.manifest;
    }

    public void setManifest(Manifest paramManifest) {
        this.manifest = paramManifest;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("edXMLManifest", this.manifest).toString();
    }
}
