package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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

    private Context context;
    private String string;
    private TextView textView;
    private boolean b;
    private String[] strings = {"Initializing App...", "Connecting to Server...",
            "Fetching Latest Releases...", "Verifying Data...", "Parsing Received Data..."};
    private int i = 0;

    public FetchScheduleItems(Context context, TextView textView, String string) {
        this.string = string;
        this.context = context;
        this.textView = textView;
        b = true;
    }

    public FetchScheduleItems(Context context, String string) {
        this.string = string;
        this.context = context;
        b = false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        publishProgress(strings[i++]);
    }

    @Override
    protected void onProgressUpdate(String... strings) {
        super.onProgressUpdate(strings);
        if (!b)
            return;
        String s = String.valueOf(textView.getText()).concat("\n").concat(strings[0]);
        textView.setText(s);
    }

    @Override
    protected List<ScheduleItem> doInBackground(String... strings) {
        publishProgress(this.strings[i++]);
        String s = BuildConfig.HAPI + strings[0];
        try {
            URL url = new URL(s);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                publishProgress(this.strings[i++]);
                httpURLConnection.connect();
                publishProgress(this.strings[i++]);
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
            if (b) {
                publishProgress(strings[i++]);
                Intent intent = new Intent(context, Schedule.class);
                intent.putExtra("mode", string);
                intent.putExtra("size", scheduleItems.size());
                Schedule.scheduleItems = scheduleItems;
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();
            } else {
                Schedule.scheduleItems = scheduleItems;
            }
        else
            publishProgress("Error Fetching Data from Server\nTry Again Later...");
    }
}