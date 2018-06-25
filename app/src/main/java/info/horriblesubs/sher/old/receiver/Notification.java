package info.horriblesubs.sher.old.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.old.task.NotificationRequest;

public class Notification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction()) ||
                Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences sharedPreferences = context
                    .getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
            boolean b = sharedPreferences.getBoolean("notification-on", false);
            if (b) {
                removeNotificationAlert(context.getApplicationContext());
                setNotificationAlert(context.getApplicationContext());
            }
        }
        new NotificationRequest().execute(BuildConfig.HAPI + "?mode=notification");
    }

    private void removeNotificationAlert(@NotNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notification-on", false).apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        Intent intent = new Intent(context, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }

    private void setNotificationAlert(@NotNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notification-on", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Intent intent = new Intent(context, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                AlarmManager.INTERVAL_HOUR, pendingIntent);
    }
}