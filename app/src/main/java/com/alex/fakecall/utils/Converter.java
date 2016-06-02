package com.alex.fakecall.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Converter {
    public static Calendar string2Calendar(String dateStr, String pattern) {
        Calendar calendar = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            Date date = simpleDateFormat.parse(dateStr);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static String calendar2String(Calendar calendar, String pattern) {
        String rs = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            rs = simpleDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static String millis2String(long millis, String pattern){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar2String(calendar, pattern);
    }

}
