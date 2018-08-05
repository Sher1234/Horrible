package info.horriblesubs.sher.fragment;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.Strings;
import info.horriblesubs.sher.adapter.ScheduleRecycler;
import info.horriblesubs.sher.model.base.ScheduleItem;
import info.horriblesubs.sher.model.response.HomeResponse;

public class Today extends Fragment {

    private HomeResponse homeResponse;
    private RecyclerView recyclerView;

    public static Today newInstance(HomeResponse homeResponse) {
        Today fragment = new Today();
        Bundle args = new Bundle();
        args.putSerializable(Strings.ExtraData, homeResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        homeResponse = (HomeResponse) getArguments().getSerializable(Strings.ExtraData);
    }

    private List<ScheduleItem> getTodaySchedule() {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        for (ScheduleItem item: homeResponse.schedule) {
            cal1.setTime(item.getTime());
            if (cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK) && item.isScheduled)
                scheduleItems.add(item);
        }
        return scheduleItems;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getContext() != null;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ScheduleRecycler(getContext(), getTodaySchedule()));
    }
}