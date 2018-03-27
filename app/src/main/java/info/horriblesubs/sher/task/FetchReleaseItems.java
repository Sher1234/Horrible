package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.activity.Home;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.model.ReleaseItem;

@SuppressLint("StaticFieldLeak")
public class FetchReleaseItems extends AsyncTask<String, String, List<ReleaseItem>> {

    private final Context context;
    private final RecyclerView recyclerView;
    private final SwipeRefreshLayout swipeRefreshLayout;

    public FetchReleaseItems(Context context, RecyclerView recyclerView,
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
    protected List<ReleaseItem> doInBackground(String... strings) {
        String s = BuildConfig.HAPI + strings[0];
        try {
            URL url = new URL(s);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            try {
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(httpURLConnection.getInputStream()));
                return new Gson().fromJson(bufferedReader,
                        new TypeToken<List<ReleaseItem>>() {
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
    protected void onPostExecute(final List<ReleaseItem> releaseItems) {
        super.onPostExecute(releaseItems);
        if (releaseItems != null) {
            final ReleaseRecycler releaseRecycler = new ReleaseRecycler(context, releaseItems);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.setAdapter(releaseRecycler);
            swipeRefreshLayout.setRefreshing(false);
            if (Home.searchView != null)
                Home.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        List<ReleaseItem> releaseItems1 = new ArrayList<>();
                        for (ReleaseItem releaseItem : releaseItems) {
                            String s = releaseItem.title + " - " + releaseItem.number;
                            if (s.toLowerCase().contains(newText.toLowerCase()))
                                releaseItems1.add(releaseItem);
                        }
                        releaseRecycler.onQueryUpdate(releaseItems1);
                        return false;
                    }
                });
        }
    }
}