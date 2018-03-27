package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.activity.Detail;

@SuppressLint("StaticFieldLeak")
public class FetchShowReleases extends AsyncTask<Integer, Integer, String> {

    private final SwipeRefreshLayout swipeRefreshLayout;
    private final RecyclerView recyclerView;
    private final Context context;

    public FetchShowReleases(RecyclerView recyclerView, Context context,
                             SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    @Override
    protected String doInBackground(@NotNull Integer... integers) {
        String s;
        switch (integers[0]) {
            case 1:
                s = "?mode=id-episode&showId=";
                break;
            case 2:
                s = "?mode=id-batch&showId=";
                break;
            default:
                s = "?mode=id-episode&showId=";
                break;
        }
        while (true)
            if (Detail.pageItem != null)
                break;
        return s + Detail.pageItem.id;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        new FetchReleaseItems(context, recyclerView, swipeRefreshLayout).execute(s);
    }
}