package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.status.header.MessageStatus;
import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class GetTraceResponse {
    protected List<MessageStatus> statuses;

    public List<MessageStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<MessageStatus> statuses) {
        this.statuses = statuses;
    }

    public GetTraceResponse(List<MessageStatus> statuses) {
        super();
        this.statuses = statuses;
    }

    public GetTraceResponse() {
        statuses = new ArrayList<>();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Statuses", statuses).toString();
    }
}