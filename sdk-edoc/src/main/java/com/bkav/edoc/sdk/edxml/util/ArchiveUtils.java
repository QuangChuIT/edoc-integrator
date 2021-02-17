package com.bkav.edoc.sdk.edxml.util;

import com.bkav.edoc.sdk.util.FileUtils;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ArchiveUtils {

    public File decompressFile(String filePath) {

        String specPath = relativePath
                + (relativePath.endsWith(File.separator) ? "" : File.separator)
                + filePath;

        String resultFilePath = "";

        File oldFile = new File(specPath);

        // Neu file ton tai nghia la chua nen => tra ve luon
        if (oldFile.exists()) {
            return oldFile;
        } else {
            // File khong ton tai => file da duoc nen

            // Lay file nen
            File zippedFile = new File(specPath + FILE_EXTENSION);

            if (zippedFile.exists()) {

                File oldFolder = new File(relativePath);

                File fileInTemp = new File(oldFolder.getAbsolutePath() + "_tmp"
                        + File.separator + filePath);

                // Neu da ton tai file giai nen trong thu muc temp => tra ve
                // luon
                if (fileInTemp.exists()) {

                    return fileInTemp;
                } else {
                    //Giai nen ra thu muc temp
                    File tempFolder = fileInTemp.getParentFile();

                    if (!tempFolder.exists()) {
                        if (!tempFolder.mkdirs()) {
                            System.out.println("Can't make dir by: " + tempFolder.getPath());
                        }
                    }

                    try {
                        resultFilePath = decompressFile(zippedFile.getAbsolutePath(),
                                tempFolder.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        resultFilePath = "";
                    }
                }

            } else {
                resultFilePath = "";
            }
        }

        return new File(resultFilePath);
    }

    /**
     * Decompresses a zlib compressed file.
     */
    private String decompressFile(String targetFilePath, String outputFolderPath)
            throws IOException {

        String resultFilePath = "";

        File zippedFile = new File(targetFilePath);

        File folder = new File(outputFolderPath);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                return "";
            }
        }

        if (zippedFile.exists()) {

            ZipInputStream zis = null;

            try {
                zis = new ZipInputStream(new FileInputStream(zippedFile));
                ZipEntry ze = zis.getNextEntry();
                while (ze != null) {

                    String fileName = ze.getName();

                    File deCompressFile = new File(outputFolderPath
                            + File.separator + fileName);

                    FileOutputStream fos = new FileOutputStream(deCompressFile);

                    shovelInToOut(zis, fos);

                    fos.close();

                    ze = zis.getNextEntry();

                    resultFilePath = deCompressFile.getAbsolutePath();
                }

            } catch (Exception e) {
                e.printStackTrace();
                resultFilePath = "";

            } finally {
                if (zis != null) {
                    zis.closeEntry();
                    zis.close();
                }
            }
        }
        return resultFilePath;

    }

    private boolean compressFile(String targetFilePath) {

        boolean result = false;
        File targetFile = new File(targetFilePath);

        if (targetFile.exists()) {

            String parentPart = targetFile.getParent();

            String targetFileName = targetFile.getName();

            File outputFile = new File(parentPart + File.separator
                    + targetFileName + FILE_EXTENSION);

            InputStream in = null;

            ZipOutputStream out = null;
            try {

                ZipEntry ze = new ZipEntry(targetFileName);

                in = new FileInputStream(targetFile);

                out = new ZipOutputStream(new FileOutputStream(outputFile));

                out.putNextEntry(ze);

                shovelInToOut(in, out);

                result = true;

            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            } finally {

                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.closeEntry();
                        out.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = false;
                }

            }

        }

        return result;

    }

    private void deleteFileAfterCompress(String targetFilePath) throws Exception {
        File targetFile = new File(targetFilePath);
        if (targetFile.exists()) {
            FileUtils.delete(targetFile, false);
        }

    }

    public static File unzip(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        File result;
        if (!enumeration.hasMoreElements()) {
            return null;
        } else {
            ZipEntry zipEntry = enumeration.nextElement();
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

    private void rollBackCompress(String targetFilePath) throws Exception {
        File targetFile = new File(targetFilePath);
        if (targetFile.exists()) {

            String parentPart = targetFile.getParent();

            String targetFileName = targetFile.getName();

            File rollBackFile = new File(parentPart + File.separator
                    + targetFileName + FILE_EXTENSION);

            if (rollBackFile.exists()) {
                deleteFileAfterCompress(targetFilePath);
            }
        }
    }

    /**
     * Shovels all data from an input stream to an output stream.
     */
    private static void shovelInToOut(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[3072];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    public ArchiveUtils() {
        try {
            relativePath = new AttachmentGlobalUtils().getAttachmentPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String relativePath = "";
    private static final String FILE_EXTENSION = ".edzip";
}
