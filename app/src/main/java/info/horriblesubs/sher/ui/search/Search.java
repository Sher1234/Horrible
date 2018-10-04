package info.horriblesubs.sher.ui.search;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.LatestRecycler;
import info.horriblesubs.sher.common.Api;
import info.horriblesubs.sher.common.Change;
import info.horriblesubs.sher.model.response.SearchResponse;
import info.horriblesubs.sher.util.DialogX;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Search extends AppCompatActivity
        implements TextView.OnEditorActionListener, View.OnClickListener, Change {

    private InterstitialAd interstitialAd;

    private AppCompatImageButton button;
    private RecyclerView recyclerView;
    private AppBarLayout appBarLayout;
    private TextView textView;
    private EditText editText;
    private DialogX dialogX;
    private Toolbar toolbar;
    private SearchTask task;
    private View view;

    private String search;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e);

        view = findViewById(R.id.view);
        button = findViewById(R.id.button);
        toolbar = findViewById(R.id.toolbar);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textViewTitle);
        recyclerView = findViewById(R.id.recyclerView);
        appBarLayout = findViewById(R.id.appBarLayout);

        button.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onThemeChange(((AppController) getApplication()).getAppTheme());

        onAdShow();
    }

    private void onLoadData(@NotNull SearchResponse response) {
        recyclerView.setAdapter(new LatestRecycler(this, response.search));
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

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_GO) {
            startSearch();
            return true;
        }
        return false;
    }

    private void startSearch() {
        if (editText.getText() == null || editText.getText().toString().length() < 2) {
            Toast.makeText(this, "Invalid search query...", Toast.LENGTH_SHORT).show();
            return;
        }
        search = editText.getText().toString();
        if (task != null)
            task.cancel(true);
        task = null;
        task = new SearchTask();
        task.execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView)
            startSearch();
    }

    @Override
    public void onThemeChange(boolean b) {
        if (b) {
            appBarLayout.setBackgroundColor(getResColor(R.color.primaryDark));
            editText.setHintTextColor(getResColor(R.color.textDisabledDark));
            button.setImageTintList(getColorList(R.color.progress_dark));
            editText.setTextColor(getResColor(R.color.textHeadingDark));
            view.setBackgroundColor(getResColor(R.color.primaryDark));
            textView.setTextColor(getResColor(android.R.color.white));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    getWindow().setNavigationBarDividerColor(getColor(R.color.primaryDark));
                getWindow().getDecorView().setSystemUiVisibility(0);
                getWindow().setStatusBarColor(getResColor(R.color.primaryDark));
                getWindow().setNavigationBarColor(getResColor(R.color.primaryDark));
            }
        } else {
            appBarLayout.setBackgroundColor(getResColor(android.R.color.white));
            editText.setHintTextColor(getResColor(R.color.textDisabledLight));
            button.setImageTintList(getColorList(R.color.progress_light));
            editText.setTextColor(getResColor(R.color.textHeadingLight));
            view.setBackgroundColor(getResColor(android.R.color.white));
            textView.setTextColor(getResColor(R.color.primaryDark));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    getWindow().setNavigationBarDividerColor(getColor(R.color.colorPrimaryDarkLight));
                getWindow().getDecorView().setSystemUiVisibility(8208);
                getWindow().setStatusBarColor(getResColor(android.R.color.white));
                getWindow().setNavigationBarColor(getResColor(android.R.color.white));
            }
        }
        onChangeMenuIcon(b);
        if (recyclerView.getAdapter() != null)
            ((Change) recyclerView.getAdapter()).onThemeChange(b);
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

    private int getResColor(@ColorRes int r) {
        return getResources().getColor(r);
    }

    @NotNull
    private ColorStateList getColorList(@ColorRes int r) {
        return getResources().getColorStateList(r);
    }

    class SearchTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private SearchResponse searchResponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogX = new DialogX(Search.this, true);
            dialogX.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<SearchResponse> call = api.getSearch(search);
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<SearchResponse> call,
                                       @NonNull Response<SearchResponse> response) {
                    if (response.body() != null)
                        searchResponse = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = 306;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = 307;
                    searchResponse = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialogX.dismiss();
            if (i == 1) {
                if (searchResponse == null)
                    Toast.makeText(Search.this, "Invalid subs...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(searchResponse);
            } else {
                if (i == 306)
                    Toast.makeText(Search.this, "Network Failure...", Toast.LENGTH_SHORT).show();
                else if (i == 307)
                    Toast.makeText(Search.this, "Request Cancelled...", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Search.this, "Unknown error, try again...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}