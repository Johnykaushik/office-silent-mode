package com.officeMode;

import android.content.Context;
import android.util.Log;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

public class Utils {

    public String[] monthFullName(){
        String[] month = new String[] {"January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December" };
        return month;
    }

    public String[] montName(){
        String[] month = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug","Sep", "Oct", "Nov", "Dec" };
        return month;
    }

    public HashMap<String, HashSet<Integer>> getMonthDateFromStore(ScheduleStore store){
        String storeDates =  store.getMonthDate();

        Log.d("TAG", "getMonthDateFromStore: " + storeDates);
        HashMap<String, HashSet<Integer>>  monthDays = new HashMap<>();
        if(storeDates.length() != 0){
            String[] months = storeDates.split(",");

            if(months.length > 0){
                for(String month : months){
                    String [] monthWDate = month.split("=");
                    if(monthWDate.length > 0){
                        String monthName = monthWDate[0];
                        HashSet<Integer> dayChunk = new HashSet<>();
                        String[] dateList = monthWDate[1].split("_");
                        if(dateList.length > 0){
                            for(String date  : dateList){
                                dayChunk.add(Integer.parseInt(date));
                            }
                        }
                        monthDays.put(monthName,dayChunk);
                    }
                }
            }
        }
        return monthDays;
    }
    public int getDateTimestamp(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        cal.set(Calendar.HOUR,10);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return Math.round(cal.getTimeInMillis() / 1000);
    }

    public int getDayFromUnix(long unixTimestamp) {
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");
        return Integer.parseInt(zonedDateTime.format(dayFormatter));
    }

    public String unixToDate(long unixTimestamp, String format) {
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern(format); // "d-MMM-uuuu" //  EEEE
        return zonedDateTime.format(monthFormatter);
    }

    public HashMap<String,Integer> getParsedDates(ScheduleStore store){
        String storeDates = store.getMonthDate();
        HashMap<String,Integer> list = new HashMap<>();
        if(storeDates != null){
            if(!storeDates.isEmpty()){
                String[] dateList = storeDates.split("-");

                for(String date : dateList){
                    list.put(date, Integer.parseInt(date));
                }
            }
        }
        return  list;
    }
}
