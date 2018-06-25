package info.horriblesubs.sher.old.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class IdService extends FirebaseInstanceIdService {

    private static final String TAG = "HorribleSubsIdService";

    public IdService() {
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Intent intent = new Intent("horribleSubs.service.registered");
        intent.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        storeToken(refreshedToken);
    }

    private void storeToken(String s) {
        this.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("horriblesubs-prefs",
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("token", s).apply();
    }
}
