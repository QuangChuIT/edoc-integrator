package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocTraceHeader;
import com.bkav.edoc.service.database.entity.EdocTraceHeaderList;
import com.bkav.edoc.service.database.services.EdocTraceHeaderListService;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;

public class EdocTraceHeaderListServiceUtil {
    private final static EdocTraceHeaderListService EDOC_TRACE_HEADER_LIST_SERVICE = new EdocTraceHeaderListService();

    public static void addEdocTraceHeaderList(EdocTraceHeaderList edocTraceHeaderList) {
        EDOC_TRACE_HEADER_LIST_SERVICE.createTraceHeaderList(edocTraceHeaderList);
    }

    public static void addTraceHeader(EdocTraceHeader edocTraceHeader) {
        EDOC_TRACE_HEADER_LIST_SERVICE.createTraceHeader(edocTraceHeader);
    }

    public static EdocTraceHeaderList addTraceHeaderList(TraceHeaderList traceHeaderList, String businessInfo, EdocDocument document) {
        return EDOC_TRACE_HEADER_LIST_SERVICE.addTraceHeaderList(traceHeaderList, businessInfo, document);
    }
}
