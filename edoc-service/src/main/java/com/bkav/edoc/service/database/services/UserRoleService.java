package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.UserRoleDaoImpl;
import com.bkav.edoc.service.database.entity.UserRole;

import java.util.List;

public class UserRoleService {
    private final UserRoleDaoImpl userRoleDao = new UserRoleDaoImpl();

    public List<UserRole> findAll() {
        return userRoleDao.findAll();
    }

    public UserRole getRoleByUserId(long userId) {
        return userRoleDao.getRoleByUserId(userId);
    }

    public void createUserRole(UserRole userRole) {
        userRoleDao.createUserRole(userRole);
    }

    public boolean checkExistUserId(long userId) {
        return userRoleDao.checkExistUserId(userId);
    }

    public UserRole getUserRoleByUserId(long userId) {
        return userRoleDao.getUserRoleByUserId(userId);
    }

    public void updateUserRole(UserRole userRole) {
        userRoleDao.updateUserRole(userRole);
    }
}
