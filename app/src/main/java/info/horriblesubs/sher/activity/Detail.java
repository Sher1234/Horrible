package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.google.gson.Gson;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.fragment.Details;
import info.horriblesubs.sher.fragment.ShowReleases;
import info.horriblesubs.sher.model.PageItem;
import info.horriblesubs.sher.task.FetchPageItem;
import info.horriblesubs.sher.task.FetchScheduleItems;
import info.horriblesubs.sher.task.LoadScheduleItems;

@SuppressLint("StaticFieldLeak")
public class Detail extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static PageItem pageItem = null;
    private ImageView imageView;
    private String link = null;

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
        Intent intent = getIntent();
        if (intent.getStringExtra("link") != null)
            link = intent.getStringExtra("link");
        else {
            Toast.makeText(this, "Error Loading Result...", Toast.LENGTH_SHORT).show();
            finish();
        }
        new FetchPageItem().execute("?mode=show-detail&link=" + link);
        new OnLoad().execute();

        Home.searchView = findViewById(R.id.searchView);
        SearchView searchView = Home.searchView;
        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize((float) 14.5);
        editText.setEnabled(false);
        searchView.setQueryHint(getResources().getString(R.string.shows));

        findViewById(R.id.imageViewDrawer).setOnClickListener(this);
        imageView = findViewById(R.id.imageViewNotification);
        imageView.setOnClickListener(this);

        invalidateBookmark();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setVisibility(View.GONE);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Episodes"));
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
                intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navSchedule:
                intent = new Intent(this, Schedule.class);
                startActivity(intent);
                finish();
                break;

            case R.id.navShows:
                break;

            case R.id.navRss:
                break;

            case R.id.navBrowser:
                break;

            case R.id.navFeedback:
                break;

            case R.id.navAbout:
                intent = new Intent(this, About.class);
                startActivity(intent);
                break;

            case R.id.navFav:
                intent = new Intent(this, Favourite.class);
                startActivity(intent);
                finish();
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
                SharedPreferences sharedPreferences = this.getSharedPreferences("horriblesubs-bookmarks", Context.MODE_PRIVATE);
                if (Detail.pageItem == null)
                    return;
                String string = sharedPreferences.getString(Detail.pageItem.id, null);
                if (string != null)
                    removeBookmark();
                else
                    addBookmark();
                break;
        }
    }

    private void invalidateBookmark() {
        SharedPreferences sharedPreferences = this
                .getSharedPreferences("horriblesubs-bookmarks", Context.MODE_PRIVATE);
        imageView.setImageResource(R.drawable.ic_bookmark_off);
        if (Detail.pageItem == null)
            return;
        String string = sharedPreferences.getString(Detail.pageItem.id, null);
        if (string != null) {
            imageView.setContentDescription("Remove Bookmark");
            imageView.setImageResource(R.drawable.ic_bookmark_on);
        } else {
            imageView.setContentDescription("Add Bookmark");
            imageView.setImageResource(R.drawable.ic_bookmark_off);
        }
    }

    private void addBookmark() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("horriblesubs-bookmarks", Context.MODE_PRIVATE);
        if (Detail.pageItem != null) {
            Gson gson = new Gson();
            String s = gson.toJson(Detail.pageItem.getPageItem());
            sharedPreferences.edit().putString(Detail.pageItem.id, s).apply();
        }
        invalidateBookmark();
    }

    private void removeBookmark() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("horriblesubs-bookmarks", Context.MODE_PRIVATE);
        if (Detail.pageItem != null)
            sharedPreferences.edit().remove(Detail.pageItem.id).apply();
        invalidateBookmark();
    }

    class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Details.newInstance();
                case 1:
                    return ShowReleases.newInstance(position);
                case 2:
                    return ShowReleases.newInstance(position);
                default:
                    return Details.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Detail.pageItem = null;
    }

    class OnLoad extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (true)
                if (Detail.pageItem != null)
                    break;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            invalidateBookmark();
        }
    }
}