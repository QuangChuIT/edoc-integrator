package com.bkav.edoc.sdk.edxml.util;

import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT_REVERSE = "dd/MM/yyyy";
    public static final String DATE_FORMAT_NEW = "yyyy-MM-dd";
    public static final String VN_DATETIME_FORMAT_NEW = "dd/MM/yyyy HH:mm:ss";
    public static final String VN_DATETIME_FORMAT_D = "dd-MM-yyyy HH:mm:ss";
    public static final String VN_DATE_FORMAT = "dd/MM/yyyy";
    public static final String VN_DATE_FORMAT_D = "dd_MM_yyyy";

    public DateUtils() {
    }

    public static Date parse(String dateStrValue, String format) {
        if (Strings.isNullOrEmpty(dateStrValue)) {
            return null;
        } else {
            if (Strings.isNullOrEmpty(format)) {
                format = DEFAULT_DATE_FORMAT;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                return simpleDateFormat.parse(dateStrValue);
            } catch (ParseException e) {
                System.out.println("Parser error with date format " + format + " value " + dateStrValue);
                return null;
            }
        }
    }

    public static Date parse(String dateStrValue) {
        return parse(dateStrValue, DEFAULT_DATE_FORMAT_REVERSE);
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        } else {
            if (Strings.isNullOrEmpty(format)) {
                format = DEFAULT_DATE_FORMAT;
            }

            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(format);
            return localSimpleDateFormat.format(date);
        }
    }

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_FORMAT);
    }
}
