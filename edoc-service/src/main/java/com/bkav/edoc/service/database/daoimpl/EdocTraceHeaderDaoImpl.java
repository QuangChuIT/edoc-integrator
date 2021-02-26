package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocTraceHeaderDao;
import com.bkav.edoc.service.database.entity.EdocTraceHeader;

public class EdocTraceHeaderDaoImpl extends RootDaoImpl<EdocTraceHeader, Long> implements EdocTraceHeaderDao {

    public EdocTraceHeaderDaoImpl() {
        super(EdocTraceHeader.class);
    }
}
