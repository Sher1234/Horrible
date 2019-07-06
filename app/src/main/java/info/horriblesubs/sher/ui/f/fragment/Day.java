package info.horriblesubs.sher.ui.f.fragment;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.adapter.ScheduleAdapter;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.ui.i.Show;

import static java.util.Calendar.DAY_OF_WEEK;

public class Day implements ScheduleAdapter.OnItemClick {

    private final RecyclerView recyclerView;
    private final List<ScheduleItem> items;
    private final Context context;
    private final int position;

    public Day(Context context, RecyclerView recyclerView, List<ScheduleItem> items, int position) {
        this.recyclerView = recyclerView;
        this.position = position;
        this.context = context;
        this.items = items;
        onLoad();
    }

    private void onLoad() {
        recyclerView.setLayoutManager(new GridLayoutManager(context, AppMe.appMe.isPortrait()?1:2));
        ScheduleAdapter adapter = ScheduleAdapter.get(this, getSchedule());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    private List<ScheduleItem> getSchedule() {
        if (items != null) {
            List<ScheduleItem> scheduleItems = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            for (@NotNull ScheduleItem item : items) {
                if ((item.getDate() == null || !item.scheduled) && position == 7) {
                    scheduleItems.add(item);
                } else {
                    if (item.getDate() == null) continue;
                    calendar.setTime(item.getDate());
                    if (calendar.get(DAY_OF_WEEK) == (position + 1) && item.scheduled)
                        scheduleItems.add(item);
                }
            }
            return scheduleItems;
        } else return null;
    }

    @Override
    public void onItemClicked(ScheduleItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(context, Show.class);
        intent.putExtra("show.link", item.link);
        context.startActivity(intent);
    }
}