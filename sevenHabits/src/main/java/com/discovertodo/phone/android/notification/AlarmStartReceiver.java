package com.discovertodo.phone.android.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.discovertodo.phone.android.global.Constant;

import java.util.Calendar;

public class AlarmStartReceiver extends BroadcastReceiver {

//	private static long interval = 86400000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals(Constant.RECEIVER_ALARM_START)) {
            long timeTrigger = getTimeTrigger(context);
//        	Time timeNotify = new Time();
//        	timeNotify.set(timeTrigger);
//        	Log.e("NOTIFY  " + ((timeTrigger - System.currentTimeMillis())/1000f), timeNotify.monthDay + "   "
//					+ timeNotify.hour + "   " + timeNotify.minute + "   " + timeNotify.second);
            Intent alarmIntent = new Intent(context, NotificationDailyReceiver.class);
            alarmIntent.setAction(Constant.RECEIVER_NOTIFICATION_DAILY);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                manager.setExact(AlarmManager.RTC_WAKEUP, timeTrigger, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeTrigger, pendingIntent);
            } else {
                manager.setRepeating(AlarmManager.RTC_WAKEUP, timeTrigger,
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

    public static long getTimeTrigger(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
        int hourNotify = sharedPreferences.getInt(Constant.SETTING_NOTIFY_HOUR, 0);
        int minuteNotify = sharedPreferences.getInt(Constant.SETTING_NOTIFY_MINUTE, 0);
//		int secondNotify = sharedPreferences.getInt(Constant.SETTING_NOTIFY_SECOND, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourNotify);
        calendar.set(Calendar.MINUTE, minuteNotify);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//    	Time timeNow = new Time();
//    	Time timeNotify = new Time();
//		timeNow.setToNow();
//		timeNotify.set(secondNotify, minuteNotify, hourNotify,
//				timeNow.monthDay, timeNow.month, timeNow.year);
//    	long timeTrigger = timeNotify.toMillis(true);
//    	if(timeTrigger - timeNow.toMillis(true) <= 0){
//    		timeTrigger += interval;
//    	}
        long timeTrigger = calendar.getTimeInMillis();
        if (timeTrigger - System.currentTimeMillis() <= 0) {
            timeTrigger += AlarmManager.INTERVAL_DAY;
        }
        return timeTrigger;
    }


}
