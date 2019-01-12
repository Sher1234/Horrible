package info.horriblesubs.sher.ui.horrible.schedule.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ScheduleAdapter;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.ScheduleItems;
import info.horriblesubs.sher.ui.horrible.show.Show;

@SuppressWarnings("ALL")
public class Day extends Fragment implements ScheduleAdapter.OnItemClick {

    private static final String ARG_RESPONSE = "RESPONSE-SCHEDULE";
    private static final String ARG_NUMBER = "NUMBER-SCHEDULE";

    private RecyclerView recyclerView;
    private ScheduleItems scheduleItems;
    private int position;

    public static Day newInstance(ScheduleItems scheduleItems, int position) {
        Day fragment = new Day();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, scheduleItems);
        args.putInt(ARG_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.horrible_3_c, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        position = getArguments().getInt(ARG_NUMBER, 0);
        scheduleItems = (ScheduleItems) getArguments().getSerializable(ARG_RESPONSE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        onLoadData();
    }

    private List<ScheduleItem> getSchedule() {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (ScheduleItem item : this.scheduleItems.items) {
            calendar.setTime(item.getDate());
            if (calendar.get(Calendar.DAY_OF_WEEK) == (position + 1) && item.scheduled)
                scheduleItems.add(item);
            else if (position == 7 && !item.scheduled)
                scheduleItems.add(item);
        }
        return scheduleItems;
    }

    private void onLoadData() {
        ScheduleAdapter scheduleRecycler = ScheduleAdapter.getAdapter(this, getSchedule());
        recyclerView.setAdapter(scheduleRecycler);
    }

    @Override
    public void onItemClicked(ScheduleItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }
}