package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import info.horriblesubs.sher.fragment.ShowsF;
import info.horriblesubs.sher.model.response.ShowsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Shows extends AppCompatActivity {

    private ShowsTask task;
    private View progressBar;
    private ViewPager viewPager;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppController.isDark)
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        super.onCreate(savedInstanceState);
        if (AppController.isDark)
            setContentView(R.layout.dark_a_shows);
        else
            setContentView(R.layout.activity_shows);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Shows.this, Search.class));
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(adRequest);

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
        task = new ShowsTask();
        task.execute();
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
        task = new ShowsTask();
        task.execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.notifications).setVisible(false).setEnabled(false);
        menu.findItem(R.id.theme).setVisible(false).setEnabled(false);
        menu.findItem(R.id.shows).setVisible(false).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                startTask();
                return true;

            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.schedule:
                startActivity(new Intent(this, Schedule.class));
                finish();
                return true;

            case R.id.browser:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://horriblesubs.info/shows/"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Error downloading...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onLoadData(@NotNull ShowsResponse showsResponse) {
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), showsResponse));
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
                    Toast.makeText(Shows.this, "Invalid subz...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(shows);
            } else
                Toast.makeText(Shows.this, "Server error...", Toast.LENGTH_SHORT).show();
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
            return ShowsF.newInstance(showsResponse, position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}