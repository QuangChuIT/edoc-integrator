package com.bkav.edoc.payload;

import java.util.List;

public class GetPendingDocResp extends BaseResp{
    private List<GetPendingResult> data;

    public List<GetPendingResult> getPendingResult() {
        return data;
    }

    public void setPendingResult(List<GetPendingResult> pendingResult) {
        this.data = pendingResult;
    }
}
