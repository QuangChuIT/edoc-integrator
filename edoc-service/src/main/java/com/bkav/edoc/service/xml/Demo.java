package com.bkav.edoc.service.xml;

import com.google.common.io.Files;
import org.json.JSONObject;

public class Demo {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        System.out.println(jsonObject.toString());

        String path = "a.txt";
        String ext = Files.getFileExtension(path);
        System.out.println(ext);
    }
}
