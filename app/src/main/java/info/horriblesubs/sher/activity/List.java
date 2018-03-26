package info.horriblesubs.sher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.task.FetchListItems;
import info.horriblesubs.sher.task.LoadListItems;

public class List extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Map<String, String> map;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Home.searchView = null;
        map = new HashMap<>();
        map.put("all", "list-all");
        map.put("current", "list-current");

        ImageView imageView = findViewById(R.id.imageView);
        Picasso.with(this).load("http://horriblesubs.info/images/b/ccs_banner.jpg")
                .into(imageView);

        Intent intent = getIntent();
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mode = intent.getStringExtra("mode");
        if (intent.getIntExtra("size", 0) == 0)
            new FetchListItems(List.this, recyclerView, swipeRefreshLayout)
                    .execute("?mode=" + map.get(mode));
        else
            new LoadListItems(this, recyclerView).execute(intent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchListItems(List.this, recyclerView, swipeRefreshLayout)
                        .execute("?mode=" + map.get(mode));
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Home.searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        menu.findItem(R.id.actionNotifications).setVisible(false);
        menu.findItem(R.id.actionNotifications).setCheckable(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        //Home.searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START) &&
                !Home.searchView.getQuery().toString().isEmpty()) {
            Home.searchView.setQuery("", false);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navHome:
                if (mode.equalsIgnoreCase("latest-all"))
                    break;
                intent = new Intent(this, Home.class);
                intent.putExtra("mode", "latest-all");
                startActivity(intent);
                finish();
                break;

            case R.id.navBatch:
                if (mode.equalsIgnoreCase("latest-batch"))
                    break;
                intent = new Intent(this, Home.class);
                intent.putExtra("mode", "latest-batch");
                startActivity(intent);
                finish();
                break;

            case R.id.navTodaySchedule:
                intent = new Intent(this, Schedule.class);
                intent.putExtra("mode", "today");
                startActivity(intent);
                finish();
                break;

            case R.id.navFullSchedule:
                intent = new Intent(this, Schedule.class);
                intent.putExtra("mode", "all");
                startActivity(intent);
                finish();
                break;

            case R.id.navCurrentShows:
                if (mode.equalsIgnoreCase("current"))
                    break;
                intent = new Intent(this, List.class);
                intent.putExtra("mode", "current");
                startActivity(intent);
                finish();
                break;

            case R.id.navAllShows:
                if (mode.equalsIgnoreCase("all"))
                    break;
                intent = new Intent(this, List.class);
                intent.putExtra("mode", "all");
                startActivity(intent);
                finish();
                break;

            case R.id.navRss:
                break;

            case R.id.navBrowser:
                break;

            case R.id.navFeedback:
                break;

            case R.id.navShare:
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}