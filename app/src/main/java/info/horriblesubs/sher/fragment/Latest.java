package info.horriblesubs.sher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.Strings;
import info.horriblesubs.sher.adapter.LatestRecycler;
import info.horriblesubs.sher.model.response.HomeResponse;

public class Latest extends Fragment {

    private HomeResponse homeResponse;
    private RecyclerView recyclerView;

    public static Latest newInstance(HomeResponse homeResponse) {
        Latest fragment = new Latest();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, homeResponse);
        fragment.setArguments(bundle);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getContext() != null;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new LatestRecycler(getContext(), homeResponse.subs));
    }
}