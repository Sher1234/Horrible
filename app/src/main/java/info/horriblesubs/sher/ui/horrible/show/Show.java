package info.horriblesubs.sher.ui.horrible.show;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.HorribleDB;
import info.horriblesubs.sher.ui.common.LoadingDialog;
import info.horriblesubs.sher.ui.horrible.show.fragment.ReleaseUI;

public class Show extends AppCompatActivity implements TaskListener, ReleaseAdapter.OnItemClick,
        CompoundButton.OnCheckedChangeListener, Toolbar.OnMenuItemClickListener {

    private AppCompatTextView textView1, textView2, textView3, textView4;
    private RecyclerView recyclerView1, recyclerView2;
    private AppCompatImageView imageView;
    private LoadingDialog loadingDialog;
    private FloatingActionButton fab;
    private HorribleDB horribleDB;
    private CheckBox checkBox;
    private String link;
    private Model model;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.horrible_5_a);

        model = ViewModelProviders.of(this).get(Model.class);
        link = getIntent().getStringExtra("show.link");
        if (link == null || link.length() < 2) {
            Toast.makeText(this, "Show Unavailable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        horribleDB = new HorribleDB(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.horrible_menu);
        toolbar.getMenu().findItem(R.id.theme).setEnabled(true).setVisible(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(this);

        onLoadAdBanner();
        onLoadInterstitialAd();
        fab = findViewById(R.id.fab);
        checkBox = new CheckBox(this);
        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textInfoA);
        textView4 = findViewById(R.id.textInfoB);
        checkBox.setOnCheckedChangeListener(this);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        fab.setImageResource(R.drawable.ic_favorite_border);

        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        model.getDetail().observe(this, new Observer<ShowDetail>() {
            @Override
            public void onChanged(@Nullable ShowDetail detail) {
                if (detail == null) {
                    Toast.makeText(Show.this, "Network error", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 600);
                    finish();
                } else {
                    Glide.with(Show.this).load(detail.image).into(imageView);
                    checkBox.setChecked(horribleDB.isFavourite(detail.id));
                    textView1.setText(Html.fromHtml(detail.title));
                    textView2.setText(Html.fromHtml(detail.body));
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkBox.setChecked(!checkBox.isChecked());
                        }
                    });
                }
            }
        });
        model.getBatches().observe(this, new Observer<List<ShowRelease>>() {
            @Override
            public void onChanged(List<ShowRelease> releases) {
                if (releases == null || releases.size() == 0) {
                    recyclerView1.setVisibility(View.GONE);
                    textView3.setVisibility(View.VISIBLE);
                } else {
                    recyclerView1.setAdapter(new ReleaseAdapter(Show.this, releases));
                    recyclerView1.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.GONE);
                }
            }
        });
        model.getReleases().observe(this, new Observer<List<ShowRelease>>() {
            @Override
            public void onChanged(List<ShowRelease> releases) {
                if (releases == null || releases.size() == 0) {
                    recyclerView2.setVisibility(View.GONE);
                    textView4.setVisibility(View.VISIBLE);
                } else {
                    recyclerView2.setAdapter(new ReleaseAdapter(Show.this, releases));
                    recyclerView2.setVisibility(View.VISIBLE);
                    textView4.setVisibility(View.GONE);
                }
            }
        });
        if (model.getDetail().getValue() == null) model.onRefresh(this, link);
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (f != null) getSupportFragmentManager().popBackStack();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) loadingDialog.dismiss();
        loadingDialog = null;
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
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        if (model.getDetail() == null || model.getDetail().getValue() == null) return;
        if (isChecked) {
            horribleDB.addToFavourites(model.getDetail().getValue());
            fab.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            horribleDB.removeFromFavourites(model.getDetail().getValue().id);
            fab.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(this, link);
                return true;
            case R.id.theme:
                AppMe.appMe.onToggleTheme();
                recreate();
                return true;
            case R.id.browser:
                if (model.getDetail() == null || model.getDetail().getValue() == null) return true;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(model.getDetail().getValue().link));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onItemClicked(ShowRelease item) {
        if (model.getDetail() != null && model.getDetail().getValue() != null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout,
                            ReleaseUI.newInstance(item, model.getDetail().getValue().image))
                    .addToBackStack("download.rls")
                    .commitAllowingStateLoss();
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
}