package info.horriblesubs.sher.ui.shows;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ListRecycler;
import info.horriblesubs.sher.common.Change;
import info.horriblesubs.sher.common.DataTask;
import info.horriblesubs.sher.common.Navigate;
import info.horriblesubs.sher.model.base.Item;
import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.response.ScheduleResponse;
import info.horriblesubs.sher.model.response.ShowsResponse;
import info.horriblesubs.sher.util.DialogX;

import static info.horriblesubs.sher.AppController.instance;

@SuppressLint("StaticFieldLeak")
public class All extends AppCompatActivity
        implements DataTask.OnDataUpdate, Navigate.Task, Change {

    private InterstitialAd interstitialAd;

    private RecyclerView recyclerView;
    private DataTask dataTask;
    private Navigate navigate;
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
        List<Item> list = instance.data.allShows;
        if (list == null || list.size() == 0) {
            fetchData();
            return;
        }
        onShowsUpdated(false, new ShowsResponse(list, instance.data.currentShows));
    }

    private void onAdShow() {
        FrameLayout layout = findViewById(R.id.adViewLayout);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_unit_footer_3));
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
        dataTask.fetchShows();
    }

    @Override
    public void preDataUpdate() {
        dialogX = new DialogX(this, true);
        dialogX.show();
    }

    @Override
    public void onShowsUpdated(boolean b, ShowsResponse response) {
        if (!b && response != null && response.all != null && response.all.size() > 0) {
            instance.data.allShows = response.all;
            instance.data.currentShows = response.current;
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ListRecycler(this, response.all));
        } else
            navigate.setError(View.VISIBLE);
        if (dialogX != null)
            dialogX.dismiss();
    }

    @Override
    public void onLatestUpdated(boolean b, List<LatestItem> items) {

    }

    @Override
    public void onFavouritesUpdated(boolean b, List<PageItem> items) {

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
        return 4;
    }
}