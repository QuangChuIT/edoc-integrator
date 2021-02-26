package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.ExcelUserHeaderDao;
import com.bkav.edoc.service.database.entity.ExcelUserHeader;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ExcelUserHeaderDaoImpl extends RootDaoImpl<ExcelUserHeader, Long> implements ExcelUserHeaderDao {
    public ExcelUserHeaderDaoImpl() {
        super(ExcelUserHeader.class);
    }

    public List<ExcelUserHeader> getUserHeader() {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ExcelUserHeader> query = builder.createQuery(ExcelUserHeader.class);
            Root<ExcelUserHeader> root = query.from(ExcelUserHeader.class);
            query.select(root);
            Query<ExcelUserHeader> q = session.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    public ExcelUserHeader getHeaderById(long id) {
        Session currentSession = openCurrentSession();
        try {
            CriteriaBuilder builder = currentSession.getCriteriaBuilder();
            CriteriaQuery<ExcelUserHeader> query = builder.createQuery(ExcelUserHeader.class);
            Root<ExcelUserHeader> root = query.from(ExcelUserHeader.class);
            query.select(root);
            query.where(builder.equal(root.get("id"), id));
            Query<ExcelUserHeader> q = currentSession.createQuery(query);
            return q.uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        } finally {
            closeCurrentSession(currentSession);
        }
    }

    private final static Logger LOGGER = Logger.getLogger(ExcelUserHeaderDaoImpl.class);
}
