package com.bdlog.demo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 判断输入的参数是否是一个有效的时间格式数据
     *
     * @param input
     * @return
     */
    public static boolean isValidateRunningDate(String input) {
        Matcher matcher = null;
        String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        if (input != null && !input.isEmpty()) {
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(input);
        }
        if (matcher != null)
            return matcher.matches();
        return false;
    }

    /**
     * 获取昨日的日期格式字符串数据
     *
     * @return
     */
    public static String getYesterday() {
        return getYesterday(DATE_FORMAT);
    }

    /**
     * 获取对应格式的时间字符串
     *
     * @param pattern
     * @return
     */
    public static String getYesterday(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 将给定的日期、此日期的模式 转换成 "YYYY-MM-dd"模式的日期
     * 可参看下面的main中的测试
     * @param date 待转换的日期
     * @param pattern 待转换日期的格式
     * @return
     */
    public static String formatDate(String date, String pattern) {
        Date dateParsed;
        String output = null;
        System.out.println("date:" + date);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            dateParsed = sdf.parse(date);
            output= new SimpleDateFormat(DATE_FORMAT).format(dateParsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("date:" + date + " out:" + output);
        return output;
    }

    public static void main(String[] args) {
        formatDate("2017/11/13", "yyyy/MM/dd");
        formatDate("2017.11.13", "yyyy.MM.dd");
        formatDate("2017-11-13", "yyyy-MM-dd");
    }
}
