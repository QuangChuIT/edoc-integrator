package com.bkav.edoc.sdk.edxml.util;

import com.bkav.edoc.sdk.util.GetterUtil;
import org.jdom2.Element;

import java.util.List;

public class EdxmlUtils {
    public static String getContentId(String value) {
        String result = "";
        if (value.startsWith("cid:")) {
            result = value.substring(4);
        } else {
            result = value;
        }
        return result;
    }

    public static int getInt(Element element, String eName, int defaultValue) {
        try {
            String str = getString(element, eName);
            return GetterUtil.getInteger(str, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(Element element, String eName, boolean defaultValue) {
        try {
            String str = getString(element, eName);
            return GetterUtil.getBoolean(str, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getString(Element element, String eName) {
        if (element != null) {
            List<Element> childrenElements = element.getChildren();
            if (childrenElements != null && childrenElements.size() > 0) {
                for (Element children : childrenElements) {
                    if (eName.equals(children.getName())) {
                        return children.getText();
                    }
                }
            }
        }
        return null;
    }
}
