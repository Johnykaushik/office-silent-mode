package com.officeMode;

import java.util.List;

public class MonthData {
    private String monthName;
    private String days;
    private Long unixDate;



    public MonthData(String monthName, String days, Long unixDate) {
        this.monthName = monthName;
        this.days = days;
        this.unixDate = unixDate;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getDays() {
        return days;
    }
    public Long getUnixDate() {
        return unixDate;
    }
}

