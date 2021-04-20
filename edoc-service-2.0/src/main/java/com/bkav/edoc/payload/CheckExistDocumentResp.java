package com.bkav.edoc.payload;

public class CheckExistDocumentResp extends BaseResp{
    private boolean isExist;

    public boolean getIsExist() {
        return isExist;
    }

    public void setResult(boolean result) {
        this.isExist = result;
    }
}
