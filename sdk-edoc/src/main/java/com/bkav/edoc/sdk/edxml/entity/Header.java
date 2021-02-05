package com.bkav.edoc.sdk.edxml.entity;

import org.jdom2.Element;

import java.util.List;

public class Header extends CommonElement implements IElement<Header> {
    private MessageHeader messageHeader;
    private TraceHeaderList traceHeaderList;
    private Signature signature;

    public Header() {
    }

    public Header(MessageHeader messageHeader, TraceHeaderList traceHeaderList) {
        this.messageHeader = messageHeader;
        this.traceHeaderList = traceHeaderList;
    }

    public Header(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Header(MessageHeader messageHeader, TraceHeaderList traceHeaderList, Signature signature) {
        this.messageHeader = messageHeader;
        this.traceHeaderList = traceHeaderList;
        this.signature = signature;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
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

    @Override
    public void createElement(Element rootElement) {

    }

    @Override
    public Header getData(Element element) {
        Header header = new Header();
        List<Element> elementList = element.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("MessageHeader".equals(children.getName())) {
                    header.setMessageHeader(messageHeader.fromContent(children));
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
}
