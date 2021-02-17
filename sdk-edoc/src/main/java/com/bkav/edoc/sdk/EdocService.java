package com.bkav.edoc.sdk;

import com.bkav.edoc.sdk.entity.*;
import com.bkav.edoc.sdk.util.ContentTypes;
import com.bkav.edoc.sdk.util.FileUtils;
import com.bkav.edoc.sdk.util.HttpImpl;
import com.bkav.edoc.sdk.util.ShaUtils;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EdocService {
    private final Gson gson = new Gson();
    private String organId;
    private String token;
    private String user_agent = "EdocClient/Bkav";
    private String storeFilePath = "";
    private String endpoint;
    private Map<String, String> headers;
    private HttpImpl http;
    private EdocProperties edocProperties;

    public EdocService(EdocProperties config) {
        if (config == null) {
            throw new IllegalArgumentException("Edoc Client config must initialize");
        }
        this.headers = new HashMap<>();
        this.edocProperties = config;
        this.endpoint = this.edocProperties.getEndpoint();
        this.organId = this.edocProperties.getOrganId();
        this.token = this.edocProperties.getToken();
        this.storeFilePath = this.edocProperties.getStoreFilePath();
        this.headers.put("organId", this.organId);
        this.headers.put("Authorization", this.token);
        this.headers.put("user_agent", this.user_agent);
        this.http = new HttpImpl(this.edocProperties);
    }

    public GetOrganizationResp getOrganization() {
        String url = this.endpoint + "/getOrganizations";
        this.headers.put("Content-Type", ContentTypes.APPLICATION_JSON);
        GetOrganizationResp getOrganizationResp = null;
        try {
            String json = this.http.sendGet(url, null, this.getListHeader());
            getOrganizationResp = gson.fromJson(json, GetOrganizationResp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getOrganizationResp;
    }

    public List<Header> getListHeader() {
        List<Header> headers = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            headers.add(new BasicHeader(entry.getKey(), entry.getValue()));
        }
        return headers;
    }

    public GetPendingDocIDsResp getPendingDocIds(String request) {
        String url = this.endpoint + "/getPendingDocIds";
        this.headers.put("Content-Type", ContentTypes.APPLICATION_JSON);
        GetPendingDocIDsResp getPendingDocIDsResp = null;
        try {
            JSONObject jsonObject = new JSONObject(request);
            jsonObject.keySet().forEach(k -> {
                Object kv = jsonObject.get(k);
                this.headers.put(k, String.valueOf(kv));
            });
            String json = this.http.sendGet(url, null, this.getListHeader());
            getPendingDocIDsResp = gson.fromJson(json, GetPendingDocIDsResp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getPendingDocIDsResp;
    }

    public SendDocResp sendDocument(String request, String edocFilePath) {
        String url = this.endpoint + "/sendDocument";
        this.headers.put("Content-Type", ContentTypes.APPLICATION_OCTET_STREAM);
        SendDocResp sendDocResp = null;
        try {
            JSONObject jsonObject = new JSONObject(request);
            jsonObject.keySet().forEach(k -> {
                Object kv = jsonObject.get(k);
                this.headers.put(k, String.valueOf(kv));
            });
            String hashFile = ShaUtils.generateSHA256(edocFilePath);
            this.headers.put("hash-edoc", hashFile);
            FileInputStream inputStream = new FileInputStream(edocFilePath);
            String content = this.http.sendPostMultiplePart(url, null, this.getListHeader(), inputStream);
            sendDocResp = gson.fromJson(content, SendDocResp.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sendDocResp;
    }

    public GetDocumentResp getDocument(String request) {
        String url = this.endpoint + "/getDocument";
        GetDocumentResp getDocumentResp = null;
        try {
            this.headers.put("Content-Type", ContentTypes.APPLICATION_JSON);
            JSONObject jsonObject = new JSONObject(request);
            jsonObject.keySet().forEach(k -> {
                Object kv = jsonObject.get(k);
                this.headers.put(k, String.valueOf(kv));
            });
            String docId = jsonObject.getString("docId");
            String content = this.http.sendPost(url, null, this.getListHeader());
            getDocumentResp = gson.fromJson(content, GetDocumentResp.class);
            if (getDocumentResp.getCode().equals("0")) {
                String data = new String(Base64.decodeBase64(getDocumentResp.getData()), StandardCharsets.UTF_8.name());
                Calendar cal = Calendar.getInstance();
                String SEPARATOR = File.separator;
                String storePathDir = this.storeFilePath;

                String dataPath = storePathDir.endsWith("/") ? storePathDir : storePathDir + "/" +
                        cal.get(Calendar.YEAR) + SEPARATOR +
                        (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                        cal.get(Calendar.DAY_OF_MONTH);
                if (!FileUtils.exist(dataPath)) {
                    FileUtils.mkdirs(dataPath);
                }

                String specDataPath = dataPath + SEPARATOR + EdocConstant.GET_DOCUMENT + this.organId + "_" + docId + ".edxml";
                File file = new File(specDataPath);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getDocumentResp;
    }

    public ConfirmReceivedResp confirmReceived(String request) {
        String url = this.endpoint + "/confirmReceived";
        ConfirmReceivedResp confirmReceivedResp = null;
        try {
            this.headers.put("Content-Type", ContentTypes.APPLICATION_JSON);
            JSONObject jsonObject = new JSONObject(request);
            jsonObject.keySet().forEach(k -> {
                Object kv = jsonObject.get(k);
                this.headers.put(k, String.valueOf(kv));
            });
            String content = this.http.sendPost(url, null, this.getListHeader());
            System.out.println(content);
            confirmReceivedResp = gson.fromJson(content, ConfirmReceivedResp.class);
        } catch (Exception e) {
            confirmReceivedResp = new ConfirmReceivedResp();
            confirmReceivedResp.setCode("9999");
            confirmReceivedResp.setErrors(new ArrayList<>());
            confirmReceivedResp.setStatus("Error");
            e.printStackTrace();
        }
        return confirmReceivedResp;
    }
}
