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

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.activity.Detail;
import info.horriblesubs.sher.model.PageItem;

@SuppressLint("StaticFieldLeak")
public class FetchPageItem extends AsyncTask<String, String, PageItem> {

    public FetchPageItem() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected PageItem doInBackground(String... strings) {
        String s = BuildConfig.HAPI + strings[0];
        try {
            URL url = new URL(s);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(httpURLConnection.getInputStream()));
                return new Gson().fromJson(bufferedReader,
                        new TypeToken<PageItem>() {
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
    protected void onPostExecute(PageItem pageItem) {
        super.onPostExecute(pageItem);
        Detail.pageItem = pageItem;
    }
}