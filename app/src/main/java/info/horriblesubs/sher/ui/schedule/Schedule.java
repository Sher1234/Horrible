package info.horriblesubs.sher.ui.schedule;

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
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.common.Change;
import info.horriblesubs.sher.common.DataTask;
import info.horriblesubs.sher.common.Navigate;
import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.base.ScheduleItem;
import info.horriblesubs.sher.model.response.ScheduleResponse;
import info.horriblesubs.sher.model.response.ShowsResponse;
import info.horriblesubs.sher.ui.schedule.fragment.Day;
import info.horriblesubs.sher.util.DialogX;

import static info.horriblesubs.sher.AppController.instance;

@SuppressLint("StaticFieldLeak")
public class Schedule extends AppCompatActivity
        implements DataTask.OnDataUpdate, Navigate.Task, Change {

    private InterstitialAd interstitialAd;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DataTask dataTask;
    private Navigate navigate;
    private DialogX dialogX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        dataTask = new DataTask(this);
        navigate = new Navigate(this);
        onAdShow();
        for (String s : getResources().getStringArray(R.array.days))
            tabLayout.addTab(tabLayout.newTab().setText(s));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        onStartActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigate.onResumeActivity();
    }

    private void onStartActivity() {
        List<ScheduleItem> list = instance.data.scheduleItems;
        if (list == null || list.size() == 0) {
            fetchData();
            return;
        }
        onScheduleUpdated(false, new ScheduleResponse(list));
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
        dataTask.fetchSchedule();
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
    public void onFavouritesUpdated(boolean b, List<PageItem> items) {

    }

    @Override
    public void onScheduleUpdated(boolean b, ScheduleResponse response) {
        if (!b && response != null && response.schedule != null && response.schedule.size() > 0) {
            instance.data.scheduleItems = response.schedule;
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), response));
            viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
        } else
            navigate.setError(View.VISIBLE);
        if (dialogX != null)
            dialogX.dismiss();
    }

    @Override
    public void onShowsUpdated(boolean b, ShowsResponse showsResponse) {

    }

    @Override
    public void onThemeChange(boolean b) {
        if (b) {
            tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_yellow));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.yellow));
            tabLayout.setTabRippleColorResource(R.color.yellowRipple);
            tabLayout.setBackgroundResource(R.color.primaryDark);
        } else {
            tabLayout.setTabTextColors(getResources().getColorStateList(R.color.tab_blue));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blue));
            tabLayout.setTabRippleColorResource(R.color.blueRipple);
            tabLayout.setBackgroundResource(android.R.color.white);
        }
        if (viewPager.getAdapter() != null)
            viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public int getActivity() {
        return 2;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final ScheduleResponse scheduleResponse;

        PagerAdapter(FragmentManager fragmentManager, ScheduleResponse scheduleResponse) {
            super(fragmentManager);
            this.scheduleResponse = scheduleResponse;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return Day.newInstance(scheduleResponse, position);
        }

        @Override
        public int getCount() {
            return 8;
        }
    }
}