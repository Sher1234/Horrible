package info.horriblesubs.sher.ui.h;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ShowsAdapter;
import info.horriblesubs.sher.adapter.ShowsAdapter.OnItemClick;
import info.horriblesubs.sher.api.horrible.model.Item;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.a.Ads;
import info.horriblesubs.sher.ui.a.Menu;
import info.horriblesubs.sher.ui.a.SearchChange;
import info.horriblesubs.sher.ui.a.SearchChange.SearchListener;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;

import static info.horriblesubs.sher.AppMe.appMe;

public class Current extends AppCompatActivity implements TaskListener, OnItemClick, SearchListener,
        View.OnClickListener, Observer<List<Item>>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private LoadingDialog dialog;
    private ShowsAdapter adapter;
    private Model model;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppMe.appMe.setTheme();
        setContentView(R.layout.c_activity_0);
        model = new ViewModelProvider(this).get(Model.class);
        model.getItems(this).observe(this, this);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        findViewById(R.id.fab).setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        new SearchChange(this, this);
        refreshLayout.setOnRefreshListener(this);
        textView = findViewById(R.id.textView);
        Ads.InterstitialAd.load(this);
        String s = "No shows available.";
        Ads.BannerAd.load(this);
        textView.setText(s);
        menu = Menu.all();
        if (model.result.getValue() == null) model.onRefresh(false);
    }

    @Override
    public void onItemClicked(Item item) {
        if (item.link == null) return;
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    @Override
    public void onPostExecute() {
        refreshLayout.setRefreshing(false);
        if (dialog != null)
            dialog.dismiss();
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
    protected void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new GridLayoutManager(this, appMe.isPortrait()?1:2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Deprecated
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(true);
                return true;
            case R.id.browser:
                try {
                    String link = getResources().getString(R.string.horrible_subs_url) + "shows/";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, getResources().getString(R.string.app_not_available), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onChanged(List<Item> items) {
        if (items == null || items.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (recyclerView.getAdapter() == null) {
                adapter = ShowsAdapter.get(this, items);
                recyclerView.setAdapter(adapter);
            }
            else adapter.onDataUpdated(items);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTermChange(String s) {
        if (recyclerView.getAdapter() instanceof ShowsAdapter)
            ((ShowsAdapter) recyclerView.getAdapter()).onSearch(s);
    }

    @Override
    public void onSearch(String s) {
        if (recyclerView.getAdapter() instanceof ShowsAdapter)
            ((ShowsAdapter) recyclerView.getAdapter()).onSearch(s);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab)
            menu.show(getSupportFragmentManager());
    }

    @Override
    public void onRefresh() {
        model.onRefresh(true);
    }
}