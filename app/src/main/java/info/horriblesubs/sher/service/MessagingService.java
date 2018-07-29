package info.horriblesubs.sher.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import info.horriblesubs.sher.Strings;

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
    }

    private void storeToken(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putString("token", s).apply();
    }
}