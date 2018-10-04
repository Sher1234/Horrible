package info.horriblesubs.sher.ui.favourites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ItemRecycler;
import info.horriblesubs.sher.common.Change;
import info.horriblesubs.sher.common.DataTask;
import info.horriblesubs.sher.common.Navigate;
import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.response.ScheduleResponse;
import info.horriblesubs.sher.model.response.ShowsResponse;
import info.horriblesubs.sher.util.DialogX;

import static info.horriblesubs.sher.AppController.instance;

@SuppressLint("StaticFieldLeak")
public class Favourites extends AppCompatActivity
        implements DataTask.OnDataUpdate, Navigate.Task, Change {

    private InterstitialAd interstitialAd;
    private RecyclerView recyclerView;
    private Navigate navigate;
    private DataTask dataTask;
    private DialogX dialogX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        recyclerView = findViewById(R.id.recyclerView);
        dataTask = new DataTask(this);
        navigate = new Navigate(this);
        onStartActivity();
        onAdShow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigate.onResumeActivity();
    }

    private void onStartActivity() {
        List<PageItem> list = instance.data.favouriteItems;
        if (list == null || list.size() == 0) {
            fetchData();
            return;
        }
        onFavouritesUpdated(false, list);
    }

    private void onAdShow() {
        FrameLayout layout = findViewById(R.id.adViewLayout);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_unit_footer_1));
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
    public void fetchData() {
        dataTask.fetchFavourites();
    }

    @Override
    public void preDataUpdate() {
        dialogX = new DialogX(this, true);
        dialogX.show();
    }

    @Override
    public void onLatestUpdated(boolean b, List<LatestItem> items) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void onFavouritesUpdated(boolean b, List<PageItem> items) {
        if (!b && items != null && items.size() > 0) {
            instance.data.favouriteItems = items;
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(new ItemRecycler(this, items));
        } else
            navigate.emptyFav();
        if (dialogX != null)
            dialogX.dismiss();
    }

    @Override
    public void onShowsUpdated(boolean b, ShowsResponse showsResponse) {

    }

    @Override
    public void onScheduleUpdated(boolean b, ScheduleResponse scheduleResponse) {

    }

    @Override
    public void onThemeChange(boolean b) {
        if (recyclerView.getAdapter() != null)
            ((Change) recyclerView.getAdapter()).onThemeChange(b);
    }

    @Override
    public int getActivity() {
        return 0;
    }
}