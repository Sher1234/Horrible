package info.horriblesubs.sher.old.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.base.Item;
import info.horriblesubs.sher.old.activity.Home;
import info.horriblesubs.sher.old.adapter.ListRecycler;
import info.horriblesubs.sher.old.task.FetchListItems;

public class List extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private String s;

    public static List newInstance(int i) {
        List fragment = new List();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        assert getArguments() != null;
        s = "?mode=list-current";
        if (getArguments().getInt(ARG_SECTION_NUMBER, 0) == 1)
            s = "?mode=list-all";
        new FetchListItems(getContext(), recyclerView, swipeRefreshLayout).execute(s);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchListItems(getContext(), recyclerView, swipeRefreshLayout).execute(s);
            }
        });
        if (Home.searchView != null)
            if (getArguments().getInt(ARG_SECTION_NUMBER, 0) == 0) {
                Home.searchView.setEnabled(false);
                Home.searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)
                        .setEnabled(false);
            } else {
                Home.searchView.setEnabled(true);
                Home.searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)
                        .setEnabled(true);
                Home.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    private java.util.List<Item> itemList = null;

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        java.util.List<Item> items = new ArrayList<>();
                        ListRecycler listRecycler = (ListRecycler) recyclerView.getAdapter();
                        if (itemList == null)
                            itemList = listRecycler.getItems();
                        for (Item item : itemList)
                            if (item.title.toLowerCase().contains(newText.toLowerCase()))
                                items.add(item);
                        listRecycler.onQueryUpdate(items);
                        return false;
                    }
                });
            }
        return rootView;
    }
}