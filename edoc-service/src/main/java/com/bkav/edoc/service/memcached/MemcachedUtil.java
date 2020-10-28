package com.bkav.edoc.service.memcached;

import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.util.PropsUtil;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MemcachedUtil {

    private static MemcachedClient memcachedClient = null;

    private static final Logger _log = Logger.getLogger(MemcachedUtil.class);

    private static final String KEY_NAMESPACE_PREFIX = "edoc_service_";

    private static MemcachedUtil INSTANCE;

    private static String _prefix = StringPool.BLANK;

    static public MemcachedUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MemcachedUtil();
        }
        return INSTANCE;
    }

    public MemcachedUtil() {
        try {
            List<InetSocketAddress> serverAddress = Collections
                    .singletonList(new InetSocketAddress("127.0.0.1", 11211));

            String address = PropsUtil.get(MemcachedKey.SERVER_KEY);
            if (address != null) {
                String[] temp = address.split(StringPool.COMMA);
                serverAddress = AddrUtil.getAddresses(Arrays
                        .asList(temp));

            }

            ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
            builder.setTranscoder(new ComplexSerializingTranscoder());

            memcachedClient = new MemcachedClient(builder.build(),
                    serverAddress);
            _prefix = PropsUtil.get("edxml.service.memcached.prefix");
        } catch (Exception e) {
            _log.error(e);
        }
    }

    public boolean create(String key, int timeLife, Object saveObject) {
        return add(key, timeLife, saveObject, false);
    }

    public Object update(String key, int timeLife, Object saveObject) {
        return add(key, timeLife, saveObject, true);
    }

    public Object read(String key) {

        Object result = null;
        try {
            if (memcachedClient != null) {

                String finalKey = buildKey(key);

                result = memcachedClient.get(finalKey);
            }
        } catch (Exception e) {
            _log.error(e);
        }

        return result;
    }

    public boolean delete(String key) {
        boolean iResult = false;
        try {
            if (memcachedClient != null) {

                String finalKey = buildKey(key);

                OperationFuture<Boolean> result = memcachedClient
                        .delete(finalKey);
                iResult = result.get(5L, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            _log.error(e);
        }

        return iResult;
    }

    private boolean add(String key, int timeLife, Object saveObject,
                        boolean isUpdate) {
        boolean iResult = false;
        try {

            if (memcachedClient != null) {

                String finalKey = buildKey(key);
                OperationFuture<Boolean> result = null;

                if (isUpdate) {
                    result = memcachedClient
                            .set(finalKey, timeLife, saveObject);
                } else {
                    result = memcachedClient
                            .add(finalKey, timeLife, saveObject);
                }
                if (result == null) {
                    return false;
                }

                try {

                    iResult = result.get(5L, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    result.cancel();
                }
            }

        } catch (Exception e) {
            _log.error(e);
        }
        return iResult;
    }

    private static String buildKey(String key) {
        StringBuilder keyBuilder = new StringBuilder(_prefix);
        keyBuilder.append(StringPool.UNDERLINE);
        keyBuilder.append(KEY_NAMESPACE_PREFIX);
        keyBuilder.append(key);
        return keyBuilder.toString();
    }
}
