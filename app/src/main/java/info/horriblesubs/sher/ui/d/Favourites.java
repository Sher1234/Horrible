package info.horriblesubs.sher.ui.d;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import info.horriblesubs.sher.db.DataMethods;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;
import info.horriblesubs.sher.ui.z.navigation.Navigation;

public class Favourites extends AppCompatActivity
        implements TaskListener, FavouriteAdapter.OnItemClick, Observer<List<ShowDetail>>, Toolbar.OnMenuItemClickListener {

    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;
    private LoadingDialog dialog;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.d_activity_0);
        new Navigation(this, this);

        model = ViewModelProviders.of(this).get(Model.class);
        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.textView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, AppMe.appMe.isPortrait()?2:4));
        model.getItems(this, this).observe(this, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        onLoadInterstitialAd();
        model.onRefresh();
        onLoadAdBanner();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            adapter.setDelete(adapter.isDeleteDisabled());
            return true;
        }
        return false;
    }

    @Override
    public void onChanged(List<ShowDetail> items) {
        if (items == null || items.size() == 0 || model.isEmpty(Favourites.this)) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            adapter = null;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = FavouriteAdapter.get(this, items);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.onUpdateFavourites(items);
                adapter.notifyDataSetChanged();
            }
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDeleteClicked(ShowDetail item) {
        if (adapter.isDeleteDisabled() || item.sid == null) return;
        new DataMethods(this).onDeleteFavourite(item.sid);
        model.onRefresh();
    }

    @Override
    public void onItemClicked(ShowDetail item) {
        String link = item.link;
        if (link == null) return;
        if (link.contains("/shows/")) {
            String[] arr = link.split("/shows/");
            link = arr[arr.length - 1];
        }
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", link);
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

    private void onLoadAdBanner() {
        String adId = getResources().getStringArray(R.array.footer)[2];
        AdRequest request = new AdRequest.Builder().build();
        FrameLayout layout = findViewById(R.id.adBanner);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }

    @Override
    public void onPostExecute() {
        if (dialog != null) dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onPreExecute() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) dialog.dismiss();
        model.onStopTask();
        dialog = null;
    }
}