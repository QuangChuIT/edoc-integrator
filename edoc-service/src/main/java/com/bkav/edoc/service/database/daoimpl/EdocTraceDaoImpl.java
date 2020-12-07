package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocTraceDao;
import com.bkav.edoc.service.database.entity.EdocTrace;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdocTraceDaoImpl extends RootDaoImpl<EdocTrace, Long> implements EdocTraceDao {

    public EdocTraceDaoImpl() {
        super(EdocTrace.class);
    }

    public List<EdocTrace> getEdocTracesByOrganId(String responseForOrganId) {
        Session currentSession = openCurrentSession();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT et FROM EdocTrace et where et.toOrganDomain=:responseForOrganId and et.enable=:enable order by et.timeStamp DESC");
            Query<EdocTrace> query = currentSession.createQuery(sql.toString());
            query.setParameter("responseForOrganId", responseForOrganId);
            query.setParameter("enable", true);
            return query.list();
        } catch (Exception e){
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(currentSession);
        }
    }


    public void disableEdocTrace(EdocTrace trace) {
        try {
            this.saveOrUpdate(trace);
        } catch (Exception e) {
            LOGGER.error("Error when disable trace " + Arrays.toString(e.getStackTrace()));
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EdocTraceDaoImpl.class);
}
