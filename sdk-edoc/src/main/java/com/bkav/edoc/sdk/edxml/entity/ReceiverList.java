package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class ReceiverList {
    private List<Receiver> receiver;

    public ReceiverList() {
    }

    public List<Receiver> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<Receiver> paramList) {
        this.receiver = paramList;
    }

    public void addReceiver(Receiver receiver) {
        if (this.receiver == null) {
            this.receiver = new ArrayList();
        }

        this.receiver.add(receiver);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Receiver", this.receiver).toString();
    }
}
