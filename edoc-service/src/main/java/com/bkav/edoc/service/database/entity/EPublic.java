package com.bkav.edoc.service.database.entity;

import java.io.Serializable;

public class EPublic implements Serializable {
    private long total;
    private long totalOrgan;
    private String dateTime;

    public EPublic() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalOrgan() {
        return totalOrgan;
    }

    public void setTotalOrgan(long totalOrgan) {
        this.totalOrgan = totalOrgan;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
