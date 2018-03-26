package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.receiver.Notification;
import info.horriblesubs.sher.task.FetchReleaseItems;
import info.horriblesubs.sher.task.LoadReleaseItems;

@SuppressLint("StaticFieldLeak")
public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mode;
    private Map<String, String> map;
    public static SearchView searchView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        map = new HashMap<>();
        map.put("latest-all", "latest");
        map.put("latest-batch", "latest-batch");

        ImageView imageView = findViewById(R.id.imageView);
        Picasso.with(this).load("http://horriblesubs.info/images/b/ccs_banner.jpg")
                .into(imageView);

        Intent intent = getIntent();
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        invalidateOptionsMenu();
        mode = intent.getStringExtra("mode");
        if (intent.getIntExtra("size", 0) == 0)
            new FetchReleaseItems(Home.this, recyclerView, swipeRefreshLayout)
                    .execute("?mode=" + map.get(mode));
        else
            new LoadReleaseItems(this, recyclerView).execute(intent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchReleaseItems(Home.this, recyclerView, swipeRefreshLayout)
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
                intent = new Intent(this, List.class);
                intent.putExtra("mode", "current");
                startActivity(intent);
                finish();
                break;

            case R.id.navAllShows:
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = this
                .getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("notification-on", false);
        searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        if (b) {
            menu.findItem(R.id.actionNotifications).setTitle("Disable Notifications");
            menu.findItem(R.id.actionNotifications).setIcon(R.drawable.ic_notifications_on);
        } else {
            menu.findItem(R.id.actionNotifications).setTitle("Enable Notifications");
            menu.findItem(R.id.actionNotifications).setIcon(R.drawable.ic_notifications_off);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionNotifications:
                SharedPreferences sharedPreferences = this
                        .getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
                boolean b = sharedPreferences.getBoolean("notification-on", false);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (b) {
                    builder.setTitle("Disable Notifications");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeNotificationAlert();
                        }
                    });
                } else {
                    builder.setTitle("Enable Notifications");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotificationAlert();
                        }
                    });
                }
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeNotificationAlert() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notification-on", false).apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        Intent intent = new Intent(Home.this, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(Home.this, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        invalidateOptionsMenu();
    }

    private void setNotificationAlert() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notification-on", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Intent intent = new Intent(Home.this, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(Home.this, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        invalidateOptionsMenu();
    }
}