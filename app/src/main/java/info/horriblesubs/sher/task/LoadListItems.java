package info.horriblesubs.sher.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import info.horriblesubs.sher.activity.Home;
import info.horriblesubs.sher.adapter.ListRecycler;
import info.horriblesubs.sher.model.Item;

@SuppressLint("StaticFieldLeak")
public class LoadListItems extends AsyncTask<Intent, Void, List<Item>> {

    private Context context;
    private RecyclerView recyclerView;

    public LoadListItems(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Item> doInBackground(Intent... intents) {
        List<Item> items = new ArrayList<>();
        int i = intents[0].getIntExtra("size", 0);
        for (int j = 0; j < i; j++) {
            Item item = (Item) intents[0].getSerializableExtra("extra-" + j);
            if (item != null)
                items.add(item);
        }
        while (true)
            if (items.size() == i && Home.searchView != null)
                break;
        return items;
    }

    @Override
    protected void onPostExecute(final List<Item> items) {
        super.onPostExecute(items);
        final ListRecycler listRecycler = new ListRecycler(context, items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(listRecycler);
        if (Home.searchView != null)
            Home.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    List<Item> items1 = new ArrayList<>();
                    for (Item item : items) {
                        if (item.title.toLowerCase().contains(newText.toLowerCase()))
                            items1.add(item);
                    }
                    listRecycler.onQueryUpdate(items1);
                    return false;
                }
            });
    }
}