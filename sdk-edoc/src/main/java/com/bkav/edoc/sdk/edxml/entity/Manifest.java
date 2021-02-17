package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class Manifest extends CommonElement implements IElement<Manifest> {
    private String version = "1.0";
    private List<Reference> references;

    public Manifest() {
    }

    public Manifest(Reference reference) {
        this.references = new ArrayList<>();
        this.references.add(reference);
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String paramString) {
        this.version = paramString;
    }

    public List<Reference> getReferences() {
        return this.references;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public void addReference(Reference reference) {
        if (this.references == null) {
            this.references = new ArrayList<>();
        }

        this.references.add(reference);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("version", this.version).add("Reference", this.references).toString();
    }

    @Override
    public void createElement(Element rootElement) {
        Element edXMLManifest = this.createElement(rootElement, "edXMLManifest");
        this.createAttribute(edXMLManifest, "version", this.version);
        if (this.references != null && !this.references.isEmpty()) {
            for (Reference reference : this.references) {
                reference.createElement(edXMLManifest);
            }
        }
    }

    @Override
    public Manifest getData(Element element) {
        Manifest manifest = new Manifest();
        List<Element> elementList = element.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element childrenElement : elementList) {
                if ("Reference".equals(childrenElement.getName())) {
                    Reference reference = new Reference().getData(childrenElement);
                    if (reference != null) {
                        manifest.addReference(reference);
                    }
                }
            }

        }
        return manifest;
    }
}
