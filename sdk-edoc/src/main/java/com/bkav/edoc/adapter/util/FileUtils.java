package com.bkav.edoc.adapter.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static int emptyFolder(File folder, boolean ignoreCannotDel) throws IOException {
        int counter = 0;
        if (folder.exists() && folder.isDirectory()) {
            File[] child = folder.listFiles();

            for (int i = 0; i < child.length; ++i) {
                File file = child[i];
                if (file.isDirectory()) {
                    counter += emptyFolder(file, ignoreCannotDel);
                }

                boolean result = file.delete();
                if (!result && !ignoreCannotDel) {
                    throw new IOException("Cannot delete " + file.getAbsolutePath());
                }

                ++counter;
            }
        }

        return counter;
    }

    public static int remove(File file, boolean ignoreCannotDelete) throws Exception {
        int counter = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                counter += emptyFolder(file, ignoreCannotDelete);
            }

            boolean result = file.delete();
            if (!result && !ignoreCannotDelete) {
                throw new Exception("Cannot delete " + file.getAbsolutePath());
            }

            ++counter;
        }

        return counter;
    }

    public static void removeIfExist(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            remove(file, true);
        }

    }

    public static boolean exist(String path) throws Exception {
        File file = new File(path);
        return file.exists();
    }

    public static void mkdirs(String path) throws Exception {
        File file = new File(path);
        if (!file.exists() && !file.mkdirs()) {
            throw new Exception("Cannot create directory " + path);
        }
    }

    public static int cp(String src, String dest) throws Exception {
        int counter = 0;
        File srcFolder = new File(src);
        if (!srcFolder.exists()) {
            throw new Exception(src + " does not exist");
        } else {
            File destFolder;
            if (srcFolder.isFile()) {
                destFolder = new File(dest);
                if (destFolder.isFile()) {
                    dest = destFolder.getParent();
                }

                if (destFolder.exists()) {
                    dest = dest + "/" + srcFolder.getName();
                }

                InputStream input = new FileInputStream(srcFolder);
                OutputStream output = new FileOutputStream(dest);
                byte[] buff = new byte[8192];
                boolean var8 = false;

                int len;
                while ((len = input.read(buff)) > 0) {
                    output.write(buff, 0, len);
                }

                input.close();
                output.close();
                ++counter;
            } else {
                destFolder = new File(dest);
                if (!destFolder.exists()) {
                    destFolder.mkdirs();
                }

                File[] child = srcFolder.listFiles();

                for (int i = 0; i < child.length; ++i) {
                    File file = child[i];
                    if (file.isFile()) {
                        counter += cp(file.getAbsolutePath(), destFolder.getAbsolutePath() + "/" + file.getName());
                    } else {
                        counter += cp(file.getAbsolutePath(), destFolder.getAbsolutePath() + "/" + file.getName());
                    }
                }
            }

            return counter;
        }
    }

    public static void writeToFile(String fname, String data, boolean append) throws IOException {
        writeToFile(fname, data.getBytes("UTF-8"), append);
    }

    public static void writeToFile(String fname, byte[] data, boolean append) throws IOException {
        FileOutputStream os = new FileOutputStream(fname, append);
        os.write(data);
        os.close();
    }

    public static <T extends FileFilter> List<String> findFiles(File dir, T filter) throws IOException {
        List<String> holder = new ArrayList<>();
        findFiles(holder, dir, filter);
        return holder;
    }

    public static <T extends FileFilter> void findFiles(List<String> holder, File file, T filter) throws IOException {
        if (file.isFile() && filter.accept(file)) {
            holder.add(file.getAbsolutePath());
        } else {
            if (file.isDirectory()) {
                File[] files;
                int size = (files = file.listFiles()).length;

                for (int i = 0; i < size; i++) {
                    File child = files[i];
                    findFiles(holder, child, filter);
                }
            }

        }
    }

}
