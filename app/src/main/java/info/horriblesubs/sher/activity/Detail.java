package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.fragment.Details;
import info.horriblesubs.sher.fragment.ShowReleases;
import info.horriblesubs.sher.model.PageItem;
import info.horriblesubs.sher.task.FetchPageItem;

@SuppressLint("StaticFieldLeak")
public class Detail extends AppCompatActivity {

    public static ImageView imageView = null;
    public static PageItem pageItem = null;
    private String link = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = findViewById(R.id.imageView);
        Picasso.with(this).load("http://horriblesubs.info/images/b/ccs_banner.jpg")
                .into(imageView);
        Intent intent = getIntent();
        if (intent.getStringExtra("link") != null)
            link = intent.getStringExtra("link");
        new FetchPageItem().execute("?mode=show-detail&link=" + link);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Details.newInstance();
                case 1:
                    return ShowReleases.newInstance(position);
                case 2:
                    return ShowReleases.newInstance(position);
                default:
                    return Details.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}