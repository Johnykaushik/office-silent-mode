package com.officeMode;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TimePicker;

import java.util.Arrays;
import java.util.Calendar;
import android.app.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import android.media.AudioManager;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private Switch switchScheduler;
    private List<CheckBox> checkBoxes;
    private TextView textSelectedWeekdays;
    private Button buttonStartTime;
    private Button buttonEndTime;

    private Button openCalender;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private Map<String, Map<String, Integer>> scheduleTime = new HashMap<String, Map<String, Integer>>();

    private List<String> selectedWeekdays;
    private ScheduleStore store;


    private List<Long> selectedDateList = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");



    Utils utils = new Utils();
    ScheduleModel scheduleModel = new ScheduleModel();
    private RecyclerView recyclerView;
    private MonthAdapter monthAdapter;

   private List<MonthData> monthDataList = new ArrayList<>();
    private void setStoreSchedule() {
        switchScheduler.setChecked(store.getIsEnable());
        String startTime = store.getStartTime();
        String endTime = store.getEndTime();
        String weekDays = store.getWeekdays();
        setTimeInStoreView(startTime, "start", false);
        setTimeInStoreView(endTime, "end", false);
        textSelectedWeekdays.setText("Selected Weekdays: " + weekDays);
        setWeekdayStatus(weekDays);
    }

    private void startSchedule(Intent serviceIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void stopSchedule(Intent serviceIntent) {
        stopService(serviceIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dndGrantAccess();

        switchScheduler = findViewById(R.id.switch_scheduler);
        checkBoxes = new ArrayList<>();
        checkBoxes.add((CheckBox) findViewById(R.id.checkbox_monday));
        checkBoxes.add((CheckBox) findViewById(R.id.checkbox_tuesday));
        checkBoxes.add((CheckBox) findViewById(R.id.checkbox_wednesday));
        checkBoxes.add((CheckBox) findViewById(R.id.checkbox_thursday));
        checkBoxes.add((CheckBox) findViewById(R.id.checkbox_friday));
        checkBoxes.add((CheckBox) findViewById(R.id.checkbox_saturday));
        checkBoxes.add((CheckBox) findViewById(R.id.checkbox_sunday));
        textSelectedWeekdays = findViewById(R.id.text_selected_weekdays);
        openCalender = findViewById(R.id.openCalender);


        buttonStartTime = findViewById(R.id.button_start_time);
        buttonEndTime = findViewById(R.id.button_end_time);
        startTimeTextView = findViewById(R.id.startTimeText);
        endTimeTextView = findViewById(R.id.endTimeText);
        selectedWeekdays = new ArrayList<>();
        // Set existing stored values at init

        store = new ScheduleStore(this);
        setStoreSchedule();
        //      end


        Intent serviceIntent = new Intent(this, ScheduleService.class);

        switchScheduler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle scheduler switch state change
                store.setEnable(isChecked);

                if (isChecked) {
                    startSchedule(serviceIntent);
                } else {
                    stopSchedule(serviceIntent);
                }

                if (isChecked) {
//                    enableSilentMode();
                } else {
//                    disableSilentMode();
                }
            }
        });

        for (final CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Handle weekday checkbox state change
                    if (isChecked) {
                        // Add selected weekday to the list
                        selectedWeekdays.add(checkBox.getText().toString());
                    } else {
                        // Remove unselected weekday from the list
                        selectedWeekdays.remove(checkBox.getText().toString());
                    }

                    // Update the display of selected weekdays
                    updateSelectedWeekdays();
                }
            });
        }

        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle start time button click
                showTimePickerDialog("start");
            }
        });

        buttonEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog("end");
            }
        });
