package com.bkav.edoc.edxml.base.body;

import com.bkav.edoc.edxml.base.BaseElement;
import com.bkav.edoc.edxml.resource.EdXmlConstant;
import com.google.common.base.MoreObjects;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.List;

public class Body extends BaseElement {

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

    public static Body fromContent(Element element) {
        Body body = new Body();
        List<Element> elementList = element.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("Manifest".equals(children.getName())) {
                    body.setManifest(Manifest.fromContent(children));
                } else if ("edXMLManifest".equals(children.getName())) {
                    body.setManifest(Manifest.fromContent(children));
                }
            }

        }
        return body;
    }

    public static Element getContent(Document document) {
        Element element = document.getRootElement().getChild("edXMLEnvelope", Namespace.getNamespace(EdXmlConstant.EDXML_URI));
        if (element != null) {
            List<Element> elementList = element.getChildren();
            if (elementList != null && elementList.size() > 0) {
                for (Element children : elementList) {
                    if ("edXMLBody".equals(children.getName())) {
                        return children;
                    }
                }
            }
        }
        return null;
    }

    public void accumulate(Element element) {
        Element edXMLBody = this.accumulate(element, "edXMLBody");
        if (this.manifest != null) {
            this.manifest.accumulate(edXMLBody);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("edXMLManifest", this.manifest).toString();
    }
}
