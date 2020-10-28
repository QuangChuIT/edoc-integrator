/**
 *
 */
package com.bkav.edoc.service.xml.base.header;

import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Traces", namespace = StringPool.TARGET_NAMESPACE)
@XmlType(name = "Traces", propOrder = {"statuses"})
@XmlAccessorType(XmlAccessType.FIELD)
public class GetTraceResponse {

    @XmlElement(name = "Status")
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