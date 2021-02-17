package com.bkav.edoc.sdk.util;

import org.apache.http.Header;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface Http {
    String sendGet(String url, Map<String, String> params, List<Header> headers);

    String sendPost(String url, Map<String, String> params, List<Header> headers);

    String sendPostMultiplePart(String url, Map<String, String> params, List<Header> headers, InputStream inputStream);


}
