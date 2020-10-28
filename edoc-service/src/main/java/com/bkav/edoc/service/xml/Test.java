package com.bkav.edoc.service.xml;

import com.bkav.edoc.service.xml.base.header.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        ResponseFor responseFor = new ResponseFor("000.00.00.H41", "267/VPCP-TTĐT", new Date(), "000.00.04.G14,2012/08/06,267/VPCP-TTĐT");
        ResponseFor responseFor2 = new ResponseFor("000.00.00.H14", "267/VPCP-TTĐT", new Date(), "000.00.04.G14,2012/08/06,267/VPCP-TTĐT");

        List<ResponseFor> responseForList = new ArrayList<>();
        responseForList.add(responseFor);
        responseForList.add(responseFor2);

        String responseForJson = gson.toJson(responseForList);
        System.out.println(responseForJson);
        System.out.println("--********************************-------------");
        BusinessDocumentInfo businessDocumentInfo = new BusinessDocumentInfo();
        businessDocumentInfo.setDocumentInfo("1");
        businessDocumentInfo.setDocumentReceiver("1");

        // khoi tao danh sach cac don vi nhan bi thay doi khi cap nhat van ban
        ReceiverList receiverList = new ReceiverList();
        Receiver receiver = new Receiver("000.00.00.H41", "0");
        Receiver receiver2 = new Receiver("000.00.00.H14", "1");
        receiverList.addReceiver(receiver);
        receiverList.addReceiver(receiver2);
        businessDocumentInfo.setReceiverList(receiverList);


        String businessDocumentInfoJson = gson.toJson(businessDocumentInfo);
        System.out.println(businessDocumentInfoJson);
        System.out.println("--********************************-------------");
        // khoi tao thong tin danh sach van ban bi thay the
        ReplacementInfoList replacementInfoList = new ReplacementInfoList();
        ReplacementInfo replacementInfo = new ReplacementInfo();
        OrganIdList organIdList = new OrganIdList();
        organIdList.addOrganId("000.00.00.H41");
        organIdList.addOrganId("000.00.00.H14");
        replacementInfo.setOrganIdList(organIdList);
        replacementInfo.setDocumentId("000.00.00.G22,2014/02/30,7806/VPCP-TTĐT");
        replacementInfoList.addReplacementInfo(replacementInfo);
        replacementInfoList.addReplacementInfo(replacementInfo);
        String replacementInfoJson = gson.toJson(replacementInfoList);
        System.out.println(replacementInfoJson);
    }
}
