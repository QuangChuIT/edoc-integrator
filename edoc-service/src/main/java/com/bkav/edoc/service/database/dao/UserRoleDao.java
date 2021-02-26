package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.entity.UserRole;

import java.util.List;

public interface UserRoleDao {
    List<UserRole> getUserRole();

    UserRole getRoleByUser(User user);

    void updateUserRole(UserRole userRole);

    void createUserRole(UserRole userRole);

    boolean checkExistUserId (long userId);

    UserRole getUserRoleByUser(User user);

}
