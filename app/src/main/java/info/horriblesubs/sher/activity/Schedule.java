package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
public class Schedule extends AppCompatActivity {

    private AdView adView;
    private View progressBar;
    private ScheduleTask task;
    private ViewPager viewPager;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Schedule.this, Search.class));
            }
        });

        adView = findViewById(R.id.adView);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_interstitial_2));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int e) {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });

        viewPager = findViewById(R.id.viewPager);
        progressBar = findViewById(R.id.progressBar);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        startTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void startTask() {
        if (task != null)
            task.cancel(true);
        task = null;
        task = new ScheduleTask();
        task.execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.notifications).setVisible(false).setEnabled(false);
        menu.findItem(R.id.schedule).setVisible(false).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shows:
                startActivity(new Intent(this, Shows.class));
                finish();
                return true;

            case R.id.refresh:
                startTask();
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
                    i = 306;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = 307;
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
                    Toast.makeText(Schedule.this, "Invalid subs...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(schedule);
            } else if (i == 306)
                Toast.makeText(Schedule.this, "Network Failure...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(Schedule.this, "Request Cancelled...", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Schedule.this, "Unknown error, try again...", Toast.LENGTH_SHORT).show();
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