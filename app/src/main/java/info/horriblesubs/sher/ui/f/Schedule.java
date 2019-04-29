package info.horriblesubs.sher.ui.f;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.f.fragment.Day;
import info.horriblesubs.sher.ui.z.LoadingDialog;
import info.horriblesubs.sher.ui.z.navigation.Navigation;

@SuppressWarnings("all")
public class Schedule extends AppCompatActivity implements TaskListener,
        Toolbar.OnMenuItemClickListener, Observer<Result<ScheduleItem>> {

    private LoadingDialog dialog;
    private ViewPager viewPager;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.f_activity_0);
        viewPager = findViewById(R.id.viewPager);
        new Navigation(this, this);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        model = ViewModelProviders.of(this).get(Model.class);
        for (String s : getResources().getStringArray(R.array.days)) tabLayout.addTab(tabLayout.newTab().setText(s));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(true);
                return true;
            case R.id.browser:
                try {
                    String link = getResources().getString(R.string.horrible_subs_url) + "release-schedule/";
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
        String adId = getResources().getStringArray(R.array.footer)[1];
        FrameLayout layout = findViewById(R.id.adBanner);
        AdRequest request = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }

    @Override
    public void onChanged(Result<ScheduleItem> result) {
        if (result == null || result.items == null || result.items.size() == 0) {
            Toast.makeText(this, "No Data Received", Toast.LENGTH_SHORT).show();
            return;
        }
        viewPager.setAdapter(new Pager(getSupportFragmentManager(), result));
        viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    private class Pager extends FragmentPagerAdapter {

        private final Result<ScheduleItem> result;

        Pager(FragmentManager fragmentManager, Result<ScheduleItem> result) {
            super(fragmentManager);
            this.result = result;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 8;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return Day.newInstance(result, position);
        }
    }
}