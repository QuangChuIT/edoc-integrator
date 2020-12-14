package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.Role;

public interface RoleDao {
    void createRole(Role role);

    boolean checkExistRoleByRoleName(String roleName);

    Role getRoleByRoleName(String roleName);
}
