package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.UserRoleDaoImpl;
import com.bkav.edoc.service.database.entity.UserRole;

import java.util.List;

public class UserRoleService {
    private UserRoleDaoImpl userRoleDao = new UserRoleDaoImpl();

    public List<UserRole> findAll() {
        userRoleDao.openCurrentSession();
        List<UserRole> userRoles = userRoleDao.findAll();
        userRoleDao.closeCurrentSession();
        return userRoles;
    }

    public UserRole getRoleByUserId(long userId) {
        userRoleDao.openCurrentSession();
        UserRole userRole = userRoleDao.getRoleByUserId(userId);
        userRoleDao.closeCurrentSession();
        return userRole;
    }

    public void createUserRole(UserRole userRole) {
        userRoleDao.openCurrentSession();
        userRoleDao.createUserRole(userRole);
        userRoleDao.closeCurrentSession();
    }

    public boolean checkExistUserId(long userId) {
        userRoleDao.openCurrentSession();
        boolean result = userRoleDao.checkExistUserId(userId);
        userRoleDao.closeCurrentSession();
        return result;
    }

    public UserRole getUserRoleByUserId(long userId) {
        userRoleDao.openCurrentSession();
        UserRole userRole = userRoleDao.getUserRoleByUserId(userId);
        userRoleDao.closeCurrentSession();
        return userRole;
    }

    public void updateUserRole(UserRole userRole) {
        userRoleDao.openCurrentSession();
        userRoleDao.updateUserRole(userRole);
        userRoleDao.closeCurrentSession();
    }
}
