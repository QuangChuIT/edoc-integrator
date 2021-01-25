package com.bkav.edoc.edxml.base.header;

import com.bkav.edoc.edxml.base.BaseElement;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class Receiver extends BaseElement {
    private String organId;
    private String receiverType;

    public Receiver() {
    }

    public Receiver(String organId, String receiverType) {
        this.organId = organId;
        this.receiverType = receiverType;
    }

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public static Receiver fromContent(Element element) {
        Receiver receiver = new Receiver();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {
            for (Element itemElement : elements) {
                if ("OrganId".equals(itemElement.getName())) {
                    receiver.setOrganId(itemElement.getText());
                }

                if ("ReceiverType".equals(itemElement.getName())) {
                    receiver.setReceiverType(itemElement.getText());
                }
            }

        }
        return receiver;
    }

    public void accumulate(Element receiverElement) {
        Element receiver = this.accumulate(receiverElement, "Receiver");
        this.accumulate(receiver, "OrganId", this.organId);
        this.accumulate(receiver, "ReceiverType", this.receiverType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId", this.organId).add("ReceiverType", this.receiverType).toString();
    }
}
