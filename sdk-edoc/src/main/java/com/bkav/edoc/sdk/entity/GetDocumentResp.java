package com.bkav.edoc.sdk.entity;

public class GetDocumentResp extends BaseResp {
    private String data;

    public GetDocumentResp() {

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetDocumentResp{" +
                ", data='" + data + '\'' +
                '}';
    }
}
