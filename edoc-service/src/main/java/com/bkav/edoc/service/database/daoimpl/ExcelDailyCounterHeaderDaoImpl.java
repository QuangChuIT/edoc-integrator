package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.ExcelDailyCounterHeaderDao;
import com.bkav.edoc.service.database.entity.ExcelDailyCounterHeader;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ExcelDailyCounterHeaderDaoImpl extends RootDaoImpl<ExcelDailyCounterHeader, Long> implements ExcelDailyCounterHeaderDao {
    public ExcelDailyCounterHeaderDaoImpl() {
        super(ExcelDailyCounterHeader.class);
    }

    @Override
    public List<ExcelDailyCounterHeader> getDailyCounterHeader() {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ExcelDailyCounterHeader> query = builder.createQuery(ExcelDailyCounterHeader.class);
            Root<ExcelDailyCounterHeader> root = query.from(ExcelDailyCounterHeader.class);
            query.select(root);
            Query<ExcelDailyCounterHeader> q = session.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    @Override
    public ExcelDailyCounterHeader getHeaderById(long id) {
        Session currentSession = openCurrentSession();
        try {
            CriteriaBuilder builder = currentSession.getCriteriaBuilder();
            CriteriaQuery<ExcelDailyCounterHeader> query = builder.createQuery(ExcelDailyCounterHeader.class);
            Root<ExcelDailyCounterHeader> root = query.from(ExcelDailyCounterHeader.class);
            query.select(root);
            query.where(builder.equal(root.get("id"), id));
            Query<ExcelDailyCounterHeader> q = currentSession.createQuery(query);
            return q.uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        } finally {
            closeCurrentSession(currentSession);
        }
    }

    public static void main(String[] args) {
        ExcelDailyCounterHeaderDaoImpl excelDailyCounterHeaderDao = new ExcelDailyCounterHeaderDaoImpl();
        System.out.println(excelDailyCounterHeaderDao.getHeaderById(1).getHeaderName());
    }

    private final static Logger LOGGER = Logger.getLogger(ExcelDailyCounterHeaderDaoImpl.class);
}
