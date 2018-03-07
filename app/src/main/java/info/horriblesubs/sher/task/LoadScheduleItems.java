package info.horriblesubs.sher.task;

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

import info.horriblesubs.sher.activity.Schedule;
import info.horriblesubs.sher.adapter.ScheduleRecycler;
import info.horriblesubs.sher.model.ScheduleItem;

@SuppressLint("StaticFieldLeak")
public class LoadScheduleItems extends AsyncTask<Void, Void, List<ScheduleItem>> {

    private int i;
    private String string;
    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public LoadScheduleItems(RecyclerView recyclerView, Context context,
                             SwipeRefreshLayout swipeRefreshLayout, String string, int i) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
        this.context = context;
        this.string = string;
        this.i = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected List<ScheduleItem> doInBackground(Void... voids) {
        while (true)
            if (Schedule.scheduleItems != null)
                break;
        List<ScheduleItem> scheduleItems = Schedule.scheduleItems;
        if (scheduleItems != null) {
            List<ScheduleItem> scheduleItemList = new ArrayList<>();
            Calendar calendar1 = Calendar.getInstance();
            switch (string) {
                case "all":
                    for (ScheduleItem scheduleItem : scheduleItems) {
                        calendar1.setTime(scheduleItem.getDate());
                        if (i == 8 && !scheduleItem.isScheduled)
                            scheduleItemList.add(scheduleItem);
                        else if (calendar1.get(Calendar.DAY_OF_WEEK) == i && scheduleItem.isScheduled)
                            scheduleItemList.add(scheduleItem);
                    }
                    return scheduleItemList;

                case "today":
                    Calendar calendar2 = Calendar.getInstance();
                    for (ScheduleItem scheduleItem : scheduleItems) {
                        calendar1.setTime(scheduleItem.getDate());
                        if (calendar1.get(Calendar.DAY_OF_WEEK) == calendar2.get(Calendar.DAY_OF_WEEK)
                                && scheduleItem.isScheduled)
                            scheduleItemList.add(scheduleItem);
                    }
                    return scheduleItemList;

                default:
                    return scheduleItems;
            }
        } else
            return null;
    }

    @Override
    protected void onPostExecute(List<ScheduleItem> scheduleItems) {
        super.onPostExecute(scheduleItems);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(new ScheduleRecycler(context, scheduleItems));
        swipeRefreshLayout.setRefreshing(false);
    }
}