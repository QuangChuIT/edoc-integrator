package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocPriority;
import com.bkav.edoc.service.database.services.EdocPriorityService;

import java.util.List;

public class EdocPriorityServiceUtil {

    private final static EdocPriorityService EDOC_PRIORITY_SERVICE = new EdocPriorityService();

    public static List<EdocPriority> getPriorities() {
        return EDOC_PRIORITY_SERVICE.getAllPriorities();
    }
}
