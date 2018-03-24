package info.horriblesubs.sher.activity.beta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.util.BottomBarX;
import info.horriblesubs.sher.util.TopBarX;

public class Home extends AppCompatActivity {

    private TopBarX topBarX;
    private ViewPager viewPager;
    private BottomBarX bottomBarX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);

        viewPager = findViewById(R.id.viewPager);

        bottomBarX = new BottomBarX(findViewById(R.id.linearLayoutBottom), this);
        topBarX = new TopBarX(findViewById(R.id.linearLayoutTop), this);

        bottomBarX.setTitle("HorribleSubs");

        topBarX.setTab1("All", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                v.setSelected(true);
            }
        });
        topBarX.setTab2("Batches", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                v.setSelected(true);
            }
        });

        topBarX.selectTabOption(0);
        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homeViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                topBarX.selectTabOption(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class HomeViewPagerAdapter extends FragmentPagerAdapter {

        HomeViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return info.horriblesubs.sher.fragment.beta.Home.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}