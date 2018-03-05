package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * NotificationRequest via FCM AsyncTask.
 */

@SuppressLint("StaticFieldLeak")
public class NotificationRequest extends AsyncTask<String, Void, Void> {

    public NotificationRequest() {
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(strings[0]).openConnection();
            Log.d("Response Message", httpURLConnection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return null;
    }

}
