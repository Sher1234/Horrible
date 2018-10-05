package info.horriblesubs.sher.ui.latest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.LatestRecycler;
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
public class Latest extends AppCompatActivity
        implements DataTask.OnDataUpdate, Navigate.Task, Change {

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
        List<LatestItem> list = instance.data.latestReleases;
        if (list == null || list.size() == 0) {
            fetchData();
            return;
        }
        onLatestUpdated(false, list);
    }

    private void onAdShow() {
        FrameLayout layout = findViewById(R.id.adViewLayout);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ad_unit_footer_2));
        adView.setAdSize(AdSize.SMART_BANNER);
        layout.addView(adView);
        adView.loadAd(adRequest);
    }

    @Override
    public void fetchData() {
        dataTask.fetchLatest();
    }

    @Override
    public void preDataUpdate() {
        dialogX = new DialogX(this, true);
        navigate.setError(View.GONE);
        dialogX.show();
    }

    @Override
    public void onLatestUpdated(boolean b, List<LatestItem> items) {
        if (!b && items != null && items.size() > 0) {
            instance.data.latestReleases = items;
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new LatestRecycler(this, items));
        } else
            navigate.setError(View.VISIBLE);
        if (dialogX != null)
            dialogX.dismiss();
    }

    @Override
    public void onFavouritesUpdated(boolean b, List<PageItem> items) {

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
        return 1;
    }
}