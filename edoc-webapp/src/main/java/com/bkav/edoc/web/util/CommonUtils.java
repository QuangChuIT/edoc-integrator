package com.bkav.edoc.web.util;

import com.bkav.edoc.service.database.entity.pagination.DataTableResult;
import com.bkav.edoc.service.database.entity.pagination.DatatableRequest;
import com.bkav.edoc.service.database.util.AppUtil;

import java.util.List;

public class CommonUtils<T> {
    public DataTableResult<T> getDataTableResult(DataTableResult<T> dataTableResult, List<T> values, int count, DatatableRequest<T> datatableRequest) {
        if (!AppUtil.isObjectEmpty(values)) {
            if (!AppUtil.isObjectEmpty(values)) {
                dataTableResult.setRecordsTotal(count);

                if (datatableRequest.getPaginationRequest().isFilterByEmpty()) {
                    dataTableResult.setRecordsFiltered(count);
                } else {
                    dataTableResult.setRecordsFiltered(values.size());
                }
            }
        }
        return dataTableResult;
    }
}
