package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.RoleDaoImpl;
import com.bkav.edoc.service.database.entity.Role;

public class RoleService {
    private final RoleDaoImpl roleDao = new RoleDaoImpl();

    public void createRole(Role role) {
        roleDao.createRole(role);
    }

    public boolean checkExistRoleByRoleName(String roleName) {
        return roleDao.checkExistRoleByRoleName(roleName);
    }

    public Role getRoleById(long roleId) {
        return roleDao.findById(roleId);
    }

    public Role getRoleByRoleName(String roleName) {
        return roleDao.getRoleByRoleName(roleName);
    }
}
