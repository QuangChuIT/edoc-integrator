package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class Body extends CommonElement implements IElement<Body> {

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

    @Override
    public void createElement(Element rootElement) {
        Element edXMLBody = this.createElement(rootElement, "edXMLBody");
        if (this.manifest != null) {
            this.manifest.createElement(edXMLBody);
        }
    }

    @Override
    public Body getData(Element element) {
        Body body = new Body();
        List<Element> elementList = element.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("Manifest".equals(children.getName())) {
                    body.setManifest(new Manifest().getData(children));
                } else if ("edXMLManifest".equals(children.getName())) {
                    body.setManifest(new Manifest().getData(children));
                }
            }

        }
        return body;
    }
}
