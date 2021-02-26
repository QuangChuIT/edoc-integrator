package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.User;

import java.util.List;

public interface UserDao {

    User findByUsername(String username);

    List<User> getAllUser();

    void updateUser(User user);

    void createUser(User user);

    boolean checkExist(String username);

    List<User> getUsers(boolean onSSO);

    boolean deleteUser(long userId);
}
