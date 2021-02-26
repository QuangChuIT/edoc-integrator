package com.bkav.edoc.converter.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {

    private static final Logger LOGGER = Logger.getLogger(PropsUtil.class);
    private static Properties prop = new Properties();

    public static Properties readPropertyFile() throws Exception {
        if (prop.isEmpty()) {
            InputStream input = PropsUtil.class.getClassLoader().getResourceAsStream("configuration.properties");
            try {
                prop.load(input);
            } catch (IOException ex) {
                LOGGER.error(ex);
                throw ex;
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
