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
import info.horriblesubs.sher.old.task.FetchReleaseItems;

public class Home extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private String s;

    public static Home newInstance(int i) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shows, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.textView);
        assert getArguments() != null;
        s = "?mode=latest";
        if (getArguments().getInt(ARG_SECTION_NUMBER, 0) == 1)
            s = "?mode=latest-batch";
        new FetchReleaseItems(getContext(), recyclerView, swipeRefreshLayout).execute(s);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchReleaseItems(getContext(), recyclerView, swipeRefreshLayout).execute(s);
            }
        });
        return rootView;
    }
}