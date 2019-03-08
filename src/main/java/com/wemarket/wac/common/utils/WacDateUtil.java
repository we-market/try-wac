package com.wemarket.wac.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class WacDateUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_QUERY = "yyyyMMddHHmmss";

    public static String nowTimeString() {
        return nowTimeString(DATE_PATTERN);
    }

    public static String nowTimeString(String format) {
        return DateFormatUtils.format(new Date(), format);
    }

    public static Date parseDateString(String dateString) throws ParseException {
        return DateUtils.parseDate(dateString, DATE_PATTERN);
    }

    public static Date parseDateStringForQuery(String dateString) throws ParseException {
        return DateUtils.parseDate(dateString, DATE_PATTERN_QUERY);
    }

    public static String translateDateStringForQuery(String dateString) throws Exception {
        Date date = DateUtils.parseDate(dateString, DATE_PATTERN);
        return DateFormatUtils.format(date, DATE_PATTERN_QUERY);
    }
}
