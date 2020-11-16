package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.UserRoleDao;
import com.bkav.edoc.service.database.entity.EdocNotification;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.entity.UserRole;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserRoleDaoImpl extends RootDaoImpl<UserRole, Long> implements UserRoleDao {

    public UserRoleDaoImpl() {
        super(UserRole.class);
    }

    @Override
    public List<UserRole> getUserRole() {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(root);
        Query<UserRole> q = session.createQuery(query);
        return q.getResultList();
    }

    @Override
    public UserRole getRoleByUserId(long userId) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(root);
        query.where(builder.equal(root.get("userId"), userId));
        Query<UserRole> q = currentSession.createQuery(query);
        return q.uniqueResult();
    }

    @Override
    public void updateUserRole(UserRole userRole) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();
            update(userRole);
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
    public void createUserRole(UserRole userRole) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();
            this.persist(userRole);
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
    public boolean checkExistUserId (long userId) {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(builder.count(root.get("userId")));
        query.where(builder.equal(root.get("userId"), userId));
        Long result = session.createQuery(query).getSingleResult();
        return result > 0L;
    }

    public UserRole getUserRoleByUserId(long userId) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(root);
        query.where(builder.equal(root.get("userId"), userId));
        Query<UserRole> q = currentSession.createQuery(query);
        return q.uniqueResult();
    }

    private final static Logger LOGGER = Logger.getLogger(UserDaoImpl.class);
}
