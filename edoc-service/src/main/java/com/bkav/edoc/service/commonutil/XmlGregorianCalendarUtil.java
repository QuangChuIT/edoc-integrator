package com.bkav.edoc.service.commonutil;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class XmlGregorianCalendarUtil {

    public static String VN_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static String VN_DATE_FORMAT = "dd/MM/yyyy";

    static public XMLGregorianCalendar getInstance()
            throws DatatypeConfigurationException {

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        return DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);

    }

    static public Date convertToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            LOGGER.error(e);
            return null;
        }
        return date;
    }

    static public String convertToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String result;
        try {
            result = formatter.format(date);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    static public Date convertToDate(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return date;
    }

    static public Date getMinDefaultDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1900);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    static public Date convertToDate(String dateString, String format,
                                     Date defaultDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            date = defaultDate;
        }
        return date;
    }

    static public XMLGregorianCalendar setTime(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        XMLGregorianCalendar xmlGregorianCalendar = null;
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            return null;
        }

        xmlGregorianCalendar.setYear(cal.get(Calendar.YEAR));
        xmlGregorianCalendar.setMonth(cal.get(Calendar.MONTH));
        xmlGregorianCalendar.setDay(cal.get(Calendar.DATE));

        return xmlGregorianCalendar;
    }

    private static final Logger LOGGER = Logger.getLogger(XmlGregorianCalendarUtil.class);
}
