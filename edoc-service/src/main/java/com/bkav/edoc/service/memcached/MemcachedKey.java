package com.bkav.edoc.service.memcached;

public class MemcachedKey {

    //Time Life
    public final static int CHECK_PERMISSION_TIME_LIFE = 3600;

    public final static int SEND_DOCUMENT_TIME_LIFE = 604800;

    public final static int CHECK_ALLOW_TIME_LIFE = 604800;

    public final static int GET_ORGAN_NAME_BY_DOMAIN_TIME = 604800;

    public final static int GET_DOCUMENT_TIME_LIFE = 604800;

    public final static int GET_PENDING_TIME_LIFE = 604800;

    public final static String DOCUMENT_KEY = "Document_";

    public final static String DYNAMICCONTACT_KEY = "Contact_";

    public static final String PRIORITY_KEY = "Priority_";

    public final static String GLOBAL_IDS_FOR_TEST = "GlobalIdsForTest";

    public final static String[] DEFAULT_SERVER = new String[]{"127.0.0.1:11211"};

    public final static String SERVER_KEY = "edxml.service.memcached.address";

    public static String getKey(String finalKey, String methodPrefixKey) {
        StringBuilder resultKey = new StringBuilder();
        resultKey.append(methodPrefixKey);
        resultKey.append(finalKey);

        return resultKey.toString();
    }
}
