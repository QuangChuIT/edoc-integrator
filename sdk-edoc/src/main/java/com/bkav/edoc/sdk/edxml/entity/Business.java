package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;
import org.jdom2.Element;

import java.util.List;

public class Business {
    private String documentId;
    private long businessDocType;
    private String businessDocReason;
    private BusinessDocumentInfo businessDocumentInfo;
    private ReplacementInfoList replacementInfoList;
    private StaffInfo staffInfo;
    private int paper;

    public Business() {
    }

    public Business(String documentId, long businessDocType, String businessDocReason, BusinessDocumentInfo businessDocumentInfo, StaffInfo staffInfo, int paper) {
        this.documentId = documentId;
        this.businessDocType = businessDocType;
        this.businessDocReason = businessDocReason;
        this.businessDocumentInfo = businessDocumentInfo;
        this.staffInfo = staffInfo;
        this.paper = paper;
    }

    public Business(String documentId, long businessDocType, String businessDocReason, BusinessDocumentInfo businessDocumentInfo, int paper) {
        this.documentId = documentId;
        this.businessDocType = businessDocType;
        this.businessDocReason = businessDocReason;
        this.businessDocumentInfo = businessDocumentInfo;
        this.paper = paper;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public long getBusinessDocType() {
        return businessDocType;
    }

    public void setBusinessDocType(long businessDocType) {
        this.businessDocType = businessDocType;
    }

    public String getBusinessDocReason() {
        return businessDocReason;
    }

    public void setBusinessDocReason(String businessDocReason) {
        this.businessDocReason = businessDocReason;
    }

    public BusinessDocumentInfo getBusinessDocumentInfo() {
        return businessDocumentInfo;
    }

    public void setBusinessDocumentInfo(BusinessDocumentInfo businessDocumentInfo) {
        this.businessDocumentInfo = businessDocumentInfo;
    }

    public ReplacementInfoList getReplacementInfoList() {
        return replacementInfoList;
    }

    public void setReplacementInfoList(ReplacementInfoList replacementInfoList) {
        this.replacementInfoList = replacementInfoList;
    }

    public StaffInfo getStaffInfo() {
        return staffInfo;
    }

    public void setStaffInfo(StaffInfo staffInfo) {
        this.staffInfo = staffInfo;
    }

    public int getPaper() {
        return paper;
    }

    public void setPaper(int paper) {
        this.paper = paper;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("DocumentId", this.documentId).
                add("BussinessDocType", this.businessDocType).add("BussinessDocReason",
                this.businessDocReason).add("Paper", this.paper).add("BussinessDocumentInfo",
                this.businessDocumentInfo).add("ReplacementInfoList",
                this.replacementInfoList).add("StaffInfo", this.staffInfo).toString();
    }

    public static Business getData(Element businessElement) {
        Business business = new Business();
        List<Element> elementList = businessElement.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element childElement : elementList) {
                if ("DocumentId".equals(childElement.getName())) {
                    business.setDocumentId(childElement.getText());
                }
                if ("BusinessDocType".equals(childElement.getName()) || "BussinessDocType".equals(childElement.getName())) {
                    business.setBusinessDocType(Long.parseLong(childElement.getText()));
                }
                if ("Paper".equals(childElement.getName())) {
                    business.setPaper(Integer.parseInt(childElement.getText()));
                }
                if ("BusinessDocReason".equals(childElement.getName()) || "BussinessDocReason".equals(childElement.getName())) {
                    business.setBusinessDocReason(childElement.getText());
                }
                if ("StaffInfo".equals(childElement.getName())) {
                    business.setStaffInfo(StaffInfo.getData(childElement));
                }
                if ("BussinessDocumentInfo".equals(childElement.getName()) || "BusinessDocumentInfo".equals(childElement.getName())) {
                    business.setBusinessDocumentInfo(BusinessDocumentInfo.getData(childElement));
                }
                if ("ReplacementInfoList".equals(childElement.getName())) {
                    business.setReplacementInfoList(ReplacementInfoList.getData(childElement));
                }
            }
        }
        return business;
    }


}
