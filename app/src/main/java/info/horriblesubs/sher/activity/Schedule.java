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

import java.util.Calendar;

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
import info.horriblesubs.sher.fragment.ScheduleFragment;
import info.horriblesubs.sher.model.response.ScheduleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Schedule extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private final String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "To be Scheduled"};
    private ScheduleTask task;
    private View progressBar;
    private ViewPager viewPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.viewPager);
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (String s : DAYS)
            tabLayout.addTab(tabLayout.newTab().setText(s));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorText));
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        editText.setTextSize((float) 13.5);
        searchView.setOnQueryTextListener(this);
        task = new ScheduleTask();
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
                task = new ScheduleTask();
                task.execute();
                return true;

            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLoadData(@NotNull ScheduleResponse scheduleResponse) {
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), scheduleResponse));
        viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
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

    class ScheduleTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private ScheduleResponse schedule;

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
            Call<ScheduleResponse> call = api.getSchedule();
            call.enqueue(new Callback<ScheduleResponse>() {
                @Override
                public void onResponse(@NonNull Call<ScheduleResponse> call,
                                       @NonNull Response<ScheduleResponse> response) {
                    if (response.body() != null)
                        schedule = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ScheduleResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    schedule = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (i == 1) {
                if (schedule == null)
                    Toast.makeText(Schedule.this, "Invalid Subz...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(schedule);
            } else
                Toast.makeText(Schedule.this, "Server Error...", Toast.LENGTH_SHORT).show();
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final ScheduleResponse scheduleResponse;

        PagerAdapter(FragmentManager fragmentManager, ScheduleResponse scheduleResponse) {
            super(fragmentManager);
            this.scheduleResponse = scheduleResponse;
        }

        @Override
        public Fragment getItem(int position) {
            return ScheduleFragment.newInstance(scheduleResponse, position);
        }

        @Override
        public int getCount() {
            return 8;
        }
    }
}