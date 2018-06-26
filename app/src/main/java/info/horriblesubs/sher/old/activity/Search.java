package info.horriblesubs.sher.old.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

@SuppressLint("StaticFieldLeak")
public class Search extends AppCompatActivity {
/*
    public static final String SEARCH_HS = "HSS_PARAM";
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView searchView = findViewById(R.id.searchView);
        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize((float) 14.5);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        String searchQuery = null;
        Intent intent = getIntent();
        if (intent != null)
            searchQuery = intent.getStringExtra(SEARCH_HS);
        searchView.setQueryHint(getResources().getString(R.string.app_name));
        if (searchQuery != null) {
            searchView.setQuery(searchQuery, true);
            new FetchReleaseItems(this, recyclerView, swipeRefreshLayout)
                    .execute("?mode=search&search=" + searchQuery);
        }

        new FetchReleaseItems(this, recyclerView, swipeRefreshLayout);

        findViewById(R.id.imageViewDrawer).setOnClickListener(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        new FetchReleaseItems(this, recyclerView, swipeRefreshLayout)
                .execute("?mode=search&search=" + query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
*/
}