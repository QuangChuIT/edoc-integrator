package com.bkav.edoc.service.database.entity;

import java.io.Serializable;
import java.util.Date;

public class EPublicStatisticDetail implements Serializable {
    private String organDomain;
    private String organName;
    private Date lastUpdate;
    private long sentExt;
    private long sentInt;
    private long receivedExt;
    private long receivedInt;
    private long signed;
    private long notSigned;
    private long total;

    public String getOrganDomain() {
        return organDomain;
    }

    public void setOrganDomain(String organDomain) {
        this.organDomain = organDomain;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getSentExt() {
        return sentExt;
    }

    public void setSentExt(long sentExt) {
        this.sentExt = sentExt;
    }

    public long getSentInt() {
        return sentInt;
    }

    public void setSentInt(long sentInt) {
        this.sentInt = sentInt;
    }

    public long getReceivedExt() {
        return receivedExt;
    }

    public void setReceivedExt(long receivedExt) {
        this.receivedExt = receivedExt;
    }

    public long getReceivedInt() {
        return receivedInt;
    }

    public void setReceivedInt(long receivedInt) {
        this.receivedInt = receivedInt;
    }

    public long getSigned() {
        return signed;
    }

    public void setSigned(long signed) {
        this.signed = signed;
    }

    public long getNotSigned() {
        return notSigned;
    }

    public void setNotSigned(long notSigned) {
        this.notSigned = notSigned;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
