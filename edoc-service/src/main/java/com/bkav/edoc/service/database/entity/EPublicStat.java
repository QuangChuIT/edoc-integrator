package com.bkav.edoc.service.database.entity;

import com.bkav.edoc.service.database.cache.OrganizationCacheEntry;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class EPublicStat implements Serializable {

    private String organDomain;
    private String organName;
    private Date lastUpdate;
    private long sent;
    private long received;
    private long total;
    private Set<EPublicStat> childOrgan;

    public EPublicStat() {
    }

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

    public long getSent() {
        return sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }

    public long getReceived() {
        return received;
    }

    public void setReceived(long received) {
        this.received = received;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Set<EPublicStat> getChildOrgan() {
        return childOrgan;
    }

    public void setChildOrgan(Set<EPublicStat> childOrgan) {
        this.childOrgan = childOrgan;
    }
}
