package info.horriblesubs.sher.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.task.NotificationRequest;

public class Notification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction()) ||
                Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences sharedPreferences = context
                    .getSharedPreferences("horriblesubs", Context.MODE_PRIVATE);
            boolean b = sharedPreferences.getBoolean("alarmSet", false);
            if (b)
                setAlarm(context.getApplicationContext());
        }
        new NotificationRequest().execute(BuildConfig.HAPI + "?mode=notification");
    }

    private void setAlarm(Context context) {
        Intent intent = new Intent(context, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                1200000, pendingIntent);
    }
}