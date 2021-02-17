package com.bkav.edoc.sdk.util;

public class PropsValues {

    public static final int HTTP_CLIENT_POOLING_MAX_TOTAL = GetterUtil.getInteger(PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_MAX_TOTAL));

    public static final int HTTP_CLIENT_POOLING_MAX_PER_ROUTE = GetterUtil.getInteger(PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_MAX_PER_ROUTE));

    public static final int HTTP_CLIENT_POOLING_CONNECTION_TIMEOUT = GetterUtil.getInteger(PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_CONNECTION_TIMEOUT));

    public static final int HTTP_CLIENT_POOLING_SOCKET_TIMEOUT = GetterUtil.getInteger(PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_SOCKET_TIMEOUT));

    public static final int HTTP_CLIENT_POOLING_REQUEST_TIMEOUT = GetterUtil.getInteger(PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_REQUEST_TIMEOUT));

    public static final int HTTP_CLIENT_POOLING_KEEPALIVE = GetterUtil.getInteger(PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_KEEPALIVE));

    public static final int HTTP_CLIENT_POOLING_CLOSE_IDLE = GetterUtil.getInteger(PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_CLOSE_IDLE));

    public static final String HTTP_CLIENT_POOLING_USER_AGENT = PropsUtils.get(PropsKeys.HTTP_CLIENT_POOLING_USER_AGENT);

}
