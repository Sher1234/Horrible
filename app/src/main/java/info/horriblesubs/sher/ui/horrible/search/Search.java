package info.horriblesubs.sher.ui.horrible.search;

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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import info.horriblesubs.sher.ui.common.LoadingDialog;
import info.horriblesubs.sher.ui.common.navigation.Horrible;
import info.horriblesubs.sher.ui.horrible.show.Show;

public class Search extends AppCompatActivity
        implements TaskListener, ListItemAdapter.OnItemClick, View.OnClickListener {

    private AppCompatEditText searchText;
    private LoadingDialog loadingDialog;
    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.instance.getAppTheme()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.horrible_4_a);

        model = ViewModelProviders.of(this).get(Model.class);
        new Horrible(this, null);

        textView = findViewById(R.id.textInfo);
        searchText = findViewById(R.id.editSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        findViewById(R.id.imageSearch).setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onLoadInterstitialAd();
        onLoadViewModel();
        onAdBannerShow();
        searchText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        searchText.setSingleLine();
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

    private void onLoadViewModel() {
        model.getItems().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> items) {
                if (items == null || items.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setAdapter(ListItemAdapter.getAll(Search.this, items));
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
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
        String adId = getResources().getStringArray(R.array.footer)[new Random().nextInt(4)];
        FrameLayout layout = findViewById(R.id.adBanner);
        AdRequest request = new AdRequest.Builder().build();
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
}