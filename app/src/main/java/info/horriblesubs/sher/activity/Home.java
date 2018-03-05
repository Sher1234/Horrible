package info.horriblesubs.sher.activity;

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

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mode;
    private Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.getSharedPreferences("horriblesubs", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("alarmSet", false);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSearch:
                return true;

            case R.id.actionNotifications:
                this.getSharedPreferences("horriblesubs", Context.MODE_PRIVATE);
                SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
                boolean b = sharedPreferences.getBoolean("alarmSet", false);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (b) {
                    builder.setTitle("Disable Notifications");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAlarm();
                        }
                    });
                } else {
                    builder.setTitle("Enable Notifications");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setAlarm();
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
                break;

            case R.id.navAllShows:
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void deleteAlarm() {
        this.getSharedPreferences("horriblesubs", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("alarmSet", true).apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        Intent intent = new Intent(Home.this, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(Home.this, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        invalidateOptionsMenu();
    }

    private void setAlarm() {
        this.getSharedPreferences("horriblesubs", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("alarmSet", true).apply();
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
