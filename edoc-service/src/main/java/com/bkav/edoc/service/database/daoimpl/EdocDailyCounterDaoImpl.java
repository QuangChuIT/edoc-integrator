package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocDailyCounterDao;
import com.bkav.edoc.service.database.entity.EdocDailyCounter;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class EdocDailyCounterDaoImpl extends RootDaoImpl<EdocDailyCounter, Long> implements EdocDailyCounterDao {

    public EdocDailyCounterDaoImpl() {
        super(EdocDailyCounter.class);
    }

    @Override
    public boolean checkExistCounter(Date date) {
        Session session = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dc from EdocDailyCounter dc where dc.dateTime=:dateTime");
        Query<EdocDailyCounter> query = session.createQuery(sql.toString());
        query.setParameter("dateTime", date);
        List<EdocDailyCounter> edocDailyCounters = query.getResultList();
        return edocDailyCounters.size() > 0;
    }

    @Override
    public List<EdocDailyCounter> getOverStat(String organDomain) {
        Session session = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dc from EdocDailyCounter dc where dc.organDomain = :organDomain");
        Query<EdocDailyCounter> query = session.createQuery(sql.toString());
        query.setParameter("organDomain", organDomain);
        return query.getResultList();
    }

    @Override
    public List<EdocDailyCounter> getOverStat(String organDomain, Date fromDate, Date toDate) {
        Session session = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dc from EdocDailyCounter dc where dc.organDomain = :organDomain and DATE(dateTime) >= DATE(:fromDate) and DATE(dateTime) <= DATE(:toDate)");
        Query<EdocDailyCounter> query = session.createQuery(sql.toString());
        query.setParameter("organDomain", organDomain);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return query.getResultList();
    }

    @Override
    public Long getStat() {
        Session session = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sum(dc.sent + dc.received) from EdocDailyCounter dc");
        Query<Long> query = session.createQuery(sql.toString());
        return query.uniqueResult();
    }

    public void createDailyCounter(EdocDailyCounter dailyCounter) {
        try (Session session = openCurrentSession()) {
            session.beginTransaction();
            this.persist(dailyCounter);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error when create edoc daily counter " + e);
        }
    }

    private final static Logger LOGGER = Logger.getLogger(EdocDailyCounterDaoImpl.class);
}
