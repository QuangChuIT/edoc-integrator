package com.bkav.edoc.service.util;

import java.io.*;

public class FileUtils {
    public static void write(String filePath, InputStream inputStream) throws IOException {
        OutputStream os = new FileOutputStream(filePath);

        byte[] buffer = new byte[1024];
        int bytesRead;
        //read from is to buffer
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        //flush OutputStream to write any buffered data to file
        os.flush();
        os.close();
    }
}
