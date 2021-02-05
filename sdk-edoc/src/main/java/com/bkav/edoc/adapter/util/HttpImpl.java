package com.bkav.edoc.adapter.util;

import com.bkav.edoc.adapter.EdocProperties;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.*;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpImpl implements Http {
    private static String host = "";
    private static int port = 80;
    private static CloseableHttpClient httpClient;

    public HttpImpl(EdocProperties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Edoc properties must initialize");
        }
        try {
            URL url = new URL(properties.getEndpoint());
            host = url.getHost();
            port = url.getPort();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    public void initialize(List<Header> headers) {
        if (httpClient != null) {
            return;
        }
        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(requestWriterFactory, new DefaultHttpResponseParserFactory());
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        // Create a connection manager with custom configuration.
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory);
        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setSocketConfig(new HttpHost(host, port), socketConfig);
        // Validate connections after 1 sec of inactivity
        connManager.setValidateAfterInactivity(1000);
        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(1000)
                .setMaxLineLength(20000000)
                .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(PropsValues.HTTP_CLIENT_POOLING_MAX_TOTAL);
        connManager.setDefaultMaxPerRoute(PropsValues.HTTP_CLIENT_POOLING_MAX_PER_ROUTE);
        // Use custom cookie store if necessary.
        CookieStore cookieStore = new BasicCookieStore();
        // Use custom credentials provider if necessary.
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // Create global request configuration
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(PropsValues.HTTP_CLIENT_POOLING_CONNECTION_TIMEOUT)
                .setSocketTimeout(PropsValues.HTTP_CLIENT_POOLING_SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(PropsValues.HTTP_CLIENT_POOLING_REQUEST_TIMEOUT)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Collections.singletonList(AuthSchemes.BASIC))
                .build();
        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultHeaders(headers)
                .setRetryHandler(retryHandler())
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

    }

    @Override
    public String sendGet(String url, Map<String, String> params, List<Header> headers) {
        initialize(headers);
        String result = "";
        CloseableHttpResponse closeableHttpResponse;
        HttpGet httpGet;

        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = uriBuilder.build();
            httpGet = new HttpGet(uri);
            System.out.println("HttpGet URI: " + httpGet.getURI());
            closeableHttpResponse = httpClient.execute(httpGet);
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            System.out.println("Send Get request to url " + url);
            System.out.println("Status code response : " + statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = closeableHttpResponse.getEntity();
                if (httpEntity != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent(), StandardCharsets.UTF_8);
                    BufferedReader rd = new BufferedReader(inputStreamReader);
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        buffer.append(line);
                    }
                    result = buffer.toString();
                }
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String sendPost(String url, Map<String, String> params, List<Header> headers) {
        initialize(headers);
        CloseableHttpResponse closeableHttpResponse;
        HttpPost httpPost;
        String result = "";
        try {
            httpPost = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
            httpPost.setEntity(urlEncodedFormEntity);
            System.out.println("Send post request to URL: " + url);
            closeableHttpResponse = httpClient.execute(httpPost);
            System.out.println("Post parameters: " + httpPost.getEntity());
            System.out.println("Response status code: " + closeableHttpResponse.getStatusLine().getStatusCode());
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            if (httpEntity != null) {
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String sendPostMultiplePart(String url, Map<String, String> params, List<Header> headers, InputStream inputStream) {
        String result = "";
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            MultipartUtility multipartUtility = new MultipartUtility(uriBuilder.toString(), StandardCharsets.UTF_8.name(), headers);
            multipartUtility.addInputStreamPart("file", inputStream);
            result = multipartUtility.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static HttpRequestRetryHandler retryHandler() {
        return (exception, executionCount, context) -> {
            System.out.println("Retry handler :" + executionCount);
            if (executionCount >= 5) {
                return false;
            } else if (exception instanceof InterruptedIOException) {
                return false;
            } else if (exception instanceof UnknownHostException) {
                return false;
            } else {
                return !(exception instanceof SSLException);
            }
        };
    }
}
