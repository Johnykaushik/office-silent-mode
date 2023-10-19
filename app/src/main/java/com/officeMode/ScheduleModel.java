package com.officeMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ScheduleModel {
    private String isEnable;
    private String weekdays;
    private String startTime;
    private String endTime;
    private HashMap<String, HashSet<Integer>> monthDays;

    public HashMap<String, HashSet<Integer>> getMonthDays() {
        return monthDays;
    }

    public void setMonthDays(HashMap<String, HashSet<Integer>> monthDays) {
        this.monthDays = monthDays;
    }


    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(String weekdays) {
        this.weekdays = weekdays;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}