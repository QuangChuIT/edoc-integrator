package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;
import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.daoimpl.UserDaoImpl;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
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
            userDao.openCurrentSession();
            User user = userDao.findByUsername(username);
            if (user != null) {
                userCacheEntry = MapperUtil.modelToUserCache(user);
                MemcachedUtil.getInstance().create(cachedKey, MemcachedKey.SEND_DOCUMENT_TIME_LIFE, userCacheEntry);
            }
            userDao.closeCurrentSession();
        }

        return userCacheEntry;
    }

    public boolean checkExist(String username) {
        userDao.openCurrentSession();
        boolean result = userDao.checkExist(username);
        userDao.closeCurrentSession();
        return result;
    }

    public User findUserById(long userId) {
        userDao.openCurrentSession();
        User user = userDao.findById(userId);
        userDao.closeCurrentSession();
        return user;
    }

    public List<User> findAll() {
        userDao.openCurrentSession();
        List<User> users = userDao.findAll();
        userDao.closeCurrentSession();
        return users;
    }

    public void updateUser(User user) {
        userDao.openCurrentSession();
        userDao.updateUser(user);
        userDao.closeCurrentSession();
    }

    public void createUser(User user) {
        userDao.createUser(user);
    }

    public UserCacheEntry getUserById(long userId) {
        UserCacheEntry userCacheEntry;
        String cacheKey = MemcachedKey.getKey(String.valueOf(userId), MemcachedKey.DOCUMENT_KEY);
        userCacheEntry = (UserCacheEntry) MemcachedUtil.getInstance().read(cacheKey);
        if (userCacheEntry == null) {
            userDao.openCurrentSession();
            User user = userDao.findById(userId);
            if (user != null) {
                userCacheEntry = MapperUtil.modelToUserCache(user);
                MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.SEND_DOCUMENT_TIME_LIFE, userCacheEntry);
            }
            userDao.closeCurrentSession();
        }
        return userCacheEntry;
    }

    public List<UserCacheEntry> getUsers(boolean onSSO) {
        List<UserCacheEntry> userCacheEntries = new ArrayList<>();
        userDao.openCurrentSession();
        List<User> users = userDao.getUsers(onSSO);
        if (users.size() > 0) {
            for (User user : users) {
                UserCacheEntry userCacheEntry = MapperUtil.modelToUserCache(user);
                userCacheEntries.add(userCacheEntry);
            }
        }
        userDao.closeCurrentSession();
        return userCacheEntries;
    }

    public List<UserCacheEntry> getAllUsers() {
        List<UserCacheEntry> userCacheEntries;
        String cacheKey = RedisKey.getKey("", RedisKey.LIST_USER);
        RedisUtil.getInstance().delete(cacheKey);
        userCacheEntries = (List<UserCacheEntry>) RedisUtil.getInstance().get(cacheKey, Object.class);
        if (userCacheEntries == null || userCacheEntries.size() == 0) {
            userCacheEntries = new ArrayList<>();
            userDao.openCurrentSession();
            List<User> users = userDao.getAllUser();
            if (users.size() > 0) {
                for (User user : users) {
                    UserCacheEntry userCacheEntry = MapperUtil.modelToUserCache(user);
                    userCacheEntries.add(userCacheEntry);
                }
                RedisUtil.getInstance().set(cacheKey, userCacheEntries);
            }
            userDao.closeCurrentSession();
        }
        return userCacheEntries;
    }

    public User getUserByOrgan(EdocDynamicContact organ) {
        userDao.openCurrentSession();
        User user = userDao.findByOrgan(organ);
        userDao.closeCurrentSession();
        return user;
    }

    public boolean deleteUser(long userId) {
        return userDao.deleteUser(userId);
    }
}
