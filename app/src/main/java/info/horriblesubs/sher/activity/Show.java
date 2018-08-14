package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.fragment.Downloads;
import info.horriblesubs.sher.model.base.ReleaseItem;
import info.horriblesubs.sher.model.response.ShowResponse;
import info.horriblesubs.sher.util.FavDBFunctions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Show extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Chip chip;
    private String link;
    private ShowTask task;
    private View progressBar;
    private ImageView imageView;
    private ShowResponse showResponse;
    private InterstitialAd interstitialAd;
    private RecyclerView recyclerView1, recyclerView2;
    private TextView textView1, textView2, textView3, textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppController.isDark)
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        super.onCreate(savedInstanceState);
        if (AppController.isDark)
            setContentView(R.layout.dark_a_show);
        else
            setContentView(R.layout.activity_show);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Show.this, Search.class));
            }
        });
        link = getIntent().getStringExtra("link");
        if (link == null || link.isEmpty()) {
            finish();
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(adRequest);

        int i = new Random().nextInt(100);
        interstitialAd = new InterstitialAd(this);
        if (i < 34)
            interstitialAd.setAdUnitId(getString(R.string.ad_unit_interstitial_1));
        else if (i < 68)
            interstitialAd.setAdUnitId(getString(R.string.ad_unit_interstitial_2));
        else if (i < 101)
            interstitialAd.setAdUnitId(getString(R.string.ad_unit_interstitial_3));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int e) {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interstitialAd.show();
                    }
                }, 1200);
            }
        });

        chip = findViewById(R.id.chip);
        chip.setOnCheckedChangeListener(this);
        imageView = findViewById(R.id.imageView);
        textView4 = findViewById(R.id.textView4);
        textView3 = findViewById(R.id.textView3);
        textView2 = findViewById(R.id.textView2);
        textView1 = findViewById(R.id.textView1);
        progressBar = findViewById(R.id.progressBar);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        startTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.notifications).setVisible(false).setEnabled(false);
        menu.findItem(R.id.schedule).setVisible(false).setEnabled(false);
        menu.findItem(R.id.theme).setVisible(false).setEnabled(false);
        menu.findItem(R.id.shows).setVisible(false).setEnabled(false);
        menu.findItem(R.id.about).setVisible(false).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void startTask() {
        if (task != null)
            task.cancel(true);
        task = null;
        task = new ShowTask();
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                startTask();
                return true;

            case R.id.browser:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(Html.fromHtml(showResponse.detail.link).toString()));
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

    private void onLoadData(@NotNull ShowResponse showResponse) {
        this.showResponse = showResponse;
        ReleaseRecycler releaseRecycler1 = new ReleaseRecycler(this, showResponse.batches);
        ReleaseRecycler releaseRecycler2 = new ReleaseRecycler(this, showResponse.subs);
        chip.setChecked((FavDBFunctions.checkFavourite(this, showResponse.detail.id)));
        Glide.with(this).load(showResponse.detail.image).into(imageView);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        textView1.setText(Html.fromHtml(showResponse.detail.title));
        textView2.setText(Html.fromHtml(showResponse.detail.body));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView1.setAdapter(releaseRecycler1);
        recyclerView2.setAdapter(releaseRecycler2);
        if (showResponse.batches != null && showResponse.batches.size() != 0)
            textView3.setVisibility(View.GONE);
        if (showResponse.subs != null && showResponse.subs.size() != 0)
            textView4.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("Download");
        if (fragment != null)
            fm.beginTransaction().remove(fragment).commit();
        else
            super.onBackPressed();
    }

    public void viewDownloadFragment(@NotNull ReleaseItem item) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.bottom_enter, R.anim.bottom_exit,
                        R.anim.bottom_enter, R.anim.bottom_exit)
                .replace(R.id.frameLayout, Downloads.newInstance(item, showResponse.detail.image), "Download")
                .commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (showResponse == null || showResponse.detail == null)
            Toast.makeText(this, "Horrible error, try refreshing page...", Toast.LENGTH_SHORT).show();
        else {
            if (b) {
                chip.setText(R.string.favourited);
                FavDBFunctions.addToFavourites(this, this.showResponse.detail);
            } else {
                chip.setText(R.string.add_to_favourites);
                FavDBFunctions.removeFromFavourites(this, showResponse.detail.id);
            }
        }
    }

    class ShowTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private ShowResponse show;

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
            Call<ShowResponse> call = api.getShow(link);
            call.enqueue(new Callback<ShowResponse>() {
                @Override
                public void onResponse(@NonNull Call<ShowResponse> call,
                                       @NonNull Response<ShowResponse> response) {
                    if (response.body() != null)
                        show = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ShowResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = 306;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = 307;
                    show = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (i == 1) {
                if (show == null)
                    Toast.makeText(Show.this, "Error loading subs, try again...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(show);
            } else {
                if (i == 306)
                    Toast.makeText(Show.this, "Network failure...", Toast.LENGTH_SHORT).show();
                else if (i == 307)
                    Toast.makeText(Show.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Show.this, "Unknown error, try again...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}