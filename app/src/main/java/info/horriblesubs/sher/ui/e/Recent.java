package info.horriblesubs.sher.ui.e;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ListItemAdapter;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.DataMethods;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;
import info.horriblesubs.sher.ui.z.navigation.Navigation;

public class Recent extends AppCompatActivity implements TaskListener, ListItemAdapter.OnItemClick,
        Toolbar.OnMenuItemClickListener, Observer<Result<ListItem>> {

    private RecyclerView recyclerView;
    private LoadingDialog dialog;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.e_activity_0);
        new Navigation(this, this);
        model = ViewModelProviders.of(this).get(Model.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, AppMe.appMe.isPortrait()?1:2));

        model.getItems(this).observe(this, this);
        if (model.result.getValue() == null) model.onRefresh(false);
        onLoadInterstitialAd();
        onLoadAdBanner();
    }

    @Override
    public void onPreExecute() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    @Override
    public void onPostExecute() {
        if (dialog != null) dialog.dismiss();
        dialog = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) dialog.dismiss();
        model.onStopTask();
        dialog = null;
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
                model.onRefresh(true);
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
        String adId = getResources().getStringArray(R.array.footer)[0];
        AdRequest request = new AdRequest.Builder().build();
        FrameLayout layout = findViewById(R.id.adBanner);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }

    @Override
    public void onChanged(Result<ListItem> result) {
        if (result == null || result.items == null || result.items.size() == 0)
            recyclerView.setVisibility(View.GONE);
        else {
            recyclerView.setAdapter(ListItemAdapter.get(Recent.this, result.items));
            new DataMethods(this).onResetNotifications(result.items);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}