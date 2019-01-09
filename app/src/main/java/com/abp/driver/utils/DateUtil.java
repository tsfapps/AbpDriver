package com.abp.driver.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {


    public static String getCurrentDateTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_MM_dd_yyyy_HH_mm_ss);
        return sdf.format(new Date());
    }

    public static String getCurrentDateTimeddMMMYYYY() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_FROM_SERVER);
        return sdf.format(new Date());
    }

    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT_24HR);
        return sdf.format(new Date());
    }

    public static String timeDiff(String dateStart, String dateStop, String flag) {
        String dH = "";
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(Constant.DATE_FORMAT_MM_dd_yyyy_HH_mm_ss);

            Date d1 = format.parse(dateStart);
            Date d2 = format.parse(dateStop);
            long diff = d2.getTime() - d1.getTime();

            switch (flag){
                case Constant.HOUR_SUFFIX:
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    if (diffDays > 0) {
                        dH = String.valueOf(diffDays + " d, " + (diffHours) + " hr, " + (diffMinutes) + " min");
                    } else if (diffHours > 0) {
                        dH = String.valueOf((diffHours) + " hr, " + (diffMinutes) + " min");
                    } else {
                        dH = String.valueOf((diffMinutes) + " min");
                    }
                    break;
                case Constant.MINUTE_SUFFIX:
                    dH = String.valueOf(diff / (60 * 1000));
                    break;
            }

        } catch (Exception e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return dH;
    }

    public static String timeDiff(String dateStart, String dateStop, String flag, boolean show) {
        String dH = "";
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(Constant.DATE_FORMAT_MM_dd_yyyy_HH_mm_ss);

            Date d1 = format.parse(dateStart);
            Date d2 = format.parse(dateStop);
            long diff = d2.getTime() - d1.getTime();

            switch (flag){
                case Constant.HOUR_SUFFIX:
                    //long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    if (diffDays > 0) {
                        dH = String.valueOf(diffDays + " d, " + (diffHours) + " : " + (diffMinutes));
                    } else if (diffHours > 0) {
                        dH = String.valueOf((diffHours) + " : " + (diffMinutes));
                    } else {
                        dH = String.valueOf("0 : "+(diffMinutes));
                    }
                    break;
                case Constant.MINUTE_SUFFIX:
                    dH = String.valueOf(diff / (60 * 1000));
                    break;
            }

        } catch (Exception e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return dH;
    }


    public static String convertDD_MM_YYYY_Into_ServerFormat(String date) {

        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(Constant.DATE_FORMAT_DD_MMM_YYYY, Locale.getDefault());
            Date date1 = inputDateFormaty.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_TO_SERVER, Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return convertedDate;
    }

    public static String getFromDate(String dateRange) {
        String fromDate = null;
        try {
            String[] date = dateRange.split("/");
            if (date.length > 0) {
                fromDate = date[0];
            }
        } catch (Exception e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return fromDate;
    }

    public static String getToDate(String dateRange) {
        String toDate = null;
        try{
            String[] date = dateRange.split("/");
            if (date.length > 0) {
                toDate = date[1];
            }
        } catch (Exception e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return toDate;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SimpleDateFormat")
    public static String convertTimeFromServer(String time){
        Date d1 = new Date(time);
        SimpleDateFormat sdft = new SimpleDateFormat(Constant.TIME_FORMAT_24HR);
        return sdft.format(d1);
    }

    public static String convertDateAccordingToServer(String date){
        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(Constant.DATE_FORMAT_FROM_SERVER, Locale.getDefault());
            Date date1 = inputDateFormaty.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_TO_SERVER, Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return convertedDate;
    }

    public static String convertWidgetDate(String date){
        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(Constant.DATE_FORMAT_DD_MM_YYYY, Locale.getDefault());
            Date date1 = inputDateFormaty.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_DD_MMM_YYYY, Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return convertedDate;
    }


    public static String dateDiff(String fromDate, String toDate) {
        String dH = "";
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(Constant.DATE_FORMAT_DD_MMM_YYYY);

            Date d1 = format.parse(fromDate);
            Date d2 = format.parse(toDate);
            long diff = d2.getTime() - d1.getTime();
            long diffDays = (diff / (24 * 60 * 60 * 1000))+1;
                dH = String.valueOf(diffDays);
        } catch (Exception e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return dH;
    }

    public static String convertServerFormatIntoDD_MM_YYYY(String date) {

        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(Constant.DATE_FORMAT_TO_SERVER, Locale.getDefault());
            Date date1 = inputDateFormaty.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_DD_MMM_YYYY, Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return convertedDate;
    }

    public static String convertServerFormatIntoDD_MMM_YYYY_HH_MM_SS(String date) {
        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(Constant.DATE_FORMAT_TO_SERVER, Locale.getDefault());
            Date date1 = inputDateFormaty.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_DD_MMM_YYYY_SS, Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return convertedDate;
    }

    public static String convertStandardFormatIntoDD_MM_YYYY(String date) {

        String convertedDate = null;
        try {
            SimpleDateFormat inputDateFormaty = new SimpleDateFormat(Constant.STANDARD_FORMAT, Locale.getDefault());
            Date date1 = inputDateFormaty.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_DD_MMM_YYYY, Locale.getDefault());
            convertedDate = sdf.format(date1);
        } catch (ParseException e) {
            CustomLog.e("DateUtil",e.toString());
        }
        return convertedDate;
    }

    public static String getCurrentDateTimeddMMMYYYYMinus3() {
        @SuppressLint("SimpleDateFormat")
        Date dNow = new Date(System.currentTimeMillis()-3*60*1000);
        SimpleDateFormat ft = new SimpleDateFormat(Constant.DATE_FORMAT_FROM_SERVER);
        return ft.format(dNow);
    }

}
