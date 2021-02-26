package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class ExcelDailyCounterHeader implements Serializable {
    private long id;
    private String headerName;

    public ExcelDailyCounterHeader() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
}
