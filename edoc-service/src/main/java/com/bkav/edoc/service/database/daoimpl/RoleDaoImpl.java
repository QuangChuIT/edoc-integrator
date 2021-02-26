package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.RoleDao;
import com.bkav.edoc.service.database.entity.Role;
import com.bkav.edoc.service.database.entity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RoleDaoImpl extends RootDaoImpl<Role, Long> implements RoleDao {

    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public void createRole(Role role) {
        this.persist(role);
    }

    @Override
    public boolean checkExistRoleByRoleName(String roleName) {
        Session session = openCurrentSession();
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<Role> root = query.from(Role.class);
            query.select(builder.count(root.get("roleName")));
            query.where(builder.equal(root.get("roleName"), roleName));
            Long result = session.createQuery(query).getSingleResult();
            return result > 0L;
        } catch (Exception e){
            LOGGER.error(e);
            return false;
        } finally {
            closeCurrentSession(session);
        }
    }

    public Role getRoleByRoleName(String roleName) {
        Session currentSession = openCurrentSession();
        try {
            CriteriaBuilder builder = currentSession.getCriteriaBuilder();
            CriteriaQuery<Role> query = builder.createQuery(Role.class);
            Root<Role> root = query.from(Role.class);
            query.select(root);
            query.where(builder.equal(root.get("roleName"), roleName));
            Query<Role> q = currentSession.createQuery(query);
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


    private final static Logger LOGGER = Logger.getLogger(RoleDaoImpl.class);
}
