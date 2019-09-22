package info.horriblesubs.sher.ui.i;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.api.horrible.response.ShowItem;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.a.Ads;
import info.horriblesubs.sher.ui.i.fragment.Detail;
import info.horriblesubs.sher.ui.i.fragment.External;
import info.horriblesubs.sher.ui.i.fragment.Info;
import info.horriblesubs.sher.ui.i.fragment.ReleaseUI;
import info.horriblesubs.sher.ui.i.fragment.ReleasesA;
import info.horriblesubs.sher.ui.i.fragment.ReleasesB;
import info.horriblesubs.sher.ui.z.LoadingDialog;

public class Show extends AppCompatActivity
        implements TaskListener, Observer<ShowItem>, OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private LoadingDialog dialog;
    private ShowItem item;
    private String link;
    private Model model;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AppMe.appMe.setTheme();
        setContentView(R.layout.i_activity_0);
        model = new ViewModelProvider(this).get(Model.class);
        link = getIntent().getStringExtra("show.link");
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(this);
        Ads.InterstitialAd.load(this);
        Ads.BannerAd.load(this);
        Log.i("show.link", link);
        if (link == null || link.isEmpty()) {
            Toast.makeText(this, "Show Unavailable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        model.getResult(this).observe(this, this);
        if (model.result.getValue() == null) model.onRefresh(link, false);
    }

    @Override
    public void onPostExecute() {
        refreshLayout.setRefreshing(false);
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onChanged(ShowItem item) {
        if (item == null) {
            Toast.makeText(Show.this, "No Data Received", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 777);
            this.item = null;
            finish();
            return;
        }
        ReleasesA episodes = (ReleasesA) getSupportFragmentManager().findFragmentById(R.id.fragment5);
        External external = (External) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        ReleasesA batches = (ReleasesA) getSupportFragmentManager().findFragmentById(R.id.fragment4);
        Detail detail = (Detail) getSupportFragmentManager().findFragmentById(R.id.fragment1);
        Info info = (Info) getSupportFragmentManager().findFragmentById(R.id.fragment3);
        if (episodes != null) episodes.onRefresh(R.string.episodes, item.episodes);
        if (batches != null) batches.onRefresh(R.string.batches, item.batches);
        if (external != null) external.onRefresh(item.detail);
        if (detail != null) detail.onRefresh(item.detail);
        if (info != null) info.onRefresh(item.detail);
        this.item = item;
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (f != null) getSupportFragmentManager().popBackStack();
        else super.onBackPressed();
    }

    public void onViewOptions(ShowRelease release) {
        if (release != null && item != null && item.detail != null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, ReleaseUI.get(release, item.detail))
                    .addToBackStack("download.rls").commitAllowingStateLoss();
    }

    public void onViewMore(boolean b) {
        if (item != null && item.detail != null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, ReleasesB.get(b?item.batches: item.episodes))
                    .addToBackStack("download.rls").commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.onStopTask();
        onPostExecute();
        dialog = null;
    }

    @Override
    public void onPreExecute() {
        if (dialog == null)
            dialog = new LoadingDialog(this);
        dialog.show();
    }

    @Override
    public void onRefresh() {
        model.onRefresh(link, true);
    }
}