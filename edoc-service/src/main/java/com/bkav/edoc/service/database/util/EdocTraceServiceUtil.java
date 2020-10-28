package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.services.EdocTraceService;
import com.bkav.edoc.service.xml.status.header.MessageStatus;

public class EdocTraceServiceUtil {
    public final static EdocTraceService EDOC_TRACE_SERVICE = new EdocTraceService();

    public static boolean addTrace(MessageStatus messageStatus) {
        return EDOC_TRACE_SERVICE.updateTrace(messageStatus);
    }

}
