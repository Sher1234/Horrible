package info.horriblesubs.sher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ListRecycler;
import info.horriblesubs.sher.model.response.ShowsResponse;

public class ShowsF extends Fragment {

    private static final String ARG_RESPONSE = "RESPONSE-SHOWS";
    private static final String ARG_NUMBER = "NUMBER-SHOWS";

    private ShowsResponse showsResponse;
    private RecyclerView recyclerView;
    private int i;

    public static ShowsF newInstance(ShowsResponse homeResponse, int i) {
        ShowsF fragment = new ShowsF();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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