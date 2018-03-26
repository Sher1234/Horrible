package info.horriblesubs.sher.activity.beta;

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
import info.horriblesubs.sher.activity.List;
import info.horriblesubs.sher.model.ScheduleItem;
import info.horriblesubs.sher.receiver.Notification;
import info.horriblesubs.sher.task.FetchScheduleItems;
import info.horriblesubs.sher.task.LoadScheduleItemsX;
import info.horriblesubs.sher.util.DialogX;

@SuppressLint("StaticFieldLeak")
public class Schedule extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static java.util.List<ScheduleItem> scheduleItems;
    public static String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "To be Scheduled"};
    private ImageView imageView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView textView = findViewById(R.id.textView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
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
        new FetchScheduleItems(this, "all").execute("?mode=schedule");
        new LoadScheduleItemsX(recyclerView, this, null, 1).execute();

        info.horriblesubs.sher.activity.Home.searchView = findViewById(R.id.searchView);
        SearchView searchView = info.horriblesubs.sher.activity.Home.searchView;
        searchView.setQueryHint(getResources().getString(R.string.schedule));
        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize((float) 14.5);

        findViewById(R.id.imageViewDrawer).setOnClickListener(this);
        this.imageView = findViewById(R.id.imageViewNotification);
        this.imageView.setOnClickListener(this);
        this.imageView.setVisibility(View.GONE);

        invalidateNotificationItem();

        new FetchScheduleItems(this, "all").execute("?mode=schedule");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (String s : DAYS)
            tabLayout.addTab(tabLayout.newTab().setText(s));
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START) &&
                !info.horriblesubs.sher.activity.Home.searchView.getQuery().toString().isEmpty()) {
            info.horriblesubs.sher.activity.Home.searchView.setQuery("", false);
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
                intent = new Intent(this, info.horriblesubs.sher.activity.beta.Home.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navSchedule:
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
        Intent intent = new Intent(Schedule.this, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(Schedule.this, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        invalidateOptionsMenu();
    }

    private void setNotificationAlert() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notification-on", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Intent intent = new Intent(Schedule.this, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(Schedule.this, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        invalidateOptionsMenu();
    }

    class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return info.horriblesubs.sher.fragment.beta.Schedule
                    .newInstance(position + 2);
        }

        @Override
        public int getCount() {
            return 8;
        }
    }
}