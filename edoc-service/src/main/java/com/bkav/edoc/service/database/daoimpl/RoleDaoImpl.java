package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.RoleDao;
import com.bkav.edoc.service.database.entity.Role;
import org.apache.log4j.Logger;
import org.hibernate.Session;

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
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Role> root = query.from(Role.class);
        query.select(builder.count(root.get("roleName")));
        query.where(builder.equal(root.get("roleName"), roleName));
        Long result = session.createQuery(query).getSingleResult();
        closeCurrentSession(session);
        return result > 0L;
    }

    private final static Logger LOGGER = Logger.getLogger(RoleDaoImpl.class);
}
