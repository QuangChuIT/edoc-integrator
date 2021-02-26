package com.bkav.edoc.sdk.edxml.entity.env;

import com.bkav.edoc.sdk.edxml.entity.Manifest;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

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

    public static Body getData(Element element) {
        Body body = new Body();
        List<Element> elementList = element.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("Manifest".equals(children.getName())) {
                    body.setManifest(Manifest.getData(children));
                } else if ("edXMLManifest".equals(children.getName())) {
                    body.setManifest(Manifest.getData(children));
                }
            }

        }
        return body;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("edXMLManifest", this.manifest).toString();
    }
}
