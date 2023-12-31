package com.officeMode;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ScheduleStore {
    private static final String PREFERENCES_NAME = "schedulePref";
    private static final String KEY_ENABLE = "schedulerStarted";
    private static final String KEY_WEEKDAYS = "weekdays";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";

    private static final String KEY_MONTH_DATE = "monthDate";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ScheduleStore(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove("isEnable");
        editor.remove("isEnabled");
        editor.apply();
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
        return sharedPreferences.getBoolean(KEY_ENABLE,false);
    }

    public String getWeekdays() {
        return sharedPreferences.getString(KEY_WEEKDAYS, "");
    }

    public String getStartTime() {
        return sharedPreferences.getString(KEY_START_TIME, "");
    }

    public String getEndTime() {
        return sharedPreferences.getString(KEY_END_TIME, "");
    }
    public void setEnable(boolean enable){
        editor.putBoolean(KEY_ENABLE,enable);
        editor.apply();
    }
    public void setStartTime(String time){
        editor.putString(KEY_START_TIME,time);
        editor.apply();
    }

    public void setEndTime(String time){
        editor.putString(KEY_END_TIME,time);
        editor.apply();
    }
    public void setWeekdays(String days){
        editor.putString(KEY_WEEKDAYS,days);
        editor.apply();
    }

    public String getMonthDate() {
        return sharedPreferences.getString(KEY_MONTH_DATE, "");
    }
    public void setMonthDate(String monthDate){

        editor.putString(KEY_MONTH_DATE,monthDate);
        editor.apply();
    }

    public void getAll(){
        Log.d("TAG", "getAll: " + sharedPreferences.getAll());
    }
}
