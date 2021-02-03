package com.bkav.edoc.payload;

public class GetDocumentResp extends BaseResp {
    private String filePath;
    private String data;

    public GetDocumentResp() {

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
