package info.horriblesubs.sher.ui.c;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
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

import java.util.List;
import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ListItemAdapter;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;
import info.horriblesubs.sher.ui.z.navigation.Navigation;

public class Search extends AppCompatActivity implements TaskListener, ListItemAdapter.OnItemClick,
        View.OnClickListener, Observer<List<ListItem>> {

    private AppCompatEditText searchText;
    private LoadingDialog dialog;
    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.c_activity_0);
        new Navigation(this, null);

        model = ViewModelProviders.of(this).get(Model.class);

        textView = findViewById(R.id.textView);
        searchText = findViewById(R.id.editSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        findViewById(R.id.imageSearch).setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, AppMe.appMe.isPortrait()?1:2));
        searchText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        model.getItems().observe(this, this);
        searchText.setSingleLine();
        onLoadInterstitialAd();
        onAdBannerShow();
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO || event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (v.getText() == null || v.getText().length() < 2) {
                        Toast.makeText(Search.this, "Invalid Search Term", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    model.onSearch(Search.this, v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClicked(ListItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    @Override
    public void onPreExecute() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    @Override
    public void onPostExecute() {
        if (dialog != null) dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) dialog.dismiss();
        model.onStopTask();
        dialog = null;
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

    private void onAdBannerShow() {
        String adId = getResources().getStringArray(R.array.footer)[2];
        AdRequest request = new AdRequest.Builder().build();
        FrameLayout layout = findViewById(R.id.adBanner);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adId);
        layout.addView(adView);
        adView.loadAd(request);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageSearch) {
            if (searchText.getText() == null || searchText.getText().length() < 2) {
                Toast.makeText(this, "Invalid Search Term", Toast.LENGTH_SHORT).show();
                return;
            }
            model.onSearch(this, searchText.getText().toString());
        }
    }

    @Override
    public void onChanged(List<ListItem> items) {
        if (items == null || items.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setAdapter(ListItemAdapter.get(Search.this, items));
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }
}