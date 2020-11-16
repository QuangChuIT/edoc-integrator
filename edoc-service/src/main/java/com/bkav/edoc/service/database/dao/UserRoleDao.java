package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.UserRole;

import java.util.List;

public interface UserRoleDao {
    List<UserRole> getUserRole();

    UserRole getRoleByUserId(long userID);

    void updateUserRole(UserRole userRole);

    void createUserRole(UserRole userRole);

    boolean checkExistUserId (long userId);

    UserRole getUserRoleByUserId(long userId);

}
