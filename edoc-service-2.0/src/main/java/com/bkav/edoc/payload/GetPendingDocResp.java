package com.bkav.edoc.payload;

import java.util.List;

public class GetPendingDocResp extends BaseResp{
    private List<GetPendingResult> pendingResult;

    public List<GetPendingResult> getPendingResult() {
        return pendingResult;
    }

    public void setPendingResult(List<GetPendingResult> pendingResult) {
        this.pendingResult = pendingResult;
    }
}
