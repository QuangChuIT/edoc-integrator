package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocTrace;

import java.util.Date;
import java.util.List;

public interface EdocTraceDao {

    List<EdocTrace> getEdocTracesByOrganId(String responseForOrganId, Date fromTime);

    void disableEdocTrace(EdocTrace trace);

    boolean exists(String fromOrgan, String toOrgan, String code, int statusCode);
}
