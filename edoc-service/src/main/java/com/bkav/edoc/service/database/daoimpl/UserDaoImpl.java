package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.UserDao;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoImpl extends RootDaoImpl<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User findByUsername(String username) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(builder.equal(root.get("username"), username));
        Query<User> q = currentSession.createQuery(query);
        return q.uniqueResult();
    }

    public User findByOrgan(EdocDynamicContact organ) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(builder.equal(root.get("dynamicContact"), organ));
        Query<User> q = currentSession.createQuery(query);
        return q.uniqueResult();
    }

    public List<User> getAllUser() {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        Query<User> q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public void updateUser(User user) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();
            update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                closeCurrentSession();
            }
        }
    }

    @Override
    public void createUser(User user) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();
            this.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                closeCurrentSession();
            }
        }
    }

    @Override
    public boolean checkExist(String username) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(builder.count(root.get("username")));
        query.where(builder.equal(root.get("username"), username));
        Long result = session.createQuery(query).getSingleResult();
        return result > 0L;
    }

    @Override
    public List<User> getUsers(boolean onSSO) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(builder.equal(root.get("sso"), onSSO));
        Query<User> userQuery = session.createQuery(query);
        return userQuery.getResultList();
    }

    @Override
    public boolean deleteUser(long userId) {
        Session session = openCurrentSession();
        boolean result;
        try {
            session.beginTransaction();
            User user = this.findById(userId);
            if (user == null) {
                LOGGER.error("Error delete user not found document with id " + userId);
                result = false;
            } else {
                session.delete(user);
                session.getTransaction().commit();
                result = true;
            }
        } catch (Exception e) {
            result = false;
            LOGGER.error("Error delete user with id " + userId + " cause " + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    private final static Logger LOGGER = Logger.getLogger(UserDaoImpl.class);
}
