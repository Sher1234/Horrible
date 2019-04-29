package info.horriblesubs.sher.ui.i;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseAdapter;
import info.horriblesubs.sher.api.horrible.model.DateParse;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.api.horrible.response.ShowItem;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.DataMethods;
import info.horriblesubs.sher.ui.i.fragment.ReleaseUI;
import info.horriblesubs.sher.ui.i.fragment.Releases;
import info.horriblesubs.sher.ui.z.LoadingDialog;

public class Show extends AppCompatActivity implements TaskListener, ReleaseAdapter.OnItemClick,
        CompoundButton.OnCheckedChangeListener, Toolbar.OnMenuItemClickListener,
        Observer<ShowItem>, View.OnClickListener {

    private AppCompatTextView textView5, textView6, textNone1, textNone2;
    private AppCompatTextView textView1, textView2, textView3, textView4;
    private RecyclerView recyclerView1, recyclerView2;
    private AppCompatImageView imageView;
    private FloatingActionButton fab;
    private DataMethods functions;
    private MaterialButton button;
    private LoadingDialog dialog;
    private CheckBox checkBox;
    private ShowItem item;
    private String link;
    private Model model;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.i_activity_0);

        model = ViewModelProviders.of(this).get(Model.class);
        link = getIntent().getStringExtra("show.link");
        Log.i("show.link", link);
        if (link == null || link.length() < 2) {
            Toast.makeText(this, "Show Unavailable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        functions = new DataMethods(this);
        toolbar.inflateMenu(R.menu.main_2);
        toolbar.getMenu().findItem(R.id.theme).setEnabled(true).setVisible(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(this);
        fab = findViewById(R.id.fab);
        button = findViewById(R.id.button);
        checkBox = new CheckBox(this);
        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textNone1 = findViewById(R.id.textInfoA);
        textNone2 = findViewById(R.id.textInfoB);
        checkBox.setOnCheckedChangeListener(this);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        button.setOnClickListener(this);
        onLoadInterstitialAd();
        onLoadAdBanner();

        fab.setImageResource(R.drawable.ic_favorite_border);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        model.getResult(this).observe(this, this);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 2));
        if (model.result.getValue() == null) model.onRefresh(link, false);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        if (model.result.getValue() == null || model.result.getValue().detail == null) return;
        if (isChecked) {
            functions.onAddToFavourite(model.result.getValue().detail);
            fab.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            functions.onDeleteFavourite(model.result.getValue().detail.sid);
            fab.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public void onPostExecute() {
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(link, true);
                return true;
            case R.id.theme:
                AppMe.appMe.onToggleTheme();
                recreate();
                return true;
            case R.id.browser:
                if (model.result.getValue() == null || model.result.getValue().detail == null) return true;
                String s = model.result.getValue().detail.link;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                s = s.contains("shows")?s:"https://horriblesubs.info/shows/"+s;
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(s));
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onItemClicked(ShowRelease item) {
        if (this.item != null && this.item.detail != null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, ReleaseUI.get(item, this.item.detail))
                    .addToBackStack("download.rls")
                    .commitAllowingStateLoss();
    }

    @Override
    public void onChanged(ShowItem item) {
        if (item == null || item.detail == null) {
            Toast.makeText(Show.this, "No Data Received", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 600);
            finish();
            return;
        }
        if (item.episodes == null || item.episodes.size() == 0) {
            recyclerView2.setVisibility(View.GONE);
            textNone2.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        } else {
            recyclerView2.setAdapter(ReleaseAdapter.get(Show.this, item.episodes, 4));
            button.setVisibility(item.episodes.size() > 4?View.VISIBLE:View.GONE);
            Log.i("show.releases", String.valueOf(item.episodes.size()));
            recyclerView2.setVisibility(View.VISIBLE);
            textNone2.setVisibility(View.GONE);
        }
        if (item.batches == null || item.batches.size() == 0) {
            recyclerView1.setVisibility(View.GONE);
            textNone1.setVisibility(View.VISIBLE);
        } else {
            recyclerView1.setAdapter(ReleaseAdapter.get(this, item.batches));
            Log.i("show.batches", String.valueOf(item.batches.size()));
            recyclerView1.setVisibility(View.VISIBLE);
            textNone1.setVisibility(View.GONE);
        }
        Glide.with(Show.this).load(item.detail.image).into(imageView);
        textView6.setText(DateParse.getNetworkDate(item.detail.getTime()));
        checkBox.setChecked(functions.isFavourite(item.detail.sid));
        textView3.setText(String.valueOf(item.detail.views));
        textView4.setText(String.valueOf(item.detail.favs));
        textView1.setText(Html.fromHtml(item.detail.title));
        textView2.setText(Html.fromHtml(item.detail.body));
        Log.i("show.detail", item.detail.toString());
        textView5.setText(R.string.horrible_subs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
        this.item = item;
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
        String adId = getResources().getStringArray(R.array.footer)[3];
        AdRequest request = new AdRequest.Builder().build();
        FrameLayout layout = findViewById(R.id.adBanner);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (f != null) getSupportFragmentManager().popBackStack();
        else super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button && item != null  && item.detail != null
                && item.episodes != null && item.episodes.size() > 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, Releases.get(item))
                    .addToBackStack("download.rls")
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) dialog.dismiss();
        model.onStopTask();
        dialog = null;
    }

    @Override
    public void onPreExecute() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }
}