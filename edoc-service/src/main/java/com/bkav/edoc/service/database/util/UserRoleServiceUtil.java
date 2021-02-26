package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.entity.UserRole;
import com.bkav.edoc.service.database.services.UserRoleService;

import java.util.List;

public class UserRoleServiceUtil {
    private final static UserRoleService USER_ROLE_SERVICE = new UserRoleService();

    public static List<UserRole> getAllUserRole(){
        return USER_ROLE_SERVICE.findAll();
    }

    public static UserRole getRoleByUserId(long userId) {
        return USER_ROLE_SERVICE.getRoleByUserId(userId);
    }

    public static void createUserRole(UserRole userRole) {
        USER_ROLE_SERVICE.createUserRole(userRole);
    }

    public static boolean checkExistUserRole(long userId) {
        return USER_ROLE_SERVICE.checkExistUserId(userId);
    }

    public static UserRole getUserRoleByUser(User user) {
        return USER_ROLE_SERVICE.getUserRoleByUser(user);
    }

    public static void updateUserRole(UserRole userRole) {
        USER_ROLE_SERVICE.updateUserRole(userRole);
    }
}
