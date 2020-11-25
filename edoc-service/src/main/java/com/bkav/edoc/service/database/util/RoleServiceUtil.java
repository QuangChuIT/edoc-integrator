package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.Role;
import com.bkav.edoc.service.database.services.RoleService;

public class RoleServiceUtil {
    private static final RoleService ROLE_SERVICE = new RoleService();

    public static void createRole (Role role) {
        ROLE_SERVICE.createRole(role);
    }

    public static boolean checkExistRoleByRoleName(String roleName) {
        return ROLE_SERVICE.checkExistRoleByRoleName(roleName);
    }
}
