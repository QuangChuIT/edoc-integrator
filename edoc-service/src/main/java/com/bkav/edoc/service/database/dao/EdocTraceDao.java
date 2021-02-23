package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocTrace;

import java.util.List;

public interface EdocTraceDao {

    List<EdocTrace> getEdocTracesByOrganId(String responseForOrganId);

    void disableEdocTrace(EdocTrace trace);
}
