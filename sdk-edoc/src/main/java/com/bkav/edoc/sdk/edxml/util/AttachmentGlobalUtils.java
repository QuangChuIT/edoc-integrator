package com.bkav.edoc.sdk.edxml.util;

import com.bkav.edoc.sdk.resource.EdXMLConfigKey;
import com.bkav.edoc.sdk.util.PropsUtils;
import com.bkav.edoc.sdk.util.StringPool;
import com.bkav.edoc.sdk.util.Validator;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;

import java.io.*;

public class AttachmentGlobalUtils {

    private final String SEPARATOR = File.separator;

    String DEFAULT_ROOT_PATH = SEPARATOR;

    public String getAttachmentPath() {
        String folderName = "attachment";
        return getSaveFilePath(folderName);
    }

    public long saveToFile(String targetPath, InputStream is)
            throws IOException {

        if (Validator.isNull(is)) {
            return -1;
        }

        File attachmentFile = new File(targetPath);

        File parentFile = attachmentFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                return -1;
            }
        }

        if (!attachmentFile.exists()) {

            if (!attachmentFile.createNewFile()) {
                return -1;
            }

        }
        OutputStream os = new FileOutputStream(attachmentFile);

        // Save an attachment into Attachment folder
        long bytesReadied = 0;
        int read = 0;

        byte[] bytes = new byte[3072];

        while ((read = is.read(bytes)) != -1) {

            os.write(bytes, 0, read);
            bytesReadied += read;

        }
        is.close();
        os.close();

        return bytesReadied;
    }

    public void saveFile(String targetPath, InputStream inputStream) throws IOException {
        if (Validator.isNull(inputStream)) {
            return;
        }

        File attachmentFile = new File(targetPath);

        File parentFile = attachmentFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                return;
            }
        }

        if (!attachmentFile.exists()) {

            if (!attachmentFile.createNewFile()) {
                return;
            }

        }
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        Files.write(buffer, attachmentFile);
    }


    public String getSaveFilePath(String folderName) {

        String rootPath = StringPool.BLANK;

        String setupPath = PropsUtils.get(EdXMLConfigKey.ATTACHMENT_FOLDER_ROOT);

        if (Validator.isNull(setupPath)) {

            rootPath = DEFAULT_ROOT_PATH;
        } else {

            rootPath = setupPath;// result.getValue();

        }
        if (!rootPath.endsWith(SEPARATOR)) {

            rootPath += SEPARATOR;
        }

        File parent = new File(rootPath);// .getParentFile();

        File targetFolder = new File(parent.getAbsolutePath() + SEPARATOR
                + folderName + SEPARATOR);

        if (!targetFolder.exists()) {
            if (!targetFolder.mkdirs()) {
                System.out.println("Can't create dir with" + targetFolder.getAbsolutePath());
            }
        }

        return targetFolder.getAbsolutePath();

    }

    public byte[] parseBase64ISToBytes(InputStream base64IS) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int readed = 0;
            while ((readed = base64IS.read(buffer, 0, buffer.length)) != -1) {

                byteArrayOutputStream.write(buffer, 0, readed);
            }
            byteArrayOutputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            base64IS.close();
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        if (!Base64.isBase64(bytes[0])) {
            bytes = Base64.encodeBase64(bytes);
        }
        return bytes;
    }

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
