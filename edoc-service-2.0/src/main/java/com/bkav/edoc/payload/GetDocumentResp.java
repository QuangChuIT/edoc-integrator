package com.bkav.edoc.payload;

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
}
