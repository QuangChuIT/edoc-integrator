package com.bkav.edoc.adapter;


public class EdocProperties {
    private String endpoint = "";
    private String organId = "";
    private String token = "";
    private String storeFilePath = "";

    public EdocProperties() {
    }

    public EdocProperties(String endpoint, String organId, String token, String storeFilePath) {
        this.endpoint = endpoint;
        this.organId = organId;
        this.token = token;
        this.storeFilePath = storeFilePath;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStoreFilePath() {
        return storeFilePath;
    }

    public void setStoreFilePath(String storeFilePath) {
        this.storeFilePath = storeFilePath;
    }

}
