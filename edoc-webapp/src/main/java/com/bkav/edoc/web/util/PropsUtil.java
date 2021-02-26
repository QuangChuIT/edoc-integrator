package com.bkav.edoc.web.util;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {
    private static final Logger LOGGER = Logger.getLogger(com.bkav.edoc.service.util.PropsUtil.class);
    private static Properties prop = new Properties();

    public static Properties readPropertyFile() throws Exception {
        if (prop.isEmpty()) {
            InputStream input = com.bkav.edoc.service.util.PropsUtil.class.getClassLoader().getResourceAsStream("application.properties");
            try {
                prop.load(input);
            } catch (IOException ex) {
                LOGGER.error(ex);
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }
        return prop;
    }

    public static String get(String key) {
        if (prop.isEmpty()) {
            try {
                prop = readPropertyFile();
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
        return prop.getProperty(key);
    }

}
