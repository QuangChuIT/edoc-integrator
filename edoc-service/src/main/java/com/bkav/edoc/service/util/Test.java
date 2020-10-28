package com.bkav.edoc.service.util;

import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.util.EdocAttachmentServiceUtil;
import com.bkav.edoc.service.kernel.util.Base64;
import com.bkav.edoc.service.kernel.util.MimeTypesUtil;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;

public class Test {

    public static String getMimeType(String fileName) throws Exception {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(fileName);
        return mimeType;
    }

    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    public static void main(String[] args) throws Exception {
        System.out.println(AttachmentGlobalUtil.getFileExtension("DS-nhan van ban.xlsx"));
        System.out.println(AttachmentGlobalUtil.getFileNameWithoutExtension("DS-nhan van ban.xlsx"));
        String raw = "quangcv";
        String encode = Base64.encode(raw.getBytes());
        System.out.println(encode);

        EdocAttachment edocAttachment = new EdocAttachment();
        edocAttachment.setCreateDate(new Date());
        edocAttachment.setFullPath("/home/edoc/attachment");
        edocAttachment.setName("File.pdf");
        edocAttachment.setOrganDomain("000.00.13.H53");
        edocAttachment.setToOrganDomain("000.00.01.H53");
        edocAttachment.setSize("1024");
        edocAttachment.setType("application/pdf");

        edocAttachment = EdocAttachmentServiceUtil.addAttachment(edocAttachment);

        System.out.println(edocAttachment);
    }
}
