package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.ExcelOrganHeaderDao;
import com.bkav.edoc.service.database.entity.ExcelOrganHeader;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ExcelOrganHeaderDaoImpl extends RootDaoImpl<ExcelOrganHeader, Long> implements ExcelOrganHeaderDao {
    public ExcelOrganHeaderDaoImpl() { super(ExcelOrganHeader.class);}

    public List<ExcelOrganHeader> getOrganHeader() {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ExcelOrganHeader> query = builder.createQuery(ExcelOrganHeader.class);
        Root<ExcelOrganHeader> root = query.from(ExcelOrganHeader.class);
        query.select(root);
        Query<ExcelOrganHeader> q = session.createQuery(query);
        return q.getResultList();
    }

    public ExcelOrganHeader getHeaderById(long id) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<ExcelOrganHeader> query = builder.createQuery(ExcelOrganHeader.class);
        Root<ExcelOrganHeader> root = query.from(ExcelOrganHeader.class);
        query.select(root);
        query.where(builder.equal(root.get("id"), id));
        Query<ExcelOrganHeader> q = currentSession.createQuery(query);
        return q.uniqueResult();
    }
}
