package com.example.checknut.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-17 14:18
 */

public class DateUtils {
    public static boolean compareDateByDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 把给定日期的时间设置成00:00:00:000
     * @param date
     * @return
     */
    public static Date setHMSOfDayToZero(Date date){
        if (null != date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY,0);
            c.set(Calendar.MINUTE,0);
            c.set(Calendar.SECOND,0);
            c.set(Calendar.MILLISECOND,0000);
            date = c.getTime();
            return date;
        }else {
            return null;
        }
    }

    public static Date getEndOfDay(Date date){
        if (null != date){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);
            c.set(Calendar.MILLISECOND,999);
            date = c.getTime();
            return date;
        } else {
            return null;
        }
    }

    /**
     * 根据给定日期，获得给定日期前一天的23:59:59:999，相当于获得给定日期的开始一刻
     * @param date
     * @return
     */
    public static Date getEndOfLastDay(Date date){
        if (null != date){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE,-1);
            c.set(Calendar.HOUR_OF_DAY,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);
            c.set(Calendar.MILLISECOND,999);
            date = c.getTime();
            return date;
        } else {
            return null;
        }
    }

    /**
     * 根据给定日期，获得给定日期下一天的00:00:00:000,相当于是获得给定日期的最后一刻
     * @param date
     * @return
     */

    public static Date getStartOfNextDay(Date date){
        if (null != date){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE,1);
            c.set(Calendar.HOUR_OF_DAY,0);
            c.set(Calendar.MINUTE,0);
            c.set(Calendar.SECOND,0);
            c.set(Calendar.MILLISECOND,000);
            date = c.getTime();
            return date;
        } else {
            return null;
        }
    }

    /**
     * 返回上个月的最后一天 23:59:59:999
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date){
        if (null != date){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DATE,1);
            date = c.getTime();
            date = DateUtils.getEndOfLastDay(date);
            return date;
        }else {
            return null;
        }
    }

    /**
     * 返回下个月第一天 00:00:00:000
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date){
        if (null != date){
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH,1);
            c.set(Calendar.DATE,1);
            date = c.getTime();
            date = DateUtils.setHMSOfDayToZero(date);
            return date;
        }else {
            return null;
        }
    }

    public static String getHMS(Calendar c){
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int s = c.get(Calendar.SECOND);
        String hs = plusZero(h), ms= plusZero(m), ss= plusZero(s);
        return hs + ":" + ms +":" + ss;
    }

    public static String plusZero(int n){
        String s = "";
        if (n < 10) {
            s = "0" + n;
        } else {
            s = String.valueOf(n);
        }
        return s;
    }
}
