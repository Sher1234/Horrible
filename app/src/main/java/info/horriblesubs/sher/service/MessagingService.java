package info.horriblesubs.sher.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.common.Constants;
import info.horriblesubs.sher.db.DataMethods;
import info.horriblesubs.sher.ui.i.Show;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "AniDex.Messaging.Code";

    public MessagingService() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token: " + s);
        Intent intent = new Intent("notification.horrible.latest");
        intent.putExtra("token", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        onStoreToken(s);
    }

    private void onStoreToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        sharedPreferences.edit().putString("token", token).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0)
            if (!isNotified(remoteMessage.getData()))
                onCreateNotification(remoteMessage.getData());
    }

    private boolean isNotified(@NotNull Map<String, String> map) {
        DataMethods dataMethods = new DataMethods(this);
        if (dataMethods.isNotified(map.get("id"))) return true;
        if (dataMethods.onNotify(map) != -1) return false;
        return false;
    }

    private void onCreateNotification(@NotNull Map<String, String> map) {
        String release = map.get("release");
        String title = map.get("title");
        String link = map.get("link");
        Intent intent = new Intent(this, Show.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("show.link", link);
        int reqCode = new Random().nextInt();
        PendingIntent pIntent = PendingIntent.getActivity(this, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String s = "New release available.";
        if (release != null && !release.isEmpty())
            if (release.contains("-")) s = "Batch of " + release + " is now available...";
            else s = "Episode " + release + " is now available...";
        String channelId = getResources().getString(R.string.notification_channel);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification_new)
                .setContentTitle(Html.fromHtml(title))
                .setContentIntent(pIntent)
                .setGroup("HorribleSubs")
                .setAutoCancel(true)
                .setColorized(true)
                .setContentText(s)
                .setSound(uri);
        onNotify(builder.build(), reqCode, channelId);
    }

    private void onNotify(Notification notification, int code, String id) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            manager.createNotificationChannel(new NotificationChannel(id, "HorribleSubs",
                    NotificationManager.IMPORTANCE_DEFAULT));
        manager.notify(code, notification);
    }
}