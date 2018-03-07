package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.activity.Detail;

@SuppressLint("StaticFieldLeak")
public class LoadShowReleases extends AsyncTask<Integer, Integer, String> {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Context context;

    public LoadShowReleases(RecyclerView recyclerView, Context context,
                            SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    @Override
    protected String doInBackground(@NotNull Integer... integers) {
        String s;
        if (integers[0] == 1)
            s = "?mode=id-episode&showId=";
        else if (integers[0] == 2)
            s = "?mode=id-batch&showId=";
        else
            s = "?mode=id-episode&showId=";
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