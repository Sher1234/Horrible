package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        Log.d("HOME-SIZE", "" + i);
        for (int j = 0; j < i; j++) {
            ReleaseItem releaseItem = (ReleaseItem) intents[0].getSerializableExtra("extra-" + j);
            if (releaseItem != null)
                releaseItems.add(releaseItem);
        }
        while (true)
            if (releaseItems.size() == i)
                break;
        return releaseItems;
    }

    @Override
    protected void onPostExecute(List<ReleaseItem> releaseItems) {
        super.onPostExecute(releaseItems);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(new ReleaseRecycler(context, releaseItems));
    }
}