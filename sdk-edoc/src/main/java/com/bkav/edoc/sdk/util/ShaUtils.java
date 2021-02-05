package com.bkav.edoc.sdk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class ShaUtils {
    public static String generateSHA256(String filePath) {
        FileInputStream fileInputStream = null;
        try {
            //Use SHA-1 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            fileInputStream = new FileInputStream(filePath);
            //Create byte array to read data in chunks
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;

            //Read file data and update in message digest
            while ((bytesCount = fileInputStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
            //Get the hash's bytes
            //This bytes[] has bytes in decimal format;
            byte[] bytes = digest.digest();

            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            System.out.println("Hex format: " + sb.toString());
            StringBuilder hex = new StringBuilder();
            for (byte aByte : bytes) {
                String h = Integer.toHexString(0xff & aByte);
                if (h.length() == 1) {
                    hex.append("0");
                }
                hex.append(h);
            }
            //return complete hash
            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
