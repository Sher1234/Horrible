package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.activity.Home;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.model.ReleaseItem;

@SuppressLint("StaticFieldLeak")
public class LoadReleaseItems extends AsyncTask<Intent, Void, List<ReleaseItem>> {

    private Context context;
    private RecyclerView recyclerView;

    public LoadReleaseItems(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ReleaseItem> doInBackground(Intent... intents) {
        List<ReleaseItem> releaseItems = new ArrayList<>();
        int i = intents[0].getIntExtra("size", 0);
        for (int j = 0; j < i; j++) {
            ReleaseItem releaseItem = (ReleaseItem) intents[0].getSerializableExtra("extra-" + j);
            if (releaseItem != null)
                releaseItems.add(releaseItem);
        }
        while (true)
            if (releaseItems.size() == i && Home.searchView != null)
                break;
        return releaseItems;
    }

    @Override
    protected void onPostExecute(final List<ReleaseItem> releaseItems) {
        super.onPostExecute(releaseItems);
        final ReleaseRecycler releaseRecycler = new ReleaseRecycler(context, releaseItems);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(releaseRecycler);
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