package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.RootDao;
import com.bkav.edoc.service.database.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public abstract class RootDaoImpl<T, Id extends Serializable> implements RootDao<T, Id> {
    private Class<T> clazz;

    public RootDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Session openCurrentSession() {
        return getSessionFactory().openSession();
    }

    public Session openCurrentSessionWithTransaction() {
        return getSessionFactory().openSession();
    }

    public void closeCurrentSession(Session session) {
        if (session != null) {
            session.close();
        }
    }


    private static SessionFactory getSessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    public void persist(T entity) {
        Session session = openCurrentSession();
        try {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error persist entity " + entity.getClass() + " cause " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            closeCurrentSession(session);
        }
    }

    public void update(T entity) {
        Session session = openCurrentSession();
        try {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error update entity " + entity.getClass() + " cause " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            closeCurrentSession(session);
        }
    }

    public T findById(Id id) {
        Session session = openCurrentSession();
        try {
            return session.get(clazz, id);
        } catch (Exception e) {
            LOGGER.error("Error find by id " + id);
            return null;
        } finally {
            closeCurrentSession(session);
        }
    }

    public void delete(T entity) {
        Session session = openCurrentSession();
        try {
            session.delete(entity);
        } catch (Exception e) {
            LOGGER.error("Error delete entity + " + entity.getClass() + " cause " + e.getMessage());
        } finally {
            closeCurrentSession(session);
        }
    }

    public List<T> findAll() {
        Session session = openCurrentSession();
        try {
            return (List<T>) session.createQuery("from " + clazz.getName()).list();
        } catch (Exception e) {
            LOGGER.error("Error find all cause " + e.getMessage());
            return null;
        } finally {
            closeCurrentSession(session);
        }
    }

    public void saveOrUpdate(T entity) {
        Session session = openCurrentSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Error save or update entity " + entity.getClass() + " cause " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            closeCurrentSession(session);
        }
    }

    public void deleteAll() {
        List<T> entityList = findAll();
        for (T entity : entityList) {
            delete(entity);
        }
    }

    private final static Logger LOGGER = Logger.getLogger(RootDaoImpl.class);
}
