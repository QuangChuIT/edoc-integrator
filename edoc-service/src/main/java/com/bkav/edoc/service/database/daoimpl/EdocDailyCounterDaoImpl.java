package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocDailyCounterDao;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EdocDailyCounterDaoImpl extends RootDaoImpl<EdocDailyCounter, Long> implements EdocDailyCounterDao {

    public EdocDailyCounterDaoImpl() {
        super(EdocDailyCounter.class);
    }

    @Override
    public boolean checkExistCounter(Date date) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT dc from EdocDailyCounter dc where dc.dateTime=:dateTime");
            Query<EdocDailyCounter> query = session.createQuery(sql.toString(), EdocDailyCounter.class);
            query.setParameter("dateTime", date);
            List<EdocDailyCounter> edocDailyCounters = query.getResultList();
            return edocDailyCounters.size() > 0;
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        } finally {
            closeCurrentSession(session);
        }
    }

    @Override
    public List<EdocDailyCounter> getOverStat(String organDomain) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT dc from EdocDailyCounter dc where dc.organDomain = :organDomain");
            Query<EdocDailyCounter> query = session.createQuery(sql.toString(), EdocDailyCounter.class);
            query.setParameter("organDomain", organDomain);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    @Override
    public List<EdocDailyCounter> getOverStat(String organDomain, Date fromDate, Date toDate) {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT dc from EdocDailyCounter dc where dc.organDomain = :organDomain " +
                    "and DATE(dateTime) >= DATE(:fromDate) and DATE(dateTime) <= DATE(:toDate)");
            Query<EdocDailyCounter> query = session.createQuery(sql.toString(), EdocDailyCounter.class);
            query.setParameter("organDomain", organDomain);
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    @Override
    public Long getStat() {
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT sum(dc.sent + dc.received) from EdocDailyCounter dc");
            Query<Long> query = session.createQuery(sql.toString(), Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e);
            return 0L;
        } finally {
            closeCurrentSession(session);
        }
    }

    public void createDailyCounter(EdocDailyCounter dailyCounter) {
        this.persist(dailyCounter);
    }

    private final static Logger LOGGER = Logger.getLogger(EdocDailyCounterDaoImpl.class);
}
