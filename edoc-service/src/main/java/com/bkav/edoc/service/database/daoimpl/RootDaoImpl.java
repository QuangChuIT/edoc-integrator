package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.RootDao;
import com.bkav.edoc.service.database.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public abstract class RootDaoImpl<T, Id extends Serializable> implements RootDao<T, Id> {
    private Class<T> clazz;
    private Session currentSession;
    private Transaction currentTransaction;

    public RootDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Session openCurrentSession() {
        this.currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionWithTransaction() {
        this.currentSession = getSessionFactory().openSession();
        this.currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        this.currentSession.close();
    }

    public void closeCurrentSessionWithTransaction() {
        this.currentTransaction.commit();
        this.currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    public Session getCurrentSession() {
        return this.currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void persist(T entity) {
        getCurrentSession().save(entity);
    }

    public void update(T entity) {
        try {
            getCurrentSession().update(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T findById(Id id) {
        return getCurrentSession().get(clazz, id);
    }

    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    public List<T> findAll() {
        return (List<T>) getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    public void saveOrUpdate(T entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    public void deleteAll() {
        List<T> entityList = findAll();
        for (T entity : entityList) {
            delete(entity);
        }
    }
}
