package com.bkav.edoc.service.database.cache;

import java.io.Serializable;
import java.util.Date;

public class TraceHeaderCacheEntry implements Serializable {
    private Long traceHeaderId;
    private String organDomain;
    private Date timeStamp;
    private long documentId;

    public Long getTraceHeaderId() {
        return traceHeaderId;
    }

    public void setTraceHeaderId(Long traceHeaderId) {
        this.traceHeaderId = traceHeaderId;
    }

    public String getOrganDomain() {
        return organDomain;
    }

    public void setOrganDomain(String organDomain) {
        this.organDomain = organDomain;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }
}
