package info.horriblesubs.sher.ui.horrible.schedule;

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
import java.util.List;
import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.ScheduleItems;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.common.LoadingDialog;
import info.horriblesubs.sher.ui.common.navigation.Horrible;
import info.horriblesubs.sher.ui.horrible.schedule.fragment.Day;

public class Schedule extends AppCompatActivity implements TaskListener, Toolbar.OnMenuItemClickListener {

    private LoadingDialog loadingDialog;
    private ViewPager viewPager;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.horrible_3_a);

        model = ViewModelProviders.of(this).get(Model.class);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        new Horrible(this, this);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        for (String s : getResources().getStringArray(R.array.days))
            tabLayout.addTab(tabLayout.newTab().setText(s));

        model.getItems().observe(this, new Observer<List<ScheduleItem>>() {
            @Override
            public void onChanged(List<ScheduleItem> items) {
                viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), items));
                viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
            }
        });

        onLoadAdBanner();
        if (model.getItems().getValue() == null) {
            model.onLoadData(this, this);
            onLoadInterstitialAd();
        }
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
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(this, this);
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
        String adId = getResources().getStringArray(R.array.footer)[new Random().nextInt(4)];
        FrameLayout layout = findViewById(R.id.adBanner);
        AdRequest request = new AdRequest.Builder().build();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }

    private class PageAdapter extends FragmentPagerAdapter {

        private final ScheduleItems scheduleItems;

        PageAdapter(FragmentManager fragmentManager, List<ScheduleItem> items) {
            super(fragmentManager);
            scheduleItems = new ScheduleItems();
            this.scheduleItems.items = items;
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
            return Day.newInstance(scheduleItems, position);
        }
    }
}