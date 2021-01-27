package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class ReceiverList extends BaseElement {
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

    public static ReceiverList fromContent(Element element) {
        ReceiverList receiverList = new ReceiverList();
        List<Element> list = element.getChildren();
        if (list != null && list.size() != 0) {
            for (Element ele : list) {
                if ("Receiver".equals(ele.getName())) {
                    receiverList.addReceiver(Receiver.fromContent(ele));
                }
            }
        }
        return receiverList;
    }

    public void accumulate(Element element) {
        Element receiverList = this.accumulate(element, "ReceiverList");
        if (this.receiver != null && !this.receiver.isEmpty()) {
            for (Receiver receiver : this.receiver) {
                receiver.accumulate(receiverList);
            }

        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Receiver", this.receiver).toString();
    }
}
