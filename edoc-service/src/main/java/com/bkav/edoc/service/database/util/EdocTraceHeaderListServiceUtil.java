package com.bkav.edoc.service.database.util;
import com.bkav.edoc.service.database.entity.EdocTraceHeader;
import com.bkav.edoc.service.database.entity.EdocTraceHeaderList;
import com.bkav.edoc.service.database.services.EdocTraceHeaderListService;

public class EdocTraceHeaderListServiceUtil {
    private final static EdocTraceHeaderListService EDOC_TRACE_HEADER_LIST_SERVICE = new EdocTraceHeaderListService();

    public static void addEdocTraceHeaderList(EdocTraceHeaderList edocTraceHeaderList) {
        EDOC_TRACE_HEADER_LIST_SERVICE.createTraceHeaderList(edocTraceHeaderList);
    }

    public static void addTraceHeader(EdocTraceHeader edocTraceHeader) {
        EDOC_TRACE_HEADER_LIST_SERVICE.createTraceHeader(edocTraceHeader);
    }

}
