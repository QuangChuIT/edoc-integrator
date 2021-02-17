package com.bkav.edoc.sdk;

import com.bkav.edoc.sdk.entity.*;
import org.json.JSONObject;

public class ServiceTest {
    public static void main(String[] args) {
        EdocProperties edocProperties = new EdocProperties();
        edocProperties.setEndpoint("http://localhost/EdocService");
        edocProperties.setOrganId("000.00.20.H36");
        edocProperties.setStoreFilePath("D:/edxml");
        edocProperties.setToken("YF3p4HUAjug3eC558MZ1qrBcPQ4=");
        EdocService edocService = new EdocService(edocProperties);

        String pStart = "---------";
        ServiceTest.run = 3;
        switch (run) {
            case 1:
                //Lay danh sach don vi
                GetOrganizationResp getOrganizationResp = edocService.getOrganization();
                System.out.println(getOrganizationResp.getOrganizations().size());
                break;
            case 2:
                // Lay danh sach van ban den hoac danh sach trang thai
                JSONObject getPending = new JSONObject();
                getPending.put("messageType", DocumentType.STATUS);
                GetPendingDocIDsResp getPendingDocIDsResp = edocService.getPendingDocIds(getPending.toString());
                System.out.println(getPendingDocIDsResp.getDocIDs().size());
                break;
            case 3:
                // Gui goi tin van ban
                //String edXMLFileLocation = "D:\\IdeaProjects\\BkavEdocSdk\\src\\main\\resources\\edoc_new.edxml";
                String edXMLFileLocation = "D:\\IdeaProjects\\BkavEdocSdk\\src\\main\\resources\\status_inbox_ 01.edxml";
                JSONObject sendDocReq = new JSONObject();
                sendDocReq.put("messageType", DocumentType.STATUS);
                SendDocResp sendDocResp = edocService.sendDocument(sendDocReq.toString(), edXMLFileLocation);
                System.out.println(sendDocResp.getDocId());
                System.out.println(sendDocResp.getCode());
                System.out.println(sendDocResp.getStatus());
                break;
            case 4:
                // Lay 1 van ban hoac trang thai
                JSONObject object = new JSONObject();
                object.put("docId", "241075");
                object.put("messageType", DocumentType.EDOC);
                GetDocumentResp getDocumentResp = edocService.getDocument(object.toString());
                System.out.println(getDocumentResp.toString());
                break;
            case 5:
                // Cap nhat trang thai van ban nhan khi da nhan va xu ly thanh cong
                JSONObject request = new JSONObject();
                request.put("docId", "241070");
                ConfirmReceivedResp confirmReceivedResp = edocService.confirmReceived(request.toString());
                System.out.println(confirmReceivedResp.getCode());
                System.out.println(confirmReceivedResp.getStatus());
            default:
                break;
        }
    }

    public static int run = 1;

}
