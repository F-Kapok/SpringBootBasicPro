package com.fans.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @ClassName DateUtils
 * @Description: TODO 时间格式化工具
 * @Author fan
 * @Date 2018-09-10 13:45
 * @Version 1.0
 **/
public class DateUtils {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";

    private static final String HH_MM_SS = "HH:mm:ss";

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * @Description: TODO 字符串转时间
     * @Param: [dateTimeStr, pattern]
     * @return: java.util.Date
     * @Author: fan
     * @Date: 2018/12/18 9:27
     **/
    public static Date str2Date(String dateTimeStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * @Description: TODO 时间转字符串
     * @Param: [date, pattern]
     * @return: java.lang.String
     * @Author: fan
     * @Date: 2018/12/18 9:28
     **/
    public static String date2Str(Date date, String pattern) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }

    public static Date getYYYYMMdd(String dateTimeStr) {
        return str2Date(dateTimeStr, YYYY_MM_DD);
    }

    public static Date getHHMMss(String dateTimeStr) {
        return str2Date(dateTimeStr, HH_MM_SS);
    }

    public static Date getYYYYMMddHHMMss(String dateTimeStr) {
        return str2Date(dateTimeStr, YYYY_MM_DD_HH_MM_SS);
    }

    public static String getYYYYMMdd(Date date) {
        return date2Str(date, YYYY_MM_DD);
    }

    public static String getHHMMss(Date date) {
        return date2Str(date, HH_MM_SS);
    }

    public static String getYYYYMMddHHMMss(Date date) {
        return date2Str(date, YYYY_MM_DD_HH_MM_SS);
    }

    public static String getIncrDay(Integer day) {
        return new DateTime().plusDays(day).toString(YYYY_MM_DD);
    }
}
