package info.horriblesubs.sher.ui.horrible.favourites;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;
import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.FavouriteAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.common.LoadingDialog;
import info.horriblesubs.sher.ui.common.navigation.Horrible;
import info.horriblesubs.sher.ui.horrible.show.Show;

public class Favourites extends AppCompatActivity
        implements TaskListener, FavouriteAdapter.OnItemClick {

    private LoadingDialog loadingDialog;
    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.instance.getAppTheme()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.horrible_2_a);

        model = ViewModelProviders.of(this).get(Model.class);
        new Horrible(this, null);
        textView = findViewById(R.id.textInfo);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        onLoadViewModel();
        if (model.getItems().getValue() == null)
            onLoadInterstitialAd();
        onLoadAdBanner();
        model.onRefresh(this, this);
    }

    @Override
    public void onItemClicked(ShowDetail item) {
        if (item.link == null) return;
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    private void onLoadInterstitialAd() {
        String adId = getResources().getStringArray(R.array.interstitial)[new Random().nextInt(3)];
        final InterstitialAd interstitialAd = new InterstitialAd(this);
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
                }, 500);
            }
        });
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.setAdUnitId(adId);
        interstitialAd.loadAd(request);
    }

    private void onLoadViewModel() {
        model.getItems().observe(this, new Observer<List<ShowDetail>>() {
            @Override
            public void onChanged(List<ShowDetail> items) {
                if (items == null || items.size() == 0 || model.isEmpty(Favourites.this)) {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        if (items.size() < 5)
                            recyclerView.setLayoutManager(new LinearLayoutManager(Favourites.this));
                        else
                            recyclerView.setLayoutManager(new GridLayoutManager(Favourites.this, 2));
                    } else recyclerView.setLayoutManager(new GridLayoutManager(Favourites.this, 4));
                    recyclerView.setAdapter(FavouriteAdapter.getAll(Favourites.this, items));
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void onLoadAdBanner() {
        String adId = getResources().getStringArray(R.array.footer)[new Random().nextInt(4)];
        FrameLayout layout = findViewById(R.id.adBanner);
        AdRequest request = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }

    @Override
    public void onPostExecute() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    @Override
    public void onPreExecute() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) loadingDialog.dismiss();
        loadingDialog = null;
    }
}