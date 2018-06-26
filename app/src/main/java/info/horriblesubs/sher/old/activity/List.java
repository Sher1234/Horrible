package info.horriblesubs.sher.old.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

@SuppressLint("StaticFieldLeak")
public class List extends AppCompatActivity {
/*
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_x);
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
        searchView.setQueryHint(getResources().getString(R.string.shows));

        findViewById(R.id.imageViewDrawer).setOnClickListener(this);
        findViewById(R.id.imageViewNotification).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageViewNotification).setEnabled(false);
        findViewById(R.id.imageViewNotification).setClickable(false);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Current Shows"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));
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

            case R.id.navFav:
                intent = new Intent(this, Favourite.class);
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
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return info.horriblesubs.sher.old.fragment.List.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
*/
}