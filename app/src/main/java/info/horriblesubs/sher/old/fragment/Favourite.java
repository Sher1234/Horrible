package info.horriblesubs.sher.old.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.old.adapter.ItemRecycler;

public class Favourite extends Fragment {

    @NotNull
    public static Favourite newInstance() {
        return new Favourite();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        final RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(getItemRecycler());
        assert getArguments() != null;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }

    @NonNull
    private ItemRecycler getItemRecycler() {
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("horriblesubs-bookmarks", Context.MODE_PRIVATE);
        Map<String, ?> map = sharedPreferences.getAll();
        Set<String> stringSet = map.keySet();
        List<PageItem> pageItems = new ArrayList<>();
        for (String s : stringSet) {
            PageItem pageItem = new Gson().fromJson((String) map.get(s), new TypeToken<PageItem>() {
            }.getType());
            pageItems.add(pageItem);
        }
        return new ItemRecycler(getContext(), pageItems);
    }
}