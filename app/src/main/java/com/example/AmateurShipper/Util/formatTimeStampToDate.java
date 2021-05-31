package com.example.AmateurShipper.Util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class formatTimeStampToDate {
    //long time_stamp;

    public formatTimeStampToDate() {
    }

    public String convertTimeToId(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDD-HHmmssSSS");
        String date = dateFormat.format(currentDate);
        return date;
    }

    public String convertTimeStamp(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, YYYY-MM-dd HH:mm:ss");
        String date = dateFormat.format(currentDate);
        //Log.i(TAG, "convertTimeStamp: "+ date);
        return date;
    }
    public String convertTimeForBlock(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        String date = dateFormat.format(currentDate);
        //Log.i(TAG, "convertTimeStamp: "+ date);
        return date;
    }

    public String convertDayMonthYear(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YY-MM-dd");
        String date = dateFormat.format(currentDate);
        return date;
    }
    public String convertToDay(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        String date = dateFormat.format(currentDate);
        //Log.i(TAG, "convertTimeStamp: "+ date);
        return date;
    }

    public String convertToDayOfWeek(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE");
        String dateOfWeek = dateFormat.format(currentDate);
        return dateOfWeek;
    }

    public String convertDayMonth(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String dayInMonth = dateFormat.format(currentDate);
        return dayInMonth;
    }
    public String convertToMonth(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        String monthInYear = dateFormat.format(currentDate);
        return monthInYear;
    }

    public static int getCurrentWeek(long time_stamp) {
        Date currentDate = new Date (time_stamp*1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int week = cal.get(cal.WEEK_OF_YEAR)-1;
        return week;
    }

    public static int getCurrentMonth(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(cal.MONTH)+1;
        return month;
    }
    public static int getCurrentYear(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int month = cal.get(cal.YEAR);
        return month;
    }

}
