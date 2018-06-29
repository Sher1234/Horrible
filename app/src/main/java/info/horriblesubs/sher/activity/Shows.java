package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.fragment.ShowsFragment;
import info.horriblesubs.sher.model.response.ShowsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Shows extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ShowsTask task;
    private View progressBar;
    private ViewPager viewPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.viewPager);
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorText));
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        editText.setTextSize((float) 13.5);
        searchView.setOnQueryTextListener(this);
        task = new ShowsTask();
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView.getQuery() != null && !searchView.getQuery().toString().isEmpty())
            searchView.setQuery(null, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.notifications).setVisible(false).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new ShowsTask();
                task.execute();
                return true;

            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLoadData(@NotNull ShowsResponse showsResponse) {
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), showsResponse));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s == null || s.isEmpty() || s.length() < 2)
            return false;
        Intent intent = new Intent(this, Search.class);
        intent.putExtra(SearchManager.QUERY, s);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    class ShowsTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private ShowsResponse shows;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.requestFocus();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<ShowsResponse> call = api.getShows("0");
            call.enqueue(new Callback<ShowsResponse>() {
                @Override
                public void onResponse(@NonNull Call<ShowsResponse> call,
                                       @NonNull Response<ShowsResponse> response) {
                    if (response.body() != null)
                        shows = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ShowsResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    shows = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (i == 1) {
                if (shows == null)
                    Toast.makeText(Shows.this, "Invalid Subz...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(shows);
            } else
                Toast.makeText(Shows.this, "Server Error...", Toast.LENGTH_SHORT).show();
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final ShowsResponse showsResponse;

        PagerAdapter(FragmentManager fragmentManager, ShowsResponse showsResponse) {
            super(fragmentManager);
            this.showsResponse = showsResponse;
        }

        @Override
        public Fragment getItem(int position) {
            return ShowsFragment.newInstance(showsResponse, position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}