package com.bkav.edoc.sdk;

import com.bkav.edoc.sdk.entity.*;
import org.json.JSONObject;

import java.util.Scanner;

public class ServiceTest {
    public static void main(String[] args) {
        EdocProperties edocProperties = new EdocProperties();
        edocProperties.setEndpoint("https://ltvb-2.tayninh.gov.vn/EdocService");
        edocProperties.setOrganId("000.00.01.A53");
        edocProperties.setStoreFilePath("/home/quangcv/edoc");
        edocProperties.setToken("7sZeravJfmziFC/FgT5mYdKKXDk=");
        EdocService edocService = new EdocService(edocProperties);

        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        showMenu();
        while (true) {
            int choose = scanner.nextInt();
            switch (choose) {
                case 1:
                    GetOrganizationResp getOrganizationResp = edocService.getOrganization();
                    System.out.println(getOrganizationResp.getOrganizations().size());
                    break;
                case 2:
                    // Lay danh sach van ban den hoac danh sach trang thai
                    JSONObject getPending = new JSONObject();
                    getPending.put("type", DocumentType.STATUS);
                    GetPendingDocIDsResp getPendingDocIDsResp = edocService.getPendingDocIds(getPending.toString());
                    System.out.println(getPendingDocIDsResp.getDocIDs().size());
                    break;
                case 3:
                    // Gui goi tin van ban
                    //String edXMLFileLocation = "D:\\IdeaProjects\\BkavEdocSdk\\src\\main\\resources\\edoc_new.edxml";
                    String edXMLFileLocation = "D:\\IdeaProjects\\BkavEdocSdk\\src\\main\\resources\\status_inbox_ 01.edxml";
                    JSONObject sendDocReq = new JSONObject();
                    sendDocReq.put("type", DocumentType.STATUS);
                    SendDocResp sendDocResp = edocService.sendDocument(sendDocReq.toString(), edXMLFileLocation);
                    System.out.println(sendDocResp.getDocId());
                    System.out.println(sendDocResp.getCode());
                    System.out.println(sendDocResp.getStatus());
                    break;
                case 4:
                    // Lay 1 van ban hoac trang thai
                    JSONObject object = new JSONObject();
                    object.put("docId", "16621371");
                    object.put("type", DocumentType.EDOC);
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
                case 0:
                    System.out.println("Exited !!!!!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid! Please choose action in below menu");
                    break;
            }
            if (exit) {
                break;
            }
            showMenu();
        }

    }

    public static void showMenu() {
        System.out.println("1. GetOrganization\n2. GetPendingDocIds\n3. SendDocument\n4. GetDocument\n5. ConfirmReceived\n0. Exit");
        System.out.print("Choose: ");
    }
}
