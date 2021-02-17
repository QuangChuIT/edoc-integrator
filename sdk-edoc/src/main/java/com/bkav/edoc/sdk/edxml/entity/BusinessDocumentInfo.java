package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class BusinessDocumentInfo extends CommonElement implements IElement<BusinessDocumentInfo> {

    private String documentInfo;
    private String documentReceiver;
    private ReceiverList receiverList;

    public BusinessDocumentInfo() {
    }

    public String getDocumentInfo() {
        return documentInfo;
    }

    public void setDocumentInfo(String documentInfo) {
        this.documentInfo = documentInfo;
    }

    public String getDocumentReceiver() {
        return documentReceiver;
    }

    public void setDocumentReceiver(String documentReceiver) {
        this.documentReceiver = documentReceiver;
    }

    public ReceiverList getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(ReceiverList receiverList) {
        this.receiverList = receiverList;
    }

    public static BusinessDocumentInfo fromContent(Element elementNode) {
        BusinessDocumentInfo businessDocumentInfo = new BusinessDocumentInfo();
        List<Element> elementList = elementNode.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element element : elementList) {
                if ("DocumentInfo".equals(element.getName())) {
                    businessDocumentInfo.setDocumentInfo(element.getText());
                }

                if ("DocumentReceiver".equals(element.getName())) {
                    businessDocumentInfo.setDocumentReceiver(element.getText());
                }

                if ("ReceiverList".equals(element.getName())) {
                    businessDocumentInfo.setReceiverList(new ReceiverList().getData(element));
                }
            }

        }
        return businessDocumentInfo;
    }

    public void accumulate(Element element) {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("DocumentInfo", this.documentInfo)
                .add("DocumentReceiver", this.documentReceiver).add("ReceiverList", this.receiverList).toString();
    }

    @Override
    public void createElement(Element element) {
        Element receiveElement = this.createElement(element, "BussinessDocumentInfo");
        this.createElement(receiveElement, "DocumentInfo", this.documentInfo);
        this.createElement(receiveElement, "DocumentReceiver", this.documentReceiver);
        this.receiverList.createElement(receiveElement);
    }

    @Override
    public BusinessDocumentInfo getData(Element element) {
        return null;
    }
}
