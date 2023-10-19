package com.officeMode;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ScheduleStore {
    private static final String PREFERENCES_NAME = "schedulePref";
    private static final String KEY_ENABLE = "isEnable";
    private static final String KEY_WEEKDAYS = "weekdays";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";

    private static final String KEY_MONTH_DATE = "monthDate";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ScheduleStore(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveData(boolean isEnable, String weekdays, String startTime, String endTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ENABLE, isEnable);
        editor.putString(KEY_WEEKDAYS, weekdays);
        editor.putString(KEY_START_TIME, startTime);
        editor.putString(KEY_END_TIME, endTime);
        editor.apply();
    }

    public boolean getIsEnable() {
        Log.d("debug", "getIsEnable: ");
        return sharedPreferences.getBoolean(KEY_ENABLE,false);
    }

    public String getWeekdays() {
        return sharedPreferences.getString(KEY_WEEKDAYS, "");
    }

    public String getStartTime() {
        return sharedPreferences.getString(KEY_START_TIME, "");
    }

    public String getEndTime() {
        System.out.println("getting alll " + sharedPreferences.getAll());
        return sharedPreferences.getString(KEY_END_TIME, "");
    }
    public void setEnable(boolean enable){
        editor.putBoolean(KEY_ENABLE,enable);
        editor.apply();
    }
    public void setStartTime(String time){
        System.out.println("Start at 1 " + time);
        editor.putString(KEY_START_TIME,time);
        editor.apply();
    }

    public void setEndTime(String time){
        System.out.println("Start at 2 " + time);
        editor.putString(KEY_END_TIME,time);
        editor.apply();
    }
    public void setWeekdays(String days){
        editor.putString(KEY_WEEKDAYS,days);
        editor.apply();
    }

    public String getMonthDate() {
        System.out.println("All stored" +   sharedPreferences.getAll());
        return sharedPreferences.getString(KEY_MONTH_DATE, "");
    }
    public void setMonthDate(String monthDate){

        editor.putString(KEY_MONTH_DATE,monthDate);
        editor.apply();
    }
}
