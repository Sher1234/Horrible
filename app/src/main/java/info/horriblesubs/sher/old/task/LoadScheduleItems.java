package info.horriblesubs.sher.old.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.horriblesubs.sher.adapter.ScheduleRecycler;
import info.horriblesubs.sher.model.base.ScheduleItem;

@SuppressLint("StaticFieldLeak")
public class LoadScheduleItems extends AsyncTask<Void, Void, List<ScheduleItem>> {

    private final int i;
    private final Context context;
    private final RecyclerView recyclerView;
    private final SwipeRefreshLayout swipeRefreshLayout;

    public LoadScheduleItems(RecyclerView recyclerView, Context context,
                             SwipeRefreshLayout swipeRefreshLayout, int i) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
        this.context = context;
        this.i = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected List<ScheduleItem> doInBackground(Void... voids) {
        while (true)
            if (null != null)
                break;
        List<ScheduleItem> scheduleItems = null;
        if (scheduleItems != null) {
            List<ScheduleItem> scheduleItemList = new ArrayList<>();
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            for (ScheduleItem scheduleItem : scheduleItems) {
                calendar1.setTime(scheduleItem.getTime());
                if (i == 1) {
                    if (calendar1.get(Calendar.DAY_OF_WEEK) == calendar2.get(Calendar.DAY_OF_WEEK)
                            && scheduleItem.isScheduled)
                        scheduleItemList.add(scheduleItem);
                } else if (i == 9 && !scheduleItem.isScheduled)
                    scheduleItemList.add(scheduleItem);
                else if (calendar1.get(Calendar.DAY_OF_WEEK) == i - 1 && scheduleItem.isScheduled)
                    scheduleItemList.add(scheduleItem);
            }
            return scheduleItemList;
        } else
            return null;
    }

    @Override
    protected void onPostExecute(List<ScheduleItem> scheduleItems) {
        super.onPostExecute(scheduleItems);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(new ScheduleRecycler(context, scheduleItems));
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }
}