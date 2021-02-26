package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.UserDao;
import com.bkav.edoc.service.database.entity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends RootDaoImpl<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User findByUsername(String username) {
        Session currentSession = openCurrentSession();
        try {
            CriteriaBuilder builder = currentSession.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);
            query.where(builder.equal(root.get("username"), username));
            Query<User> q = currentSession.createQuery(query);
            return q.uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        } finally {
            if (currentSession != null) {
                currentSession.close();
            }
        }

    }

    public List<User> getAllUser() {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);
            Query<User> q = session.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            LOGGER.error("Error get all user cause " + e);
            return new ArrayList<>();
        }
    }

    @Override
    public void updateUser(User user) {
        this.update(user);
    }

    @Override
    public void createUser(User user) {
        this.persist(user);
    }

    @Override
    public boolean checkExist(String username) {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<User> root = query.from(User.class);
            query.select(builder.count(root.get("username")));
            query.where(builder.equal(root.get("username"), username));
            Long result = session.createQuery(query).getSingleResult();
            return result > 0L;
        } catch (Exception e) {
            LOGGER.error("Error check exist user with username " + username + " cause " + e.getMessage());
            return false;
        } finally {
            closeCurrentSession(session);
        }
    }

    @Override
    public List<User> getUsers(boolean onSSO) {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);
            query.where(builder.equal(root.get("sso"), onSSO));
            Query<User> userQuery = session.createQuery(query);
            return userQuery.getResultList();
        } catch (Exception e) {
            LOGGER.error("Error get user on sso cause " + e.getMessage());
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }
    }

    @Override
    public boolean deleteUser(long userId) {
        boolean result;
        try {
            User user = this.findById(userId);
            if (user == null) {
                LOGGER.error("Error delete user not found document with id " + userId);
                result = false;
            } else {
                delete(user);
                result = true;
            }
        } catch (Exception e) {
            result = false;
            LOGGER.error("Error delete user with id " + userId + " cause " + e.getMessage());
        }
        return result;
    }

    private final static Logger LOGGER = Logger.getLogger(UserDaoImpl.class);
}
