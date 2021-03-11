package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.cache.UserCacheEntry;
import com.bkav.edoc.service.database.daoimpl.UserDaoImpl;
import com.bkav.edoc.service.database.entity.User;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;
import com.bkav.edoc.service.redis.RedisKey;
import com.bkav.edoc.service.redis.RedisUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public int changeUserPassword (String userName, String oldPassword, String newPassword, String url) throws KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException, IOException {
        String strBase64 = getBase64Encode(userName, oldPassword);
        int result = -1;
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                builder.build());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                sslConnectionSocketFactory).build();
        try {
            HttpPatch httpPatch = new HttpPatch(url);
            String jsonBody = "{\"schemas\": [\"urn:ietf:params:scim:api:messages:2.0:PatchOp\"],\"Operations\": [{\"op\": \"add\",\"value\": {\"password\": \"%s\"}}]}";
            jsonBody = String.format(jsonBody, newPassword);
            StringEntity entity = new StringEntity(jsonBody);
            httpPatch.setEntity(entity);
            httpPatch.setHeader("Authorization", "Basic " + strBase64);
            httpPatch.setHeader("Content-Type", "application/scim+json");
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);
            CloseableHttpResponse response = httpclient.execute(httpPatch);
            try {
                result = response.getStatusLine().getStatusCode();
                LOGGER.info("Successfully change user password to sso server " + result);
            } catch (Exception e) {
                LOGGER.error("Error change user password to sso server cause " + Arrays.toString(e.getStackTrace()));
            } finally {
                response.close();
            }
        } catch (Exception e) {
            LOGGER.error("Error change user password to sso server cause ", e);
        } finally {
            httpclient.close();
        }
        return result;
    }

    private String getBase64Encode(String userName, String password) {
        LOGGER.info("Get Base64Encode ...");
        String str = userName + ":" + password;
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public boolean deleteUser(long userId) {
        return userDao.deleteUser(userId);
    }

    private static final Logger LOGGER = Logger.getLogger(UserService.class);

}
