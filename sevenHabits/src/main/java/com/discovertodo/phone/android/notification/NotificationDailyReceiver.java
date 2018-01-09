package com.discovertodo.phone.android.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v7.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.MainActivity;
import com.discovertodo.phone.android.global.Constant;
import com.discovertodo.phone.android.util.DailyBoostersUtil;

import org.json.JSONArray;

public class NotificationDailyReceiver extends BroadcastReceiver {

    private static final int idMessage = 11221122;
    private JSONArray arrBootster;

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("NOTIFY", "NotificationDailyReceiver");
        if (intent.getAction().equals(Constant.RECEIVER_NOTIFICATION_DAILY)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
            boolean notifyStatus = sharedPreferences.getBoolean(Constant.SETTING_NOTIFY_STATUS, true);
            if (!notifyStatus) {
                return;
            }
            Time time = new Time();
            time.setToNow();
            int weekDay = sharedPreferences.getInt(Constant.SETTING_NOTIFY_WEEK_DAY, 99);
            // SETTING_NOTIFY_WEEK_DAY = 99 có nghĩa là ngày nào cũng bắn
            if (weekDay != 99 && weekDay != time.weekDay) {
                return;
            }
            // Không trùng thơi gian cài đặt và thời gian hiện tại thì sẽ không bắn
            int hourNotify = sharedPreferences.getInt(Constant.SETTING_NOTIFY_HOUR, 0);
            int minuteNotify = sharedPreferences.getInt(Constant.SETTING_NOTIFY_MINUTE, 0);
            if (time.hour != hourNotify || time.minute != minuteNotify) {
                return;
            }
            DailyBoostersUtil.getDateInstall(context);
            arrBootster = DailyBoostersUtil.loadArray(context);
            int days = DailyBoostersUtil.getDaysBetweenCustom(context, time.year, time.month, time.monthDay);
            int count = days % arrBootster.length();
            String text = null;
            try {
                text = arrBootster.getString(count);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (text == null) {
                return;
            }
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(text);
            Notification notification = builder.build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_ALL;
            notificationManager.notify(idMessage, notification);
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wl.acquire(10000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // set Alarm for the next day
                Intent alarmIntent = new Intent(context, NotificationDailyReceiver.class);
                alarmIntent.setAction(Constant.RECEIVER_NOTIFICATION_DAILY);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                manager.setExact(AlarmManager.RTC_WAKEUP, AlarmStartReceiver.getTimeTrigger(context), pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // set Alarm for the next day
                Intent alarmIntent = new Intent(context, NotificationDailyReceiver.class);
                alarmIntent.setAction(Constant.RECEIVER_NOTIFICATION_DAILY);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, AlarmStartReceiver.getTimeTrigger(context), pendingIntent);
            }
        }
    }

}
