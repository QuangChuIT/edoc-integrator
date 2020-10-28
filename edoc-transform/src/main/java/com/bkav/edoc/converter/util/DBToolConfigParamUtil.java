package com.bkav.edoc.converter.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * @author QuangCV
 */
public class DBToolConfigParamUtil {

    public static String getConfigParamValue(String configParamName) throws IOException {

        String configParamValue = "";

        FileInputStream fileInputStream = null;

        try {

            fileInputStream = new FileInputStream(DBToolConstants.TOOL_CONFIGURATION_FILE_PATH);

            Properties dbToolProperties = new Properties();

            dbToolProperties.load(fileInputStream);

            configParamValue = dbToolProperties.getProperty(configParamName, _BLANK);

        } catch (IOException e) {
            _log.error(e);

        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }

        }
        return configParamValue;
    }

    private static final String _BLANK = "";

    private static final Logger _log = Logger.getLogger(DBToolConfigParamUtil.class.getName());
}

