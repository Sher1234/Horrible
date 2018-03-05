package info.horriblesubs.sher.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.task.LoadScheduleItems;

public class Schedule extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SCHEDULE_MODE = "schedule_mode";

    public static Schedule newInstance(int sectionNumber, String s) {
        Schedule fragment = new Schedule();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SCHEDULE_MODE, s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_home, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        assert getArguments() != null;
        new LoadScheduleItems(recyclerView, getContext(), swipeRefreshLayout,
                getArguments().getString(ARG_SCHEDULE_MODE), getArguments().getInt(ARG_SECTION_NUMBER))
                .execute();
        return rootView;
    }
}