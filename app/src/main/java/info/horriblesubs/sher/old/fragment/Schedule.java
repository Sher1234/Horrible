package info.horriblesubs.sher.old.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.old.task.FetchScheduleItems;
import info.horriblesubs.sher.old.task.LoadScheduleItems;

public class Schedule extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static Schedule newInstance(int sectionNumber) {
        Schedule fragment = new Schedule();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        final RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        assert getArguments() != null;
        new LoadScheduleItems(recyclerView, getContext(), swipeRefreshLayout,
                getArguments().getInt(ARG_SECTION_NUMBER))
                .execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchScheduleItems().execute("?mode=schedule");
                new LoadScheduleItems(recyclerView, getContext(), swipeRefreshLayout,
                        getArguments().getInt(ARG_SECTION_NUMBER))
                        .execute();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }
}