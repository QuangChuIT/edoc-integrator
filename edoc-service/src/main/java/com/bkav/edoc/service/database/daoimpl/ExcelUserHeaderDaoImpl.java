package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.ExcelUserHeaderDao;
import com.bkav.edoc.service.database.entity.ExcelUserHeader;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ExcelUserHeaderDaoImpl extends RootDaoImpl<ExcelUserHeader, Long> implements ExcelUserHeaderDao {
    public ExcelUserHeaderDaoImpl() { super(ExcelUserHeader.class);}

    public List<ExcelUserHeader> getUserHeader() {
        Session session = openCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<ExcelUserHeader> query = builder.createQuery(ExcelUserHeader.class);
        Root<ExcelUserHeader> root = query.from(ExcelUserHeader.class);
        query.select(root);
        Query<ExcelUserHeader> q = session.createQuery(query);
        closeCurrentSession(session);
        return q.getResultList();
    }

    public ExcelUserHeader getHeaderById(long id) {
        Session currentSession = openCurrentSession();
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<ExcelUserHeader> query = builder.createQuery(ExcelUserHeader.class);
        Root<ExcelUserHeader> root = query.from(ExcelUserHeader.class);
        query.select(root);
        query.where(builder.equal(root.get("id"), id));
        Query<ExcelUserHeader> q = currentSession.createQuery(query);
        closeCurrentSession(currentSession);
        return q.uniqueResult();
    }
}
