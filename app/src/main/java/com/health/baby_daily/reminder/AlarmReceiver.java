package com.health.baby_daily.reminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.health.baby_daily.R;

import java.util.Calendar;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        String mReceivedID = intent.getStringExtra(ReminderEditActivity.EXTRA_REMINDER_ID);

        // Get notification title from Reminder Database
        ReminderDatabase rb = new ReminderDatabase(context);
        int id = Integer.parseInt(mReceivedID);
        Reminder reminder = rb.getReminder(id);
        String mTitle = reminder.getTitle();
        String mBaby = reminder.getBaby();

        // Create intent to open ReminderEditActivity on notification click
        Intent editIntent = new Intent(context, ReminderEditActivity.class);
        editIntent.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, mReceivedID);
        PendingIntent mClick = PendingIntent.getActivity(context, id, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = null;
        if (mTitle.equals("Bottle")){
            // Create Notification
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/bottle");
            mBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.bottlenew))
                    .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                    .setContentTitle(mTitle)
                    .setTicker("It time for " + mBaby + " to drink milk")
                    .setContentText("It time for " + mBaby + " to drink milk")
                    .setSound(alarmSound)
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
        }else if (mTitle.equals("Sleep")){
            // Create Notification
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/sleep");

            mBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.sleepnew))
                    .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                    .setContentTitle(mTitle)
                    .setTicker("It time for " + mBaby + " to sleep")
                    .setContentText("It time for " + mBaby + " to sleep")
                    .setSound(alarmSound)
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
        }else if (mTitle.equals("Diapers")){
            // Create Notification
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/diaper");

            mBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.diapernew))
                    .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                    .setContentTitle(mTitle)
                    .setTicker("It time for " + mBaby + " to defecate")
                    .setContentText("It time for " + mBaby + " to defecate")
                    .setSound(alarmSound)
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
        }else if (mTitle.equals("Feed")){
            // Create Notification
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/feed");

            mBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.feednew))
                    .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                    .setContentTitle(mTitle)
                    .setTicker("It time for " + mBaby + " to eat")
                    .setContentText("It time for " + mBaby + " to eat")
                    .setSound(alarmSound)
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
        }else if (mTitle.equals("Medicine") || mTitle.equals("Measurement") || mTitle.equals("Vaccine") || mTitle.equals("Appointment")){
            // Create Notification
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/appnotifysound");

            mBuilder = new NotificationCompat.Builder(context)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.overall))
                    .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                    .setContentTitle(mTitle)
                    .setTicker("Incoming event for " + mBaby)
                    .setContentText("Incoming event for " + mBaby)
                    .setSound(alarmSound)
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);
        }

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(Integer.parseInt(mReceivedID), mBuilder.build());
    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification time
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using notification time
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, int ID, long RepeatTime) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification timein
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using initial notification time and repeat interval time
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                RepeatTime , mPendingIntent);

        // Restart alarm if device is rebooted
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel Alarm using Reminder ID
        mPendingIntent = PendingIntent.getBroadcast(context, ID, new Intent(context, AlarmReceiver.class), 0);
        mAlarmManager.cancel(mPendingIntent);

        // Disable alarm
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}