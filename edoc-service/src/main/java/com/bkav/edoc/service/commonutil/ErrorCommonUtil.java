package com.bkav.edoc.service.commonutil;

import com.bkav.edoc.service.resource.StringPool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ErrorCommonUtil {
    public static String getInfoToLog(String msg, Class<?> class_) {
        StringBuilder msgBuilder = new StringBuilder();
        if (msg == null) {

            msg = StringPool.BLANK;

        }

        Calendar cal = Calendar.getInstance(_LOCALE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");

        msgBuilder.append(msg);

        StackTraceElement[] stackTraceElements = Thread.currentThread()
                .getStackTrace();

        for (StackTraceElement stackTraceElement : stackTraceElements) {

            String classNameInStack = stackTraceElement.getClassName();

            String inputClassName = class_.getName();

            if (classNameInStack.equals(inputClassName)) {

                msgBuilder.append("--");
                msgBuilder.append("Method: ");
                msgBuilder.append(stackTraceElement.getMethodName());
                msgBuilder.append("--");
                msgBuilder.append("Line Number: ");
                msgBuilder.append(stackTraceElement.getLineNumber());
                msgBuilder.append("--");
                msgBuilder.append(dateFormat.format(cal.getTime()));

            }
        }

        return msgBuilder.toString();
    }

    private static final Locale _LOCALE = new Locale("vi", "VN");
}
