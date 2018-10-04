package info.horriblesubs.sher.ui.show;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.common.Api;
import info.horriblesubs.sher.common.Change;
import info.horriblesubs.sher.model.base.ReleaseItem;
import info.horriblesubs.sher.model.response.ShowResponse;
import info.horriblesubs.sher.ui.show.fragment.Downloads;
import info.horriblesubs.sher.util.DialogX;
import info.horriblesubs.sher.util.FavDBFunctions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Show extends AppCompatActivity
        implements View.OnClickListener, Change, CompoundButton.OnCheckedChangeListener {

    private View view;
    private String link;
    private ShowTask task;
    private Toolbar toolbar;
    private DialogX progress;
    private CheckBox checkBox;
    private ImageView imageView;
    private FloatingActionButton fab;
    private AppBarLayout appBarLayout;
    private ShowResponse showResponse;
    private InterstitialAd interstitialAd;
    private RecyclerView recyclerView1, recyclerView2;
    private TextView textView, textView1, textView2, textView3, textView4, textViewA, textViewB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        link = getIntent().getStringExtra("link");
        if (link == null || link.isEmpty()) {
            Toast.makeText(this, "Error opening show...", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        onSetViews();
        onThemeChange(((AppController) getApplicationContext()).getAppTheme());
        onAdShow();
        fetchShow();
    }

    private void onAdShow() {
        FrameLayout layout = findViewById(R.id.adViewLayout);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_unit_footer_4));
        adView.setAdSize(AdSize.SMART_BANNER);
        layout.addView(adView);
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
                }, 650);
            }
        });
    }

    private void onSetViews() {
        fab = findViewById(R.id.fab);
        view = findViewById(R.id.view);
        toolbar = findViewById(R.id.toolbar);
        checkBox = new CheckBox(this);
        imageView = findViewById(R.id.imageView);
        textViewB = findViewById(R.id.textViewB);
        textViewA = findViewById(R.id.textViewA);
        textView4 = findViewById(R.id.textView4);
        textView3 = findViewById(R.id.textView3);
        textView2 = findViewById(R.id.textView2);
        textView1 = findViewById(R.id.textView1);
        textView = findViewById(R.id.textViewTitle);
        appBarLayout = findViewById(R.id.appBarLayout);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkBox.setOnCheckedChangeListener(this);
        fab.setOnClickListener(this);
    }

    private void fetchShow() {
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
                fetchShow();
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

    private void onDataUpdated(@NotNull ShowResponse showResponse) {
        this.showResponse = showResponse;
        ReleaseRecycler releaseRecycler1 = new ReleaseRecycler(this, showResponse.batches);
        checkBox.setChecked((FavDBFunctions.checkFavourite(this, showResponse.detail.id)));
        ReleaseRecycler releaseRecycler2 = new ReleaseRecycler(this, showResponse.subs);
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
                .setCustomAnimations(R.animator.fly_in, R.animator.fade_out,
                        R.animator.fade_in, R.animator.fly_out)
                .replace(R.id.frameLayout, Downloads.newInstance(item, showResponse.detail.image), "Download")
                .commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (showResponse == null || showResponse.detail == null)
            Toast.makeText(this, "Horrible error, try refreshing page...", Toast.LENGTH_SHORT).show();
        else {
            if (b) {
                fab.setImageResource(R.drawable.ic_favorite_filled);
                FavDBFunctions.addToFavourites(this, this.showResponse.detail);
            } else {
                fab.setImageResource(R.drawable.ic_favorite);
                FavDBFunctions.removeFromFavourites(this, showResponse.detail.id);
            }
        }
    }

    @ColorInt
    private int getResColor(@ColorRes int i) {
        return getResources().getColor(i);
    }

    @NotNull
    private ColorStateList getResColorList(@ColorRes int i) {
        return getResources().getColorStateList(i);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            Log.e("AZX", "Z" + checkBox.isChecked());
            checkBox.setChecked(!checkBox.isChecked());
            Log.e("AZX", "A" + checkBox.isChecked());
        }
    }

    @Override
    public void onThemeChange(boolean b) {
        if (b) {
            appBarLayout.setBackgroundColor(getResColor(R.color.primaryDark));
            fab.setBackgroundTintList(getResColorList(R.color.progress_dark));
            textView1.setTextColor(getResColor(R.color.textHeadingDark));
            textView2.setTextColor(getResColor(R.color.textHeadingDark));
            textView3.setTextColor(getResColor(R.color.textHeadingDark));
            textView4.setTextColor(getResColor(R.color.textHeadingDark));
            textViewA.setTextColor(getResColor(R.color.textHeadingDark));
            textViewB.setTextColor(getResColor(R.color.textHeadingDark));
            view.setBackgroundColor(getResColor(R.color.primaryDark));
            textView.setTextColor(getResColor(android.R.color.white));
            fab.setImageTintList(getResColorList(R.color.fab_dark));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    getWindow().setNavigationBarDividerColor(getResColor(R.color.primaryDark));
                getWindow().getDecorView().setSystemUiVisibility(0);
                getWindow().setStatusBarColor(getResColor(R.color.primaryDark));
                getWindow().setNavigationBarColor(getResColor(R.color.primaryDark));
            }
        } else {
            appBarLayout.setBackgroundColor(getResColor(android.R.color.white));
            fab.setBackgroundTintList(getResColorList(R.color.progress_light));
            textView1.setTextColor(getResColor(R.color.textHeadingLight));
            textView2.setTextColor(getResColor(R.color.textHeadingLight));
            textView3.setTextColor(getResColor(R.color.textHeadingLight));
            textView4.setTextColor(getResColor(R.color.textHeadingLight));
            textViewA.setTextColor(getResColor(R.color.textHeadingLight));
            textViewB.setTextColor(getResColor(R.color.textHeadingLight));
            view.setBackgroundColor(getResColor(android.R.color.white));
            textView.setTextColor(getResColor(android.R.color.black));
            fab.setImageTintList(getResColorList(R.color.fab_light));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    getWindow().setNavigationBarDividerColor(getResColor(R.color.colorPrimaryDarkLight));
                getWindow().getDecorView().setSystemUiVisibility(8208);
                getWindow().setStatusBarColor(getResColor(android.R.color.white));
                getWindow().setNavigationBarColor(getResColor(android.R.color.white));
            }
        }
        onChangeMenuIcon(b);
        if (recyclerView1.getAdapter() != null)
            ((Change) recyclerView1.getAdapter()).onThemeChange(b);
        if (recyclerView2.getAdapter() != null)
            ((Change) recyclerView2.getAdapter()).onThemeChange(b);
    }

    private void onChangeMenuIcon(boolean b) {
        Drawable drawable = toolbar.getNavigationIcon();
        assert drawable != null;
        if (b)
            DrawableCompat.setTint(drawable.mutate(), getResColor(android.R.color.white));
        else
            DrawableCompat.setTint(drawable.mutate(), getResColor(android.R.color.black));
        toolbar.setNavigationIcon(drawable);
    }

    class ShowTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private ShowResponse show;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new DialogX(Show.this, true);
            progress.show();
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
            progress.dismiss();
            if (i == 1) {
                if (show == null)
                    Snackbar.make(view, "Error loading subs, try again...", Snackbar.LENGTH_INDEFINITE).show();
                else
                    onDataUpdated(show);
            } else {
                if (i == 306)
                    Snackbar.make(view, "Network failure...", Snackbar.LENGTH_INDEFINITE).show();
                else if (i == 307)
                    Snackbar.make(view, "Request cancelled...", Snackbar.LENGTH_INDEFINITE).show();
                else
                    Snackbar.make(view, "Unknown error, try again...", Snackbar.LENGTH_INDEFINITE).show();
                finish();
            }
        }
    }
}