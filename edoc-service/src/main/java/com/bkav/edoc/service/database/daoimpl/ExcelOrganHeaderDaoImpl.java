package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.ExcelOrganHeaderDao;
import com.bkav.edoc.service.database.entity.ExcelOrganHeader;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ExcelOrganHeaderDaoImpl extends RootDaoImpl<ExcelOrganHeader, Long> implements ExcelOrganHeaderDao {
    public ExcelOrganHeaderDaoImpl() {
        super(ExcelOrganHeader.class);
    }

    public List<ExcelOrganHeader> getOrganHeader() {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ExcelOrganHeader> query = builder.createQuery(ExcelOrganHeader.class);
            Root<ExcelOrganHeader> root = query.from(ExcelOrganHeader.class);
            query.select(root);
            Query<ExcelOrganHeader> q = session.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    public ExcelOrganHeader getHeaderById(long id) {
        Session currentSession = openCurrentSession();
        try {
            CriteriaBuilder builder = currentSession.getCriteriaBuilder();
            CriteriaQuery<ExcelOrganHeader> query = builder.createQuery(ExcelOrganHeader.class);
            Root<ExcelOrganHeader> root = query.from(ExcelOrganHeader.class);
            query.select(root);
            query.where(builder.equal(root.get("id"), id));
            Query<ExcelOrganHeader> q = currentSession.createQuery(query);
            return q.uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        } finally {
            closeCurrentSession(currentSession);
        }
    }

    private final static Logger LOGGER = Logger.getLogger(ExcelOrganHeaderDaoImpl.class);
}
