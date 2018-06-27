package info.horriblesubs.sher.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ListRecycler;
import info.horriblesubs.sher.model.response.ShowsResponse;

public class ShowsFragment extends Fragment {

    private static final String ARG_RESPONSE = "RESPONSE-SHOWS";
    private static final String ARG_NUMBER = "NUMBER-SHOWS";

    private ShowsResponse showsResponse;
    private RecyclerView recyclerView;
    private int i;

    public static ShowsFragment newInstance(ShowsResponse homeResponse, int i) {
        ShowsFragment fragment = new ShowsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, homeResponse);
        args.putInt(ARG_NUMBER, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shows, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        showsResponse = (ShowsResponse) getArguments().getSerializable(ARG_RESPONSE);
        i = getArguments().getInt(ARG_NUMBER, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        onLoadData();
    }

    private void onLoadData() {
        ListRecycler listRecycler;
        if (i == 0)
            listRecycler = new ListRecycler(getContext(), showsResponse.current);
        else
            listRecycler = new ListRecycler(getContext(), showsResponse.all);
        recyclerView.setAdapter(listRecycler);
    }
}