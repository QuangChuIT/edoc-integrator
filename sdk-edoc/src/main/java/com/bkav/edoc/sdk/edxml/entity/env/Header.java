package com.bkav.edoc.sdk.edxml.entity.env;

import com.bkav.edoc.sdk.edxml.entity.MessageHeader;
import com.bkav.edoc.sdk.edxml.entity.Signature;
import com.bkav.edoc.sdk.edxml.entity.TraceHeaderList;

public class Header {
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

}
