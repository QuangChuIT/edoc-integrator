package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.daoimpl.UserDaoImpl;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserDaoImpl userDao = new UserDaoImpl();

    public UserCacheEntry findByUsername(String username) {
        UserCacheEntry userCacheEntry;
        String cachedKey = MemcachedKey.getKey(username, MemcachedKey.DOCUMENT_KEY);
        MemcachedUtil.getInstance().delete(cachedKey);
        userCacheEntry = (UserCacheEntry) MemcachedUtil.getInstance().read(cachedKey);
        if (userCacheEntry == null) {
            User user = userDao.findByUsername(username);
            if (user != null) {
                userCacheEntry = MapperUtil.modelToUserCache(user);
                MemcachedUtil.getInstance().create(cachedKey, MemcachedKey.SEND_DOCUMENT_TIME_LIFE, userCacheEntry);
            }
        }
        return userCacheEntry;
    }

    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public boolean checkExist(String username) {
        return userDao.checkExist(username);
    }

    public User findUserById(long userId) {
        return userDao.findById(userId);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public void createUser(User user) {
        userDao.createUser(user);
    }

    public UserCacheEntry getUserById(long userId) {
        UserCacheEntry userCacheEntry;
        String cacheKey = MemcachedKey.getKey(String.valueOf(userId), MemcachedKey.DOCUMENT_KEY);
        userCacheEntry = (UserCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
        if (userCacheEntry == null) {
            User user = userDao.findById(userId);
            if (user != null) {
                userCacheEntry = MapperUtil.modelToUserCache(user);
            }
        }
        return userCacheEntry;
    }

    public List<UserCacheEntry> getUsers(boolean onSSO) {
        List<UserCacheEntry> userCacheEntries = new ArrayList<>();
        List<User> users = userDao.getUsers(onSSO);
        if (users.size() > 0) {
            for (User user : users) {
                UserCacheEntry userCacheEntry = MapperUtil.modelToUserCache(user);
                userCacheEntries.add(userCacheEntry);
            }
        }
        return userCacheEntries;
    }

    public List<UserCacheEntry> getAllUsers() {
        List<UserCacheEntry> userCacheEntries;
        String cacheKey = RedisKey.getKey("", RedisKey.LIST_USER);
        RedisUtil.getInstance().delete(cacheKey);
        userCacheEntries = (List<UserCacheEntry>) RedisUtil.getInstance().get(cacheKey, Object.class);
        if (userCacheEntries == null || userCacheEntries.size() == 0) {
            userCacheEntries = new ArrayList<>();
            List<User> users = userDao.getAllUser();
            if (users.size() > 0) {
                for (User user : users) {
                    UserCacheEntry userCacheEntry = MapperUtil.modelToUserCache(user);
                    userCacheEntries.add(userCacheEntry);
                }
                RedisUtil.getInstance().set(cacheKey, userCacheEntries);
            }
        }
        return userCacheEntries;
    }

    public boolean deleteUser(long userId) {
        return userDao.deleteUser(userId);
    }
}
