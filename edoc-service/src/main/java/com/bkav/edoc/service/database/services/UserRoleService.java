package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.UserRoleDaoImpl;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.entity.UserRole;
import com.bkav.edoc.service.database.util.UserServiceUtil;

import java.util.List;

public class UserRoleService {
    private final UserRoleDaoImpl userRoleDao = new UserRoleDaoImpl();

    public List<UserRole> findAll() {
        return userRoleDao.findAll();
    }

    public UserRole getRoleByUserId(long userId) {
        User user = UserServiceUtil.findUserById(userId);
        return userRoleDao.getRoleByUser(user);
    }

    public void createUserRole(UserRole userRole) {
        userRoleDao.createUserRole(userRole);
    }

    public boolean checkExistUserId(long userId) {
        return userRoleDao.checkExistUserId(userId);
    }

    public UserRole getUserRoleByUser(User user) {
        return userRoleDao.getUserRoleByUser(user);
    }

    public void updateUserRole(UserRole userRole) {
        userRoleDao.updateUserRole(userRole);
    }
}
