package info.horriblesubs.sher.ui.c;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
import info.horriblesubs.sher.adapter.ListItemAdapter;
import info.horriblesubs.sher.adapter.ListItemAdapter.OnItemClick;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.a.Ads;
import info.horriblesubs.sher.ui.a.Menu;
import info.horriblesubs.sher.ui.a.SearchChange;
import info.horriblesubs.sher.ui.a.SearchChange.SearchListener;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;

import static info.horriblesubs.sher.AppMe.appMe;

public class Search extends AppCompatActivity implements TaskListener, OnItemClick,
        OnClickListener, Observer<List<ListItem>>, SearchListener {

    private SwipeRefreshLayout refreshLayout;
    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private LoadingDialog dialog;
    private Model model;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppMe.appMe.setTheme();
        setContentView(R.layout.c_activity_0);
        model = ViewModelProviders.of(this).get(Model.class);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        model.getItems().observe(this, this);
        findViewById(R.id.fab).setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        new SearchChange(this, this);
        textView = findViewById(R.id.textView);
        Ads.InterstitialAd.load(this);
        Ads.BannerAd.load(this);
        menu = Menu.all();
    }

    @Override
    public void onItemClicked(ListItem item) {
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
        refreshLayout.setRefreshing(true);
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
        String s = getIntent().getStringExtra("search");
        if (s != null && !s.isEmpty()) model.onSearch(this, s);
    }

    @Override
    public void onChanged(List<ListItem> items) {
        if (items == null || items.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(null);
        } else {
            recyclerView.setAdapter(ListItemAdapter.get(Search.this, items));
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab)
            menu.show(getSupportFragmentManager());
    }

    @Override
    public void onTermChange(String s) {
        if (recyclerView.getAdapter() instanceof ListItemAdapter)
            ((ListItemAdapter) recyclerView.getAdapter()).onSearch(s);
    }

    @Override
    public void onSearch(String s) {
        if (s == null || s.length() < 2) {
            Toast.makeText(this, "Invalid Search Term", Toast.LENGTH_SHORT).show();
            return;
        }
        model.onSearch(this, s);
    }
}