package info.horriblesubs.sher.old.service;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotifyService extends FirebaseMessagingService {

    private static final String TAG = NotifyService.class.getSimpleName();

    public NotifyService() {
    }

    private static boolean isAppInBackground(@NotNull Context context) {
        boolean isInBackground = true;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses =
                activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
            if (runningAppProcessInfo.importance == ActivityManager
                    .RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                for (String activeProcess : runningAppProcessInfo.pkgList)
                    if (activeProcess.equals(context.getPackageName()))
                        isInBackground = false;

        return isInBackground;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "DATA: " + remoteMessage.getData());
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void handleNotification(String message) {
        Log.d("message", message + "");
        if (!isAppInBackground(getApplicationContext())) {
            Intent intent = new Intent("horribleSubs.service.notification");
            intent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        playNotificationSound();
    }

    private void playNotificationSound() {
        try {
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName()
                    + "/raw/notification");
            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}