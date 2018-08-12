package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.LatestRecycler;
import info.horriblesubs.sher.model.response.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Search extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    private String search;
    private SearchTask task;
    private View progressBar;
    private EditText editText;
    private RecyclerView recyclerView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_interstitial_3));
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

        editText = findViewById(R.id.editText);
        editText.setOnEditorActionListener(this);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        findViewById(R.id.imageView).setOnClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onLoadData(@NotNull SearchResponse response) {
        recyclerView.setAdapter(new LatestRecycler(this, response.search));
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

    class SearchTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private SearchResponse searchResponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    searchResponse = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (i == 1) {
                if (searchResponse == null)
                    Toast.makeText(Search.this, "Invalid subs...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(searchResponse);
            } else
                Toast.makeText(Search.this, "Server error...", Toast.LENGTH_SHORT).show();
        }
    }
}