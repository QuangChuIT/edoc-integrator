package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.database.services.EdocTraceService;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.status.header.MessageStatus;

import java.util.Date;
import java.util.List;

public class EdocTraceServiceUtil {
    public final static EdocTraceService EDOC_TRACE_SERVICE = new EdocTraceService();

    public static EdocTrace addTrace(MessageStatus messageStatus, List<Error> errorList) {
        return EDOC_TRACE_SERVICE.updateTrace(messageStatus, errorList);
    }

    public static List<EdocTrace> getEdocTracesByOrganId(String organId, Date fromTime) {
        return EDOC_TRACE_SERVICE.getEdocTracesByOrganId(organId, fromTime);
    }

    public static List<EdocTrace> getEdocTracesByTraceId(long docId) {
        return EDOC_TRACE_SERVICE.getEdocTracesByTraceId(docId);
    }

    public static EdocTrace getEdocTrace(long traceId) {
        return EDOC_TRACE_SERVICE.getTrace(traceId);
    }
}
