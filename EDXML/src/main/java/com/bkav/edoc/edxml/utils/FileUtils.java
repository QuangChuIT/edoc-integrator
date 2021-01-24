package com.bkav.edoc.edxml.utils;

import java.io.File;

public class FileUtils {
    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public static String getFileNameWithoutExtension(String fullFilename) {
        final String fileName = new File(fullFilename).getName();
        final int dotIndex = fileName.lastIndexOf('.');
        final String fileNameWithoutExtension;
        if (dotIndex == -1) {
            fileNameWithoutExtension = fileName;
        } else {
            fileNameWithoutExtension = fileName.substring(0, dotIndex);
        }
        return fileNameWithoutExtension;
    }

    public static String getContentType(String format) {
        String contentType = "";
        if ("pdf".equals(format)) {
            contentType = "application/pdf";
        } else if ("doc".equals(format)) {
            contentType = "application/msword";
        } else if ("docx".equals(format)) {
            contentType = "application/docx";
        } else if ("xls".equals(format)) {
            contentType = "application/vnd.ms-excel";
        } else if ("xlsx".equals(format)) {
            contentType = "application/xlsx";
        } else if ("zip".equals(format)) {
            contentType = "application/zip";
        } else {
            contentType = "application/zip";
        }
        return contentType;
    }
}
