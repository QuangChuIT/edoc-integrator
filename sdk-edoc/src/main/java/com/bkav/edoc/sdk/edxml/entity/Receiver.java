package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class Receiver extends CommonElement implements IElement<Receiver> {
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("OrganId", this.organId).add("ReceiverType", this.receiverType).toString();
    }

    @Override
    public void createElement(Element element) {
        Element receiver = this.createElement(element, "Receiver");
        this.createElement(receiver, "OrganId", this.organId);
        this.createElement(receiver, "ReceiverType", this.receiverType);
    }

    @Override
    public Receiver getData(Element element) {
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
}
