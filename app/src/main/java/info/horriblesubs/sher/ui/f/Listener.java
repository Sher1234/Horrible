package info.horriblesubs.sher.ui.f;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

class Listener {

    static TabLayout.OnTabSelectedListener listener(final ViewPager2 viewPager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (viewPager.getAdapter() != null)
                    viewPager.getAdapter().notifyItemChanged(tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }
        };
    }

    static ViewPager2.OnPageChangeCallback listener(final TabLayout tabLayout) {
        return new ViewPager2.OnPageChangeCallback() {
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        };
    }
}