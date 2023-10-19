package com.officeMode;

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
}
