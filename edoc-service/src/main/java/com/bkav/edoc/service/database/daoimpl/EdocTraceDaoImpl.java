package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocTraceDao;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.util.PropsUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EdocTraceDaoImpl extends RootDaoImpl<EdocTrace, Long> implements EdocTraceDao {

    public EdocTraceDaoImpl() {
        super(EdocTrace.class);
    }

    public List<EdocTrace> getEdocTracesByOrganId(String responseForOrganId, Date fromTime) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            String parentDomain = PropsUtil.get("edoc.domain.A.parent");

            if (fromTime != null) {
                sql.append("SELECT et FROM EdocTrace et where " +
                        "(et.toOrganDomain=:responseForOrganId) and et.timeStamp >= :fromTime order by et.timeStamp DESC");
            } else {
                sql.append("SELECT et FROM EdocTrace et where " +
                        "(et.toOrganDomain=:responseForOrganId) and et.enable=:enable order by et.timeStamp DESC");
            }
            LOGGER.info(sql.toString());
            Query<EdocTrace> query = currentSession.createQuery(sql.toString(), EdocTrace.class);
            query.setParameter("responseForOrganId", responseForOrganId);
            query.setParameter("fromOrganId", responseForOrganId);
            if (fromTime != null) {
                query.setParameter("fromTime", fromTime);
            } else {
                query.setParameter("enable", true);
            }
            return query.list();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(currentSession);
        }
        return new ArrayList<>();
    }


    public void disableEdocTrace(EdocTrace trace) {
        try {
            this.saveOrUpdate(trace);
        } catch (Exception e) {
            LOGGER.error("Error when disable trace " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public boolean exists(String fromOrgan, String toOrgan, String code, int statusCode) {
        Session currentSession = openCurrentSession();
        boolean exists = false;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT et FROM EdocTrace et where et.toOrganDomain=:toOrgan and et.fromOrganDomain=:fromOrgan and et.code=:code and et.statusCode=:statusCode");
            Query<EdocTrace> query = currentSession.createQuery(sql.toString(), EdocTrace.class);
            query.setParameter("toOrgan", toOrgan);
            query.setParameter("fromOrgan", fromOrgan);
            query.setParameter("code", code);
            query.setParameter("statusCode", statusCode);
            List<EdocTrace> result = query.getResultList();
            if (result != null && result.size() > 0) {
                exists = true;
            }
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(currentSession);
        }
        return exists;
    }

    private static final Logger LOGGER = Logger.getLogger(EdocTraceDaoImpl.class);
}
