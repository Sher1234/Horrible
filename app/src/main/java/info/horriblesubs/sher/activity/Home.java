package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.receiver.Notification;
import info.horriblesubs.sher.task.FetchScheduleItems;
import info.horriblesubs.sher.task.LoadScheduleItems;
import info.horriblesubs.sher.util.DialogX;

@SuppressLint("StaticFieldLeak")
public class Home extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    public static SearchView searchView = null;
    private ImageView imageView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView textView = findViewById(R.id.textView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior
                .from(findViewById(R.id.bottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    textView.setCompoundDrawablesWithIntrinsicBounds(null,
                            getResources().getDrawable(R.drawable.ic_up), null, null);
                else
                    textView.setCompoundDrawablesWithIntrinsicBounds(null,
                            getResources().getDrawable(R.drawable.ic_down), null, null);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        new FetchScheduleItems().execute("?mode=schedule");
        new LoadScheduleItems(recyclerView, this, null, 1).execute();

        Home.searchView = findViewById(R.id.searchView);
        SearchView searchView = Home.searchView;
        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize((float) 14.5);
        searchView.setQueryHint(getResources().getString(R.string.app_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(Home.this, Search.class);
                intent.putExtra(Search.SEARCH_HS, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        findViewById(R.id.imageViewDrawer).setOnClickListener(this);
        this.imageView = findViewById(R.id.imageViewNotification);
        this.imageView.setOnClickListener(this);

        invalidateNotificationItem();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Batches"));
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
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
                break;

            case R.id.navSchedule:
                intent = new Intent(this, Schedule.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navShows:
                intent = new Intent(this, List.class);
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

            case R.id.navAbout:
                intent = new Intent(this, About.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewDrawer:
                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.imageViewNotification:
                SharedPreferences sharedPreferences = getSharedPreferences("horriblesubs-prefs",
                        Context.MODE_PRIVATE);
                boolean b = sharedPreferences.getBoolean("notification-on", false);
                final DialogX dialogX = new DialogX(this);
                if (b) {
                    dialogX.setTitle("Disable Notifications")
                            .setDescription("This will disable new release notifications, You will not be able receive notifications on any new release.");
                    dialogX.positiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeNotificationAlert();
                            dialogX.dismiss();
                        }
                    });
                } else {
                    dialogX.setTitle("Enable Notifications")
                            .setDescription("This will enable new release notifications, You will receive notifications on every new release.");
                    dialogX.positiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setNotificationAlert();
                            dialogX.dismiss();
                        }
                    });
                }
                dialogX.negativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogX.dismiss();
                    }
                });
                dialogX.show();
                break;
        }
    }

    private void invalidateNotificationItem() {
        SharedPreferences sharedPreferences = this
                .getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("notification-on", false);
        if (b) {
            imageView.setContentDescription("Disable Notifications");
            imageView.setImageResource(R.drawable.ic_notifications_on);
        } else {
            imageView.setContentDescription("Enable Notifications");
            imageView.setImageResource(R.drawable.ic_notifications_off);
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
        invalidateNotificationItem();
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
                AlarmManager.INTERVAL_HOUR, pendingIntent);
        invalidateNotificationItem();
    }

    class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return info.horriblesubs.sher.fragment.Home.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}