package info.horriblesubs.sher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.ScheduleItem;
import info.horriblesubs.sher.task.FetchScheduleItems;

public class Schedule extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static List<ScheduleItem> scheduleItems;
    public static String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "To be Scheduled"};
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.with(this).load("http://horriblesubs.info/images/b/ccs_banner.jpg")
                .into(imageView);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode == null)
            mode = "today";
        if (intent.getIntExtra("size", 0) == 0 || scheduleItems.size() == 0)
            new FetchScheduleItems(this, mode).execute("?mode=schedule");

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        if (mode.equals("all")) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            for (String s : DAYS)
                tabLayout.addTab(tabLayout.newTab().setText(s));
        } else if (mode.equals("today")) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.today)));
        }
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), mode);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.actionNotifications).setVisible(false);
        menu.findItem(R.id.actionNotifications).setCheckable(false);
        menu.findItem(R.id.actionSearch).setVisible(false);
        menu.findItem(R.id.actionSearch).setCheckable(false);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navHome:
                intent = new Intent(this, Home.class);
                intent.putExtra("mode", "latest-all");
                startActivity(intent);
                finish();
                break;

            case R.id.navBatch:
                intent = new Intent(this, Home.class);
                intent.putExtra("mode", "latest-batch");
                startActivity(intent);
                finish();
                break;

            case R.id.navTodaySchedule:
                if (mode.equalsIgnoreCase("today"))
                    break;
                intent = new Intent(this, Schedule.class);
                intent.putExtra("mode", "today");
                startActivity(intent);
                finish();
                break;

            case R.id.navFullSchedule:
                if (mode.equalsIgnoreCase("all"))
                    break;
                intent = new Intent(this, Schedule.class);
                intent.putExtra("mode", "all");
                startActivity(intent);
                finish();
                break;

            case R.id.navCurrentShows:
                intent = new Intent(this, info.horriblesubs.sher.activity.List.class);
                intent.putExtra("mode", "current");
                startActivity(intent);
                finish();
                break;

            case R.id.navAllShows:
                intent = new Intent(this, info.horriblesubs.sher.activity.List.class);
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

    class PagerAdapter extends FragmentPagerAdapter {

        private String MODE;

        PagerAdapter(FragmentManager fragmentManager, String MODE) {
            super(fragmentManager);
            this.MODE = MODE;
        }

        @Override
        public Fragment getItem(int position) {
            return info.horriblesubs.sher.fragment.Schedule
                    .newInstance(position + 1, this.MODE);
        }

        @Override
        public int getCount() {
            switch (this.MODE) {
                case "all":
                    return 8;
                case "today":
                    return 1;
                default:
                    return 0;
            }
        }
    }
}