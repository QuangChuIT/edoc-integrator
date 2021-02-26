package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocTraceHeaderList;

import java.util.List;

public interface EdocTraceHeaderListDao {
    EdocTraceHeaderList getTraceHeaderListByDocId(long documentId);
}
