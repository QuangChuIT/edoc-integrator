package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.UserRoleDao;
import com.bkav.edoc.service.database.entity.UserRole;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDaoImpl extends RootDaoImpl<UserRole, Long> implements UserRoleDao {

    public UserRoleDaoImpl() {
        super(UserRole.class);
    }

    @Override
    public List<UserRole> getUserRole() {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
            Root<UserRole> root = query.from(UserRole.class);
            query.select(root);
            Query<UserRole> q = session.createQuery(query);
            return q.getResultList();

        } catch (Exception e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            closeCurrentSession(session);
        }

    }

    @Override
    public UserRole getRoleByUserId(long userId) {
        Session currentSession = openCurrentSession();
        try {
            CriteriaBuilder builder = currentSession.getCriteriaBuilder();
            CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
            Root<UserRole> root = query.from(UserRole.class);
            query.select(root);
            query.where(builder.equal(root.get("userId"), userId));
            Query<UserRole> q = currentSession.createQuery(query);
            return q.uniqueResult();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        } finally {
            closeCurrentSession(currentSession);
        }

    }

    @Override
    public void updateUserRole(UserRole userRole) {
        update(userRole);
    }

    @Override
    public void createUserRole(UserRole userRole) {
        this.persist(userRole);
    }

    @Override
    public boolean checkExistUserId(long userId) {
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<UserRole> root = query.from(UserRole.class);
            query.select(builder.count(root.get("userId")));
            query.where(builder.equal(root.get("userId"), userId));
            Long result = session.createQuery(query).getSingleResult();
            return result > 0L;
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        } finally {
            closeCurrentSession(session);
        }
    }

    public UserRole getUserRoleByUserId(long userId) {
        Session currentSession = openCurrentSession();
        try {
            CriteriaBuilder builder = currentSession.getCriteriaBuilder();
            CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
            Root<UserRole> root = query.from(UserRole.class);
            query.select(root);
            query.where(builder.equal(root.get("userId"), userId));
            Query<UserRole> q = currentSession.createQuery(query);
            return q.uniqueResult();
        } catch (Exception e) {
            LOGGER.error("Error get user role by user id " + userId);
            return null;
        } finally {
            closeCurrentSession(currentSession);
        }
    }

    private final static Logger LOGGER = Logger.getLogger(UserDaoImpl.class);
}
