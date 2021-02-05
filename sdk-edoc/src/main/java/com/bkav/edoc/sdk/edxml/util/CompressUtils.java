package com.bkav.edoc.sdk.edxml.util;

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class CompressUtils {

    public CompressUtils() {
    }

    public static File unzip(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration enumeration = zipFile.entries();
        File result;
        if (!enumeration.hasMoreElements()) {
            return null;
        } else {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
            File fileEntry = new File(file.getParentFile(), zipEntry.getName());
            try (FileOutputStream outputStream = new FileOutputStream(fileEntry); InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                ByteStreams.copy(inputStream, outputStream);
                result = fileEntry;
            }
            return result;
        }
    }

    public static File zip(InputStream inputStream, String zipFileName, String fileName) throws IOException {
        String str = UUID.randomUUID().toString();
        File fileEntry = new File(fileName, str + ".zip");
        ZipOutputStream localZipOutputStream = new ZipOutputStream(new FileOutputStream(fileEntry));
        ZipEntry localZipEntry = new ZipEntry(str + "." + zipFileName);
        localZipOutputStream.putNextEntry(localZipEntry);
        ByteStreams.copy(inputStream, localZipOutputStream);
        localZipOutputStream.close();
        return fileEntry;
    }
}
