package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.RoleDaoImpl;
import com.bkav.edoc.service.database.entity.Role;

public class RoleService {
    private final RoleDaoImpl roleDao = new RoleDaoImpl();

    public void createRole (Role role) {
        roleDao.openCurrentSession();
        roleDao.createRole(role);
        roleDao.closeCurrentSession();
    }

    public boolean checkExistRoleByRoleName(String roleName) {
        roleDao.openCurrentSession();
        boolean result = roleDao.checkExistRoleByRoleName(roleName);
        roleDao.closeCurrentSession();
        return result;
    }
}
