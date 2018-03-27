package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.activity.Schedule;
import info.horriblesubs.sher.model.ScheduleItem;

@SuppressLint("StaticFieldLeak")
public class FetchScheduleItems extends AsyncTask<String, String, List<ScheduleItem>> {

    public FetchScheduleItems() {
    }

    @Override
    protected List<ScheduleItem> doInBackground(String... strings) {
        String s = BuildConfig.HAPI + strings[0];
        try {
            URL url = new URL(s);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(httpURLConnection.getInputStream()));
                return new Gson().fromJson(bufferedReader,
                        new TypeToken<List<ScheduleItem>>() {
                        }.getType());
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ScheduleItem> scheduleItems) {
        super.onPostExecute(scheduleItems);
        if (scheduleItems != null)
                Schedule.scheduleItems = scheduleItems;
    }
}