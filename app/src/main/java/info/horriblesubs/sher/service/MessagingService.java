package info.horriblesubs.sher.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.Strings;
import info.horriblesubs.sher.activity.Show;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "HzMessageService";

    public MessagingService() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token: " + s);
        Intent intent = new Intent("horribleSubs.service.registered");
        intent.putExtra("token", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        storeToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0)
            sendNotification(remoteMessage.getData());
    }

    private void storeToken(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putString("token", s).apply();
    }

    private void sendNotification(@NotNull Map<String, String> map) {
        Intent intent = new Intent(this, Show.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String[] strings = map.get("link").split("/");
        intent.putExtra("link", strings[strings.length - 1]);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                new Random().nextInt(100) + Integer.parseInt(map.get("number")),
                intent, PendingIntent.FLAG_ONE_SHOT);
        String s = "Episode " + map.get("number") + " subs available...";
        String id = getString(R.string.notification_channel);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, id)
                        .setSmallIcon(R.drawable.ic_notification_new)
                        .setContentTitle(map.get("title"))
                        .setAutoCancel(true)
                        .setContentText(s)
                        .setSound(uri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id,
                    "New horrible releases", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(new Random().nextInt(100) + Integer.parseInt(map.get("number")),
                notificationBuilder.build());
    }
}