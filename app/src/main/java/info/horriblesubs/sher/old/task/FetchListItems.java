package info.horriblesubs.sher.old.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.adapter.ListRecycler;
import info.horriblesubs.sher.model.base.Item;

@SuppressLint("StaticFieldLeak")
public class FetchListItems extends AsyncTask<String, String, List<Item>> {

    private final Context context;
    private final RecyclerView recyclerView;
    private final SwipeRefreshLayout swipeRefreshLayout;

    public FetchListItems(Context context, RecyclerView recyclerView,
                          SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected List<Item> doInBackground(String... strings) {
        String s = BuildConfig.HAPI + strings[0];
        try {
            URL url = new URL(s);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(httpURLConnection.getInputStream()));
                return new Gson().fromJson(bufferedReader,
                        new TypeToken<List<Item>>() {
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
    protected void onPostExecute(final List<Item> items) {
        super.onPostExecute(items);
        final ListRecycler listRecycler = new ListRecycler(context, items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(listRecycler);
        swipeRefreshLayout.setRefreshing(false);
    }
}