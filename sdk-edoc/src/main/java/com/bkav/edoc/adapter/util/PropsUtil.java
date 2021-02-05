package com.bkav.edoc.adapter.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {
    private static Properties prop = new Properties();

    public static Properties readPropertyFile() {
        if (prop.isEmpty()) {
            try (InputStream input = PropsUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
                prop.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return prop;
    }

    public static String get(String key) {
        if (prop.isEmpty()) {
            try {
                prop = readPropertyFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return prop.getProperty(key);
    }

}
