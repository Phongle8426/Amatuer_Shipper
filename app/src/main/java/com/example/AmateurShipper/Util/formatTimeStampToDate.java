package com.example.AmateurShipper.Util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class formatTimeStampToDate {
    //long time_stamp;

    public formatTimeStampToDate() {
    }

    public String convertTimeStamp(long time_stamp){
        Date currentDate = new Date (time_stamp*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, YYYY-MM-dd HH:mm:ss");
        String date = dateFormat.format(currentDate);
        Log.i(TAG, "convertTimeStamp: "+ date);
        return date;
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
}
