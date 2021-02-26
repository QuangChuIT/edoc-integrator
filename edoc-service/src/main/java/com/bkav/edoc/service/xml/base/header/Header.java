package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.xml.base.BaseElement;
import com.google.common.base.MoreObjects;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.List;

public class Header extends BaseElement {

    private IMessageHeader messageHeader;
    private TraceHeaderList traceHeaderList;
    private Signature signature;

    public Header() {
    }

    public Header(IMessageHeader messageHeader, TraceHeaderList traceHeaderList) {
        this.messageHeader = messageHeader;
        this.traceHeaderList = traceHeaderList;
    }

    public Header(IMessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Header(IMessageHeader messageHeader, TraceHeaderList traceHeaderList, Signature signature) {
        this.messageHeader = messageHeader;
        this.traceHeaderList = traceHeaderList;
        this.signature = signature;
    }

    public IMessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(IMessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public TraceHeaderList getTraceHeaderList() {
        return traceHeaderList;
    }

    public void setTraceHeaderList(TraceHeaderList traceHeaderList) {
        this.traceHeaderList = traceHeaderList;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public static Header fromContent(Element element, IMessageHeader iMessageHeader) {
        Header header = new Header();
        List<Element> elementList = element.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("MessageHeader".equals(children.getName())) {
                    header.setMessageHeader(iMessageHeader.fromContent(children));
                }
                if ("TraceHeaderList".equals(children.getName())) {
                    header.setTraceHeaderList(TraceHeaderList.fromContent(children));
                }
                if ("Signature".equals(children.getName())) {
                    header.setSignature(Signature.fromContent(children));
                }
            }

        }
        return header;
    }

    public static Element getContent(Document document) {
        Element element = document.getRootElement().getChild("edXMLEnvelope", Namespace.getNamespace(EdXmlConstant.EDXML_URI));
        if (element != null) {
            List<Element> elementChildren = element.getChildren();
            if (elementChildren != null && elementChildren.size() > 0) {
                for (Element children : elementChildren) {
                    if ("edXMLHeader".equals(children.getName())) {
                        return children;
                    }
                }
            }

        }
        return null;
    }

    public void accumulate(Element parentElement) {
        Element edXMLHeader = this.accumulate(parentElement, "edXMLHeader");
        this.accumulate(edXMLHeader, (BaseElement) this.messageHeader);
        this.accumulate(edXMLHeader, this.traceHeaderList);
        this.accumulate(edXMLHeader, this.signature);
    }

    private void accumulate(Element parentElement, BaseElement baseElement) {
        if (baseElement != null) {
            baseElement.accumulate(parentElement);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("MessageHeader",
                this.messageHeader).add("TraceHeaderList",
                this.traceHeaderList).add("Signature", this.signature).toString();
    }
}
