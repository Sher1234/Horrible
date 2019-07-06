package info.horriblesubs.sher.ui.d;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.FavouriteAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.DataMethods;
import info.horriblesubs.sher.ui.a.Ads;
import info.horriblesubs.sher.ui.a.Menu;
import info.horriblesubs.sher.ui.a.Menu.Delete;
import info.horriblesubs.sher.ui.a.SearchChange;
import info.horriblesubs.sher.ui.a.SearchChange.SearchListener;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static info.horriblesubs.sher.AppMe.appMe;
import static info.horriblesubs.sher.adapter.FavouriteAdapter.OnItemClick;
import static info.horriblesubs.sher.adapter.FavouriteAdapter.get;

public class Favourites extends AppCompatActivity implements TaskListener, SearchListener,
        OnItemClick, Observer<List<ShowDetail>>, OnClickListener, Delete, SwipeRefreshLayout.OnRefreshListener {

    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;
    private LoadingDialog dialog;
    private Model model;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppMe.appMe.setTheme();
        setContentView(R.layout.d_activity_0);
        model = ViewModelProviders.of(this).get(Model.class);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.swipeRefreshLayout);
        model.items(this, this).observe(this, this);
        findViewById(R.id.fab).setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        new SearchChange(this, this);
        refreshLayout.setOnRefreshListener(this);
        textView = findViewById(R.id.textView);
        Ads.InterstitialAd.load(this);
        Ads.BannerAd.load(this);
        menu = Menu.favs(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new GridLayoutManager(this, appMe.isPortrait()?2:4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        textView.setText(R.string.empty_favourites);
        model.onRefresh();
    }

    @Deprecated
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            adapter.delete(adapter.isDeleteDisabled());
            return true;
        }
        return false;
    }

    @Override
    public void onChanged(List<ShowDetail> items) {
        if (items == null || items.size() == 0 || model.isEmpty(Favourites.this)) {
            recyclerView.setVisibility(GONE);
            textView.setVisibility(VISIBLE);
            adapter = null;
        } else {
            recyclerView.setVisibility(VISIBLE);
            if (adapter == null) {
                adapter = get(this, items);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.onUpdateFavourites(items);
                adapter.notifyDataSetChanged();
            }
            textView.setVisibility(GONE);
        }
    }

    @Override
    public void onDeleteClicked(ShowDetail item) {
        if (adapter.isDeleteDisabled() || item.sid == null) return;
        new DataMethods(this).onDeleteFavourite(item.sid);
        model.onRefresh();
    }

    @Override
    public void onItemClicked(ShowDetail item) {
        String link = item.link;
        if (link == null) return;
        if (link.contains("/shows/")) {
            String[] arr = link.split("/shows/");
            link = arr[arr.length - 1];
        }
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", link);
        startActivity(intent);
    }

    public void onPostExecute() {
        if (dialog != null) dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onPreExecute() {
        if (dialog == null)
            dialog = new LoadingDialog(this);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.onStopTask();
        onPostExecute();
        dialog = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab)
            menu.show(getSupportFragmentManager());
    }

    @Override
    public void onTermChange(String s) {
        if (adapter != null)
            adapter.onSearch(s);
    }

    @Override
    public void onSearch(String s) {
        if (adapter != null)
            adapter.onSearch(s);
    }

    @Override
    public void onDelete() {
        adapter.delete(adapter.isDeleteDisabled());
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }
}