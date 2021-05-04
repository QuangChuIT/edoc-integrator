package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocTraceDao;
import com.bkav.edoc.service.database.entity.EdocTrace;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.google.gson.Gson;
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

            // Fix tam code, co thoi gian se toi uu lai code
            // Se goi Stored procedure
            Query<EdocTrace> query;
            if (responseForOrganId.equals(PropsUtil.get("edoc.domain.A.parent"))) {
                if (GetterUtil.getBoolean(PropsUtil.get("edoc.check.organ.is.tayninh"), false)) {
                    if (fromTime != null) {
                        sql.append("SELECT et FROM EdocTrace et where et.toOrganDomain like concat('%' ,:responseForOrganId, '%') " +
                                "and et.timeStamp >= :fromTime order by et.timeStamp DESC");
                    } else {
                        sql.append("SELECT et FROM EdocTrace et where et.toOrganDomain like concat('%' ,:responseForOrganId, '%') " +
                                "and et.enable=:enable order by et.timeStamp DESC");
                    }
                    query = currentSession.createQuery(sql.toString(), EdocTrace.class);
                    query.setParameter("responseForOrganId", PropsUtil.get("edoc.domain.A53.regex"));
                } else {
                    if (fromTime != null) {
                        sql.append("SELECT et FROM EdocTrace et where et.toOrganDomain like concat('%' ,:responseForOrganId, '%')" +
                                " or et.toOrganDomain like concat('%' ,:responseForOrganId1, '%')" +
                                " or et.toOrganDomain like concat('%' ,:responseForOrganId2, '%')" +
                                " or et.toOrganDomain like concat('%' ,:responseForOrganId3, '%')" +
                                " and et.timeStamp >= :fromTime order by et.timeStamp DESC");
                    } else {
                        sql.append("SELECT et FROM EdocTrace et where et.toOrganDomain like concat('%' ,:responseForOrganId, '%')" +
                                " or et.toOrganDomain like concat('%' ,:responseForOrganId1, '%')" +
                                " or et.toOrganDomain like concat('%' ,:responseForOrganId2, '%')" +
                                " or et.toOrganDomain like concat('%' ,:responseForOrganId3, '%')" +
                                " and et.enable=:enable order by et.timeStamp DESC");
                    }
                    String[] listOrgan = PropsUtil.get("edoc.integrator.center.lamdong").split("\\|");
                    System.out.println(sql);
                    query = currentSession.createQuery(sql.toString(), EdocTrace.class);
                    query.setParameter("responseForOrganId", listOrgan[0]);
                    query.setParameter("responseForOrganId1", listOrgan[1]);
                    query.setParameter("responseForOrganId2", listOrgan[2]);
                    query.setParameter("responseForOrganId3", listOrgan[3]);
                }

                /*if (GetterUtil.getBoolean(PropsUtil.get("edoc.check.organ.is.tayninh"), false)) {
                    responseForOrganId = PropsUtil.get("edoc.domain.A53.regex");
                } else {
                    responseForOrganId = PropsUtil.get("edoc.integrator.center.lamdong");
                }*/

            } else {
                if (fromTime != null) {
                    sql.append("SELECT et FROM EdocTrace et where " +
                            "(et.toOrganDomain=:responseForOrganId) and et.timeStamp >= :fromTime order by et.timeStamp DESC");
                } else {
                    sql.append("SELECT et FROM EdocTrace et where " +
                            "(et.toOrganDomain=:responseForOrganId) and et.enable=:enable order by et.timeStamp DESC");
                }
                query = currentSession.createQuery(sql.toString(), EdocTrace.class);
                query.setParameter("responseForOrganId", responseForOrganId);
            }
            //query.setParameter("fromOrganId", responseForOrganId);
            if (fromTime != null) {
                query.setParameter("fromTime", fromTime);
            } else {
                query.setParameter("enable", true);
            }
            LOGGER.info(query);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(currentSession);
        }
        return new ArrayList<>();
    }

    public List<EdocTrace> getEdocTracesByTraceId(long docId) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT et FROM EdocTrace et where et.enable=:enable and et.traceId = :traceId order by et.timeStamp DESC");
            Query<EdocTrace> query = currentSession.createQuery(sql.toString(), EdocTrace.class);
            query.setParameter("enable", true);
            query.setParameter("traceId", docId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(currentSession);
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        EdocTraceDaoImpl edocTraceDao = new EdocTraceDaoImpl();
        //String json = new Gson().toJson(edocTraceDao.getEdocTracesByOrganId("000.A53.000", null));
        List<EdocTrace> list = edocTraceDao.getEdocTracesByOrganId("000.H36.000",null);
        list.forEach(li -> {
            System.out.println(li.getFromOrganDomain() + " - " + li.getToOrganDomain());
        });
        System.out.println(list.size());
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
