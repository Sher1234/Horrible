package info.horriblesubs.sher.ui.schedule.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ScheduleRecycler;
import info.horriblesubs.sher.model.base.ScheduleItem;
import info.horriblesubs.sher.model.response.ScheduleResponse;

public class Day extends Fragment {

    private static final String ARG_RESPONSE = "RESPONSE-SCHEDULE";
    private static final String ARG_NUMBER = "NUMBER-SCHEDULE";

    private ScheduleResponse scheduleResponse;
    private RecyclerView recyclerView;
    private int i;

    public static Day newInstance(ScheduleResponse scheduleResponse, int i) {
        Day fragment = new Day();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, scheduleResponse);
        args.putInt(ARG_NUMBER, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        scheduleResponse = (ScheduleResponse) getArguments().getSerializable(ARG_RESPONSE);
        i = getArguments().getInt(ARG_NUMBER, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        onLoadData(getContext());
    }

    private List<ScheduleItem> getSchedule() {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (ScheduleItem item : scheduleResponse.schedule) {
            calendar.setTime(item.getTime());
            if (calendar.get(Calendar.DAY_OF_WEEK) == i + 1 && item.isScheduled)
                scheduleItems.add(item);
            else if (i == 7 && !item.isScheduled)
                scheduleItems.add(item);
        }
        return scheduleItems;
    }

    private void onLoadData(Context context) {
        ScheduleRecycler scheduleRecycler = new ScheduleRecycler(context, getSchedule());
        recyclerView.setAdapter(scheduleRecycler);
    }
}