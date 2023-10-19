package com.officeMode;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleService extends Service {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    private ScheduleStore store;
    private String TAG = "debug-services";

    private static final CharSequence CHANNEL_NAME = "Your Channel Name";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private Integer[] getTimeSpl(String time) {
        Integer hour = null;
        Integer min = null;

        if (time != null) {
            String[] timeAr = time.split(":");

            Log.d("errors", "setTimeInStoreView: " + time);
            int timeLen = timeAr.length;
            if (timeLen >= 2) {
                hour = Integer.parseInt(timeAr[0]);
                min = Integer.parseInt(timeAr[1]);
            }
        }
        Integer[] times = {hour, min};
        return times;
    }

    private void checkScheduleStatus() {
        try {
            store = new ScheduleStore(this);
            String daysList[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            int weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            boolean isEnable = store.getIsEnable();
            if (isEnable) {
                String startTime = store.getStartTime();
                String endTime = store.getEndTime();
                String weekDays = store.getWeekdays();
                if (weekDays != null) {
                    String[] days = weekDays.split(", ");
                    Map<String, Boolean> weekMap = new HashMap<>();
                    for (String day : days) {
                        weekMap.put(day, true);
                    }
                    weekDay = weekDay - 1;
                    String today = daysList[weekDay];

                    if (weekMap.containsKey(today)) {
                        Integer[] startAt = getTimeSpl(startTime);
                        Integer[] endAt = getTimeSpl(endTime);
//                    Log.d(TAG, "checkScheduleStatus: startH " + startAt[0]);
//                    Log.d(TAG, "checkScheduleStatus: startM " + startAt[1]);
//                    Log.d(TAG, "checkScheduleStatus: endH " + endAt[0]);
//                    Log.d(TAG, "checkScheduleStatus: endM " + endAt[1]);
                        Calendar calendar = Calendar.getInstance();
                        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                        Integer minute = calendar.get(Calendar.MINUTE);

                        Integer startH = startAt[0];
                        Integer startM = startAt[1];
                        Integer endH = endAt[0];
                        Integer endM = endAt[1];
                        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                        int currentMode = audioManager.getRingerMode();
                        if (startH != null && startM != null && endH != null && endM != null) {
                            Calendar startTimeU = Calendar.getInstance();
                            startTimeU.set(Calendar.HOUR_OF_DAY, startH);
                            startTimeU.set(Calendar.MINUTE, startM);

                            Calendar endTimeU = Calendar.getInstance();
                            endTimeU.set(Calendar.HOUR_OF_DAY, endH);
                            endTimeU.set(Calendar.MINUTE, endM);

                            long currentTimestamp = calendar.getTimeInMillis() / 1000;
                            long startTimestamp = startTimeU.getTimeInMillis() / 1000;
                            long endTimestamp = endTimeU.getTimeInMillis() / 1000;


                            Log.d(TAG, "start time: " + currentTimestamp + " start " + startTimestamp + " end " + endTimestamp);

//                        if(currentMode == AudioManager.RINGER_MODE_NORMAL){
                            if (startTimestamp <= currentTimestamp && currentTimestamp < endTimestamp) {
                                if (currentMode != AudioManager.RINGER_MODE_VIBRATE) {
                                    dndGrantAccess(AudioManager.RINGER_MODE_VIBRATE);
                                    Log.d(TAG, "can enable: timer ");
                                }
                            }
//                        } else if(currentMode == AudioManager.RINGER_MODE_VIBRATE){
                            if (endTimestamp > startTimestamp && currentTimestamp >= endTimestamp) {
                                if (currentMode != AudioManager.RINGER_MODE_NORMAL) {
                                    dndGrantAccess(AudioManager.RINGER_MODE_NORMAL);
                                    Log.d(TAG, "can disable: timer ");
                                }
                            }
//                        }
                        }
                    }else {
//                        dndGrantAccess(AudioManager.RINGER_MODE_NORMAL);
                    }
                }
            }
        } catch (Exception ex) {
            Log.d("errrr", "checkScheduleStatus: " + ex.getMessage());
        }
    }

    private void dndGrantAccess(int mode) {
        NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (n.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(mode);
        } else {
            // Ask the user to grant access
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForeground(NOTIFICATION_ID, createNotification());
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Office Silent mode" )
                .setSmallIcon(R.drawable.scheduler_background)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        // Start the service in the foreground with the notification
        startForeground(NOTIFICATION_ID, builder.build());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        checkScheduleStatus();
                        System.out.println("Service running");

                    }
                });
            }
        }, 20000, 1000);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}