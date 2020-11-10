package io.ztc.tools;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DATE {

    public static final int YMD = 0;
    public static final int YMD_HMS = 1;
    public static final int YMD_HM0 = 2;
    public static final int HMS = 3;

    /**
     * 标准时间字符串转换成时间类型
     * @param str 标准时间字符串
     * @return date 时间
     */
    public static Date getDateByString(String str) {
        Date date;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 日期转换成字符串
     * @param date 时间
     * @return str 事件字符串
     */
    public static String getStringByDate(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 日期转换成字符串
     * @return str 事件字符串
     */
    public static String getNowTime(int Type) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        switch (Type){
            case YMD: pattern = "yyyy-MM-dd";
            case YMD_HMS: pattern = "yyyy-MM-dd HH:mm:ss";
            case YMD_HM0:pattern = "yyyy-MM-dd HH:mm:00";
            case HMS :pattern ="HH:mm:00";
        }
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 日期转换成字符串
     * @return str 事件字符串
     */
    public static String getNowTime(String pattern) {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 时间戳转换成字符串
     * @return str 事件字符串
     */
    public static String getTime(Long time,String pattern) {
        if (time!=null){
            Date date = new Date(time);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        }
        return "";
    }

    /**
     * 时间转换成12小时制字符串
     * @param date 时间
     * @return str 12小时制字符串
     */
    public static String get12HCSByDate(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("a hh:mm");
        return format.format(date);
    }

    /**
     * 时间转换成12小时制字符串
     * @param date 时间
     * @return str 12小时制中文时间字符串
     */
    public static String getChinese12HCSByDate(Date date) {

        String AM_PM;
        String str;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat time_12 = new SimpleDateFormat("hh:mm");
        String time = time_12.format(date);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        String hour_str = hour.format(date);

        if (Integer.parseInt(hour_str)>0 && Integer.parseInt(hour_str)<4){
            AM_PM = "凌晨";
            str = AM_PM+" "+time;
        }else if (Integer.parseInt(hour_str)==0){
            AM_PM = "凌晨";
            str = AM_PM+" 00:00";
        }else if (Integer.parseInt(hour_str)==12){
            AM_PM = "中午";
            str = AM_PM+time;
        }else if (Integer.parseInt(hour_str)>19 && Integer.parseInt(hour_str)<23){
            AM_PM = "晚上";
            str = AM_PM+time;
        }else {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("a hh:mm");
            str = format.format(date);
        }
        return str;
    }



    /**
     * 提取日期
     * @param str 标准时间字符串
     * @return 时间日期字符串
     */
    public static String extractDate(String str){
        String new_str;
        try {
            Date date = getDateByString(str);
            new_str = getStringByDate(date);
        }catch (Exception e){
            return str;
        }
        return new_str;
    }

    /**
     * 提取的12小时制时间字符串
     * @param str 标准24小时制时间字符串
     * @return 12小时制时间字符串
     */
    public static String get12HCSByString(String str){
        Date date = getDateByString(str);
        return get12HCSByDate(date);
    }

    /**
     * 完整24小时字符串转中文区段12小时
     * @param str 标准24小时制时间字符串
     * @return 中文12小时字符串
     */
    public static String getChinese12HCSByString(String str){
        Date date = getDateByString(str);
        return getChinese12HCSByDate(date);
    }

    /**
     * 获取当前时间n个月时间字符串
     * @param months 指定月份
     * @return 指定月份的当前时间的字符串
     */

    public static String getMonthsAgo(int months){
        Date dNow = new Date(); //当前时间
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -months); //设置为前n月
        Date dBefore = calendar.getTime(); //得到前n月的时间
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        return sdf.format(dBefore);
    }

    /**
     * 字符串转可获取年月日的时间
     * @param mDate 标准时间字符串
     * @param mFormat 时间格式
     * @return 日历时间
     */

    public static Calendar getCalendarByString(String mDate,String mFormat){
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat(mFormat);
            Date date = format.parse(mDate);
            Calendar calendar = Calendar.getInstance();
            assert date != null;
            calendar.setTime(date);
            return calendar;
        } catch ( ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定时间的上个月 时间字符串
     * @param year 指定年
     * @param mon 指定月份
     * @return 时间字符串(X年X月)
     */
    public static String getLMon(int year ,int mon){
        if (mon == 1){
            year = year - 1;
            mon = 12;
        }else {
            mon = mon - 1;
        }
        return year+"年"+mon+"月";
    }

    /**
     * 获取指定时间的上上个月 时间字符串
     * @param year 指定年
     * @param mon 指定月份
     * @return 时间字符串(X年X月)
     */
    public static String getLLMon(int year ,int mon){
        if (mon == 1){
            year = year - 1;
            mon = 11;
        }else if(mon == 2){
            year = year - 1;
            mon = 12;
        }else {
            mon = mon - 2;
        }
        return year+"年"+mon+"月";
    }

    /**
     * 时间戳转日期格式
     * @param time
     * @return
     */
    public static String getDateByStamp(Long time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(time));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时差转年月日时分秒
     * @param time1
     * @param time2
     * @return
     */
    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;

        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天"+hour + "小时"+min + "分钟" + sec + "秒";
        if (hour != 0) return hour + "小时"+ min + "分钟" + sec + "秒";
        if (min != 0) return min + "分钟" + sec + "秒";
        if (sec != 0) return sec + "秒" ;
        return "0秒";
    }
}