//       calender open button
        openCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int date) {
                                updateSelectedDatesText(month,date);
                                System.out.println("my current selected date " + year  + " month " + month + " day of year" + date);
                            }
                        },
                        year, month, day);
                // set min day of calender that is today
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

                //  set max date from now (2 months from now)
                Calendar max = Calendar.getInstance();
                max.add(Calendar.MONTH,2);
                datePickerDialog.getDatePicker().setMaxDate(max.getTimeInMillis());
                datePickerDialog.show();
            }
        });

    try {
        if(store.getIsEnable()){
            startSchedule(serviceIntent);
        }else {
            stopSchedule(serviceIntent);
        }
    }catch(Exception er){
        System.out.println("i have a erro " + er.getMessage());
    }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        monthDataList = new ArrayList<>(); // Replace with your data
        monthAdapter = new MonthAdapter(monthDataList,store);
        recyclerView.setAdapter(monthAdapter);

        setMonthDateFromStore(true);
    }
    private void showTimePickerDialog(String type) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if(type == "start"){
                            setTimeInStoreView(hourOfDay + ":" + minute,"start",true);
                        }else {
                            setTimeInStoreView(hourOfDay + ":" + minute,"end",true);
                        }
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private String formatTime(int hourOfDay, int minute) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return timeFormat.format(calendar.getTime());
    }
    private void updateSelectedWeekdays() {
        StringBuilder builder = new StringBuilder();
        for (String weekday : selectedWeekdays) {
            builder.append(weekday).append(", ");
        }

        if (builder.length() > 0) {
            // Remove the trailing comma and space
            builder.setLength(builder.length() - 2);
        }

        store.setWeekdays(builder.toString());
        textSelectedWeekdays.setText("Selected Weekdays: " + builder.toString());
    }

    private boolean compareTime(int hours,int mins){
            Calendar currentTme = Calendar.getInstance();
            System.out.println("Current time is " + currentTme);
            Calendar ourTime = Calendar.getInstance();
            ourTime.set(Calendar.HOUR_OF_DAY,hours);
            ourTime.set(Calendar.MINUTE,mins);
            System.out.println("set time is " + ourTime);
            return currentTme.compareTo(ourTime) == 0;
    }

    private void dndGrantAccess(){
        NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(!n.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }


    private void setTimeInStoreView(String time,String type,Boolean saveInStore){
        if(time != null){
            String[] timeAr = time.split(":");

            Log.d("errors", "setTimeInStoreView: " + time);
            int timeLen = timeAr.length;
            if(timeLen >= 2){
                try {
                    Integer hour = Integer.parseInt(timeAr[0]);
                    Integer min = Integer.parseInt(timeAr[1]);

                    String formattedTime = formatTime(hour, min);
                    if(type == "start"){
                        startTimeTextView.setText(formattedTime);
                        if(saveInStore){
                            store.setStartTime(hour + ":" + min);
                        }
                    }else  if(type == "end"){
                        endTimeTextView.setText(formattedTime);
                        if(saveInStore){
                            store.setEndTime(hour + ":" + min);
                        }
                    }

                    Map<String, Integer> timeSet = new HashMap<>();
                    timeSet.put("hours", hour);
                    timeSet.put("minute", min);
                    scheduleTime.put(type, timeSet);

                }catch (Exception ex){
                    System.out.println("time set has error 1" + ex.getMessage());
                    System.out.println(ex.getStackTrace());

                }
            }
        }
    }
    private void setWeekdayStatus(String weekDays){
        if(weekDays != null){
            String[] days = weekDays.split(", ");
            Map<String,Boolean> weekMap = new HashMap<>();
            for(String day  : days){
                weekMap.put(day,true);
                System.out.println("selected days " + day);
            }
            try {
                for (final CheckBox checkBox : checkBoxes) {
                    if (weekMap.containsKey(checkBox.getText())) {
                        checkBox.setChecked(true);
                         selectedWeekdays.add(checkBox.getText().toString());
                    }
                }
            }catch(Exception ex){
                System.out.println("found a erro " + ex.getMessage());
            }
        }
    }

    private void updateSelectedDatesText(int month, int date) {

        try {
        String[] monthsName = utils.montName();
        String monthName = monthsName[month];
        HashMap<String, HashSet<Integer>>  monthDays =  setMonthDateFromStore(false);
//        System.out.println("monthDaysmonthDays " + monthDays);
            if(monthDays != null){
                if(monthDays.containsKey(monthName)){
                    monthDays.get(monthName).add(date);
                }else {
                    HashSet<Integer> dateSet = new HashSet<>();
                    dateSet.add(date);
                    monthDays.put(monthName,dateSet);
                }
                scheduleModel.setMonthDays(monthDays);
            }   else {
                HashMap<String,HashSet<Integer>>  monthDaysInit = new HashMap<>();
                HashSet<Integer> dateSetB = new HashSet<>();
                dateSetB.add(date);
                monthDaysInit.put(monthName,dateSetB);
                scheduleModel.setMonthDays(monthDaysInit);
            }

            HashMap<String, HashSet<Integer>>  monthDaysUpdated = scheduleModel.getMonthDays();

            StringBuilder formattedDates = new StringBuilder();

            for(Map.Entry<String, HashSet<Integer>> entry : monthDaysUpdated.entrySet()){
                String nameOfMonth = entry.getKey();
                HashSet<Integer> allDays =  entry.getValue();
                formattedDates.append(nameOfMonth + "=");
                for(int i : allDays){
                    formattedDates.append(i+"_");
                }
                formattedDates.append(",");
            }
            store.setMonthDate(String.valueOf(formattedDates));
            setMonthDateFromStore(true);
//            System.out.println("Install successfully " + date + " " +  month + ">>>>>>>> " +   formattedDates + "already saved " + store.getMonthDate());

        }catch (Exception ex){
            System.out.println("date has error " + ex.getMessage() + "line" + ex);
        }
    }

    public void sampleTest(){
        System.out.println("function classed");
    }
    public HashMap<String, HashSet<Integer>> setMonthDateFromStore(Boolean isSet){


        String storeDates =  store.getMonthDate();
        HashMap<String, HashSet<Integer>>  monthDays = new HashMap<>();
        if(isSet){
            monthDataList.clear();
        }
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
                                if(isSet) {
                                    monthDataList.add(new MonthData(monthName, Integer.parseInt(date)));
                                    monthAdapter.notifyDataSetChanged();
                                }
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
