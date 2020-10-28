package com.bkav.edoc.service.redis;

import com.bkav.edoc.service.kernel.string.StringPool;
import com.bkav.edoc.service.kernel.util.GetterUtil;
import com.bkav.edoc.service.util.PropsUtil;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.time.Duration;

public class RedisUtil {

    private static RedisUtil INSTANCE;

    // redis connection pool
    private static JedisPool jedisPool;

    //address of redis server
    private static final String redisHost;
    private static final int redisPort;
    private static final int timeout;

    private final Gson gson = new Gson();


    static public RedisUtil getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new RedisUtil();
        }
        return INSTANCE;
    }

    private static String buildKey(String key) {
        StringBuilder keyBuilder = new StringBuilder(String.valueOf(System.nanoTime()));
        keyBuilder.append(StringPool.UNDERLINE);
        keyBuilder.append(KEY_NAMESPACE_PREFIX);
        keyBuilder.append(key);
        return keyBuilder.toString();
    }

    private static final String KEY_NAMESPACE_PREFIX = "edoc_service_";

    private RedisUtil() {
        //configure pool connection
        jedisPool = new JedisPool(buildPoolConfig(), redisHost, redisPort, timeout);
    }

    public static void main(String[] args) {
        Test test = new Test(1, "abc");
        RedisUtil.getInstance().set("entry-1", test);
        try {
            Test entry = RedisUtil.getInstance().get("entry-1", Test.class);
            System.out.println(entry);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public <T> T get(String key, Class<T> classOfT) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            return gson.fromJson(json, classOfT);
        } catch (JedisException e) {
            LOGGER.error("Error when get data from redis cache " + e);
        }
        return null;
    }

    public boolean set(String key, Object value) {
        Jedis jedis = jedisPool.getResource();
        boolean result = false;
        try {
            String json = gson.toJson(value);
            result = jedis.set(key, json).equals("OK");
        } catch (JedisException e) {
            LOGGER.error("Error when set data to redis " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public boolean delete(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = -1L;
        try {
            result = jedis.del(key);
        } catch (JedisException e) {
            LOGGER.error(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result > 0L;
    }

    public boolean clearAllCache() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.flushAll().equals("OK");
        } catch (JedisException e) {
            LOGGER.error("Error when clear all cached !!! " + e.getMessage());
            return false;
        }
    }

    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    private static final Logger LOGGER = Logger.getLogger(RedisUtil.class);

    static {
        redisHost = PropsUtil.get("eDoc.service.redis.host");
        redisPort = GetterUtil.getInteger(PropsUtil.get("eDoc.service.redis.port"), 6379);
        timeout = GetterUtil.getInteger(PropsUtil.get("eDoc.redis.timeout"), 0);
    }
}
