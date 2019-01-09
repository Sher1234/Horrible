package info.horriblesubs.sher.ui.horrible.latest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import info.horriblesubs.sher.adapter.ListItemAdapter;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.common.LoadingDialog;
import info.horriblesubs.sher.ui.common.navigation.Horrible;
import info.horriblesubs.sher.ui.horrible.show.Show;

public class Latest extends AppCompatActivity
        implements TaskListener, ListItemAdapter.OnItemClick, Toolbar.OnMenuItemClickListener {

    private LoadingDialog loadingDialog;
    private RecyclerView recyclerView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.instance.getAppTheme()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.horrible_1_a);

        model = ViewModelProviders.of(this).get(Model.class);
        new Horrible(this, this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        onLoadViewModel();
        onLoadAdBanner();
        model.onLoadData(this, this);
        onLoadInterstitialAd();
    }

    private void onLoadViewModel() {
        model.getItems().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> items) {
                if (items == null || items.size() == 0) recyclerView.setVisibility(View.GONE);
                else {
                    recyclerView.setAdapter(ListItemAdapter.getAll(Latest.this, items));
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onPreExecute() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }

    @Override
    public void onPostExecute() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) loadingDialog.dismiss();
        loadingDialog = null;
    }

    @Override
    public void onItemClicked(ListItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(this, this);
                return true;
            case R.id.browser:
                try {
                    String link = getResources().getString(R.string.horrible_subs_url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, getResources().getString(R.string.app_not_available), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
        }
        return false;

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
        String adId = getResources().getStringArray(R.array.footer)[new Random().nextInt(4)];
        FrameLayout layout = findViewById(R.id.adBanner);
        AdRequest request = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }
}