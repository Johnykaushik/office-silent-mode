package com.officeMode;

import java.util.List;

public class MonthData {
    private String monthName;
    private Integer days;

    public MonthData(String monthName, Integer days) {
        this.monthName = monthName;
        this.days = days;
    }

    public String getMonthName() {
        return monthName;
    }

    public Integer getDays() {
        return days;
    }
}

